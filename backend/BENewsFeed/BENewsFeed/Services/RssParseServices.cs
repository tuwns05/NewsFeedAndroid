using BENewsFeed.Models;
using Microsoft.EntityFrameworkCore;
using System.ServiceModel.Syndication;
using System.Xml.Linq;

namespace BENewsFeed.Services
{
    //Đọc RSS từ các nguồn, trích xuất thông tin và lưu vào database bảng Articles
    public class RssParseServices
    {
        public readonly NewsFeedDbContext _context;

        public RssParseServices(NewsFeedDbContext context)
        {
            _context = context;
        }

        public async Task FetchAllSoucesAsync()
        {
            try
            {
                var sources = await _context.Sources.Where(s => s.IsActive == true).ToListAsync();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error fetching sources: {ex.Message}");
            }
        }

        private async Task FetchSourceAsync(Source source)
        {
            using var reader = new System.Xml.XmlTextReader(source.RssUrl);

            var feed = SyndicationFeed.Load(reader);

            var defaultCategoryId = 1;

            foreach (var item in feed.Items)
            {
                var link = item.Links.FirstOrDefault()?.Uri.ToString() ?? "";

                // Bỏ qua nếu bài đã tồn tại
                if (await _context.Articles.AnyAsync(a => a.Link == link))
                    continue;

                var article = new Article
                {
                    SourceId = source.Id,
                    CategoryId = defaultCategoryId,
                    Title = item.Title?.Text ?? "",
                    Description = item.Summary?.Text ?? "",
                    ImageUrl = ExtractImage(item),
                    Link = link,
                    PublishedAt = item.PublishDate.DateTime,
                    FetchedAt = DateTime.Now
                };

                _context.Articles.Add(article);
            }
        }

        // Hàm trích xuất URL hình ảnh từ item
        private string ExtractImage(SyndicationItem item)
        {
            foreach(var ext in item.ElementExtensions)
            {
                try
                {
                    var el = ext.GetObject<XElement>();
                    if( el.Name.LocalName == "thumbnail" || el.Name.LocalName == "enclosure")
                    {
                        return el.Attribute("url")?.Value ?? "";
                    }
                }
                catch { }

            }
            var enclosure = item.Links.FirstOrDefault(l => l.RelationshipType == "enclosure");

            return enclosure?.Uri.ToString() ?? "";
        }
    }
}
