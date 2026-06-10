using BENewsFeed.dto;
using BENewsFeed.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BENewsFeed.Controllers
{
    //Controller này lấy tất cả nguồn tin đang được kích hoạt (IsActive = true),
    //chỉ lấy Id và Name, rồi trả về cho frontend thông qua API GET /api/sources
    [ApiController]
    [Route("api/[controller]")]
    public class SourcesController : Controller
    {
        private readonly NewsFeedDbContext _db;

        public SourcesController(NewsFeedDbContext db) => _db = db;

        // GET /api/sources
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var sources = await _db.Sources
                .Where(s => s.IsActive == true)
                .Select(s => new SourceDto
                {
                    Id = s.Id,
                    Name = s.Name
                })
                .ToListAsync();

            return Ok(sources);
        }
    }
}
