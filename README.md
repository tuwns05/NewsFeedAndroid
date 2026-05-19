NewsFeed — Đọc báo thông minh
Ứng dụng Android đọc tin tức tổng hợp từ nhiều nguồn báo Việt Nam qua RSS Feed, kết hợp .NET REST API backend và Kotlin Jetpack Compose.

Công nghệ sử dụng
Backend: ASP.NET Core Web API, Entity Framework Core, SQL Server
Android: Kotlin, Jetpack Compose, Room Database, Retrofit2, Navigation Compose

Tính năng chính
1. Trang chủ

Hiển thị danh sách tin tức mới nhất từ nhiều nguồn (VnExpress, Tuổi Trẻ, Zing News)
Lọc tin theo chuyên mục: Công nghệ, Thể thao, Giáo dục, Giải trí
Pull to refresh cập nhật tin mới
Tin đã đọc hiển thị mờ để phân biệt

2. Chi tiết bài viết

Xem nội dung đầy đủ bài báo
Lưu bài vào danh sách yêu thích (Room Database)
Mở bài gốc trên trình duyệt (Intent)
Chia sẻ bài viết ra ứng dụng khác (Content Provider)
Toast thông báo khi lưu thành công
Sử dụng Text To Speech API của Android xây dựng chức năng tự động đọc báo.

3. Tin đã lưu

Danh sách bài viết đã bookmark, xem được khi offline
Vuốt để xóa, Dialog xác nhận trước khi xóa
Sắp xếp theo ngày lưu

4. Tìm kiếm

Tìm kiếm bài viết theo từ khóa, tiêu đề
Lọc theo nguồn báo và chuyên mục
Lịch sử tìm kiếm gần đây
