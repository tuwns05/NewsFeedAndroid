using System;
using System.Collections.Generic;

namespace BENewsFeed.Models;

public partial class Source
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string RssUrl { get; set; } = null!;

    public bool? IsActive { get; set; }

    public int? CategoryId { get; set; }

    public virtual ICollection<Article> Articles { get; set; } = new List<Article>();
    public Category Category { get; set; } = null!;
}
