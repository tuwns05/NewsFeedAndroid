namespace BENewsFeed.Services
{
    public class NewsUpdateService : BackgroundService
    {
        private readonly IServiceProvider _service;

        public NewsUpdateService(IServiceProvider service)
        {
            _service = service;
        }

        protected override async Task ExecuteAsync(CancellationToken cl)
        {
            await FetchNewsAsync();

            using var timer = new PeriodicTimer(TimeSpan.FromHours(6));
            while (await timer.WaitForNextTickAsync(cl))
            {
                await FetchNewsAsync();
            }

        }
        
        private async Task FetchNewsAsync()
        {
            try
            {
                using var scope = _service.CreateScope();
                var rssService = scope.ServiceProvider.GetRequiredService<RssParseServices>();
                await rssService.FetchAllSoucesAsync();

            }
            catch(Exception ex) {
                ex.ToString();
            }
        }


    }
}
