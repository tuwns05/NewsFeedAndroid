using BENewsFeed.Models;
using Microsoft.AspNetCore.Mvc;

namespace BENewsFeed.Controllers
{
    //lấy danh sách các danh mục (Categories)
    [ApiController]
    [Route("api/[controller]")]
    public class CategoriesController : Controller
    {
            private readonly NewsFeedDbContext _context;
            public CategoriesController(NewsFeedDbContext context)
            {
                _context = context;
            }

            [HttpGet]
            public IActionResult GetAll()
            {
                var categories = _context.Categories
                    .Select(c => new
                    {
                        c.Id,
                        c.Name,
                        c.Slug
                    })
                    .ToList();
                return Ok(categories);
            }
        }
    
}
