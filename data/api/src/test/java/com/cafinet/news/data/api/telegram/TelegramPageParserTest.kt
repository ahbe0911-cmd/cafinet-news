package com.cafinet.news.data.api.telegram

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TelegramPageParserTest {
    private val parser = TelegramPageParser(nowMillis = { 1_725_196_800_000L })

    @Test
    fun `parses a public Telegram post`() {
        val page = """
            <html><body>
              <div class="tgme_channel_info_header_title"><span>خبرگزاری نمونه</span></div>
              <div class="tgme_widget_message" data-post="sample_news/1234">
                <a class="tgme_widget_message_photo_wrap"
                   style="background-image:url('https://cdn.example.test/photo.jpg')"></a>
                <div class="tgme_widget_message_text">تیتر خبر نمونه<br>این متن کامل پست است.</div>
                <span class="tgme_widget_message_views">1.2K</span>
                <a class="tgme_widget_message_date" href="/sample_news/1234">
                  <time datetime="2024-09-01T12:00:00+00:00"></time>
                </a>
              </div>
            </body></html>
        """.trimIndent()

        val result = parser.parse(page, "sample_news")
        val post = result.posts.single()

        assertEquals("خبرگزاری نمونه", result.title)
        assertEquals(1234L, post.telegramMessageId)
        assertEquals("sample_news", post.channelUsername)
        assertEquals("تیتر خبر نمونه", post.title)
        assertEquals("این متن کامل پست است.", post.description)
        assertEquals("https://cdn.example.test/photo.jpg", post.imageUrl)
        assertEquals("https://t.me/sample_news/1234", post.postUrl)
        assertEquals(1_200, post.viewCount)
        assertTrue(post.id > 0)
    }

    @Test
    fun `keeps media-only posts useful`() {
        val page = """
            <div class="tgme_channel_info_header_title"><span>کانال ویدئو</span></div>
            <div class="tgme_widget_message" data-post="video_feed/77">
              <video src="https://cdn.example.test/video.mp4"
                     poster="https://cdn.example.test/poster.jpg"></video>
              <a class="tgme_widget_message_date" href="https://t.me/video_feed/77">
                <time datetime="2024-09-01T13:00:00+00:00"></time>
              </a>
            </div>
        """.trimIndent()

        val post = parser.parse(page, "video_feed").posts.single()

        assertEquals("ویدئوی جدید از کانال ویدئو", post.title)
        assertEquals("https://cdn.example.test/video.mp4", post.videoUrl)
        assertEquals("https://cdn.example.test/poster.jpg", post.imageUrl)
    }
}
