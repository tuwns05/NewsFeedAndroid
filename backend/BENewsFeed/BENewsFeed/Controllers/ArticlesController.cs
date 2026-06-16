using BENewsFeed.dto;
using BENewsFeed.Models;
using BENewsFeed.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using static Microsoft.Extensions.Logging.EventSource.LoggingEventSource;

namespace BENewsFeed.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ArticlesController : Controller
    {
        private readonly NewsFeedDbContext _context;
        private readonly RssParseServices _rssService;

        public ArticlesController(NewsFeedDbContext context, RssParseServices rssService)
        {
            _context = context;
            _rssService = rssService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll([FromQuery] string? category, [FromQuery] int? sourceId)
        {
            var query = _context.Articles.Include(a=> a.Source).Include(a => a.Category).AsQueryable();

            //Lọc theo chuyên mục
            if (!string.IsNullOrEmpty(category))
            {
                query = query.Where(a => a.Category.Slug == category);
            }

            //Lọc theo nguồn báo

            if(sourceId.HasValue)
            {
                query = query.Where(a => a.SourceId == sourceId.Value);
            }

                var articles = await query
            .OrderByDescending(a => a.PublishedAt)
            .Select(a => new ArticleDto
            {
                Id = a.Id,
                Title = a.Title,
                Description = a.Description ?? "",
                ImageUrl = a.ImageUrl ?? "",
                Link = a.Link,
                PublishedAt = a.PublishedAt.HasValue
                    ? a.PublishedAt.Value.ToString("dd/MM/yyyy HH:mm")
                    : "",
                SourceName = a.Source.Name,
                Category = a.Category.Name
            })
            .ToListAsync();

            return Ok(articles);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var article = await _context.Articles
                .Include(a => a.Source)
                .Include(a => a.Category)
                .Where(a => a.Id == id)
                .Select(a => new ArticleDto
                {
                    Id = a.Id,
                    Title = a.Title,
                    Description = a.Description ?? "",
                    ImageUrl = a.ImageUrl ?? "",
                    Link = a.Link,
                    PublishedAt = a.PublishedAt.HasValue
                        ? a.PublishedAt.Value.ToString("dd/MM/yyyy HH:mm")
                        : "",
                    SourceName = a.Source.Name,
                    Category = a.Category.Name
                })
                .FirstOrDefaultAsync();
            if (article == null)
            {
                return NotFound();
            }
            return Ok(article);
        }

        [HttpPost("refresh")]
        public async Task<IActionResult> Refresh()
        {
            try
            {
                await _rssService.FetchAllSoucesAsync();

                return Ok(new
                {
                    success = true,
                    message = "Cập nhật tin tức thành công."
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new
                {
                    success = false,
                    message = "Có lỗi xảy ra khi cập nhật tin tức.",
                    error = ex.Message
                });
            }
        }

        //Search đầu vào là 1 chuỗi string trả về danh sách bài viết có tiêu đề chứa string đó
        [HttpGet("search")]
        public async Task<IActionResult> Search([FromQuery] String key)
        {
            if(String.IsNullOrEmpty(key))
            {
                return BadRequest(new
                {
                    success = false,
                    message = "Vui lòng cung cấp từ khóa tìm kiếm."
                });
            }
            var articles = await _context.Articles
            .Include(a => a.Source)
             .Include(a => a.Category)
             .Where(a => a.Title.Contains(key))
             .OrderByDescending(a => a.PublishedAt)
             .Select(a => new ArticleDto
             {
                 Id = a.Id,
                 Title = a.Title,
                 Description = a.Description ?? "",
                 ImageUrl = a.ImageUrl ?? "",
                 Link = a.Link,
                 PublishedAt = a.PublishedAt.HasValue
                     ? a.PublishedAt.Value.ToString("dd/MM/yyyy HH:mm")
                     : "",
                 SourceName = a.Source.Name,
                 Category = a.Category.Name
             })
             .ToListAsync();
            // Return Ok là trả về kết quả tìm kiếm dưới dạng JSON
                 return Ok(articles);
                }
            }
}
