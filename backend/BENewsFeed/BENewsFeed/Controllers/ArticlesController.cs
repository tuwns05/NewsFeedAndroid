using BENewsFeed.dto;
using BENewsFeed.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BENewsFeed.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ArticlesController : Controller
    {
        private readonly NewsFeedDbContext _context;

        public ArticlesController(NewsFeedDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll([FromQuery] string? category, [FromQuery] int? sourceId,
            [FromQuery] int page = 1, [FromQuery] int pageSize = 10)
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
            .Skip((page - 1) * pageSize)
            .Take(pageSize)
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
    }
}
