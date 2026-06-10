using System;
using System.Collections.Generic;

namespace BENewsFeed.Models;

public partial class Article
{
    public int Id { get; set; }

    public int SourceId { get; set; }

    public int CategoryId { get; set; }

    public string Title { get; set; } = null!;

    public string? Description { get; set; }

    public string? ImageUrl { get; set; }

    public string Link { get; set; } = null!;

    public DateTime? PublishedAt { get; set; }

    public DateTime? FetchedAt { get; set; }

    public virtual Category Category { get; set; } = null!;

    public virtual Source Source { get; set; } = null!;
}
