namespace BENewsFeed.dto
{
    public class ArticleDto
    {
        public int Id { get; set; }
        public string Title { get; set; } = "";
        public string Description { get; set; } = "";
        public string ImageUrl { get; set; } = "";
        public string Link { get; set; } = "";
        public string PublishedAt { get; set; } = "";
        public string SourceName { get; set; } = "";
        public string Category { get; set; } = "";
    }
}
