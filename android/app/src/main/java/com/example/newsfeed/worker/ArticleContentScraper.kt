package com.example.newsfeed.worker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL

object ArticleContentScraper {
    suspend fun fetchArticleContent(url: String): String = withContext(Dispatchers.IO) {
        try {
            val doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                            .get()
            val host = URL(url).host
            val selector = selectorForHost(host)
            val paragraphs = doc.select(selector)
            val text = paragraphs.joinToString("\n\n") { it.text().trim() }
                .filter { true } // giữ nguyên, có thể lọc thêm nếu cần
                .let { it }

            text.ifBlank { "Không tìm thấy nội dung bài báo." }
        }catch (e: Exception){
            "Lỗi khi tải nội dung: ${e.localizedMessage}"
        }
    }
    // Mỗi trang báo có cấu trúc HTML khác nhau -> cần selector riêng
    private fun selectorForHost(host: String): String = when {
        host.contains("vnexpress") -> "article.fck_detail p.Normal, article.fck_detail figure figcaption"
        host.contains("tuoitre")   -> "div.detail-content.afcbc-body p"
        host.contains("thanhnien") -> "div.detail-content p"
        host.contains("dantri")    -> "div.singular-content p"
        host.contains("vietnamnet")-> "div.maincontent p"
        host.contains("baochinhphu") -> "div.detail-content p"
        else -> "article p, div.content p, p" // fallback chung
    }
}