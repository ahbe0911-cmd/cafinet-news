package com.cafinet.news.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TelegramChannelTest {
    @Test
    fun `normalizes usernames and public links`() {
        assertEquals("mehrnews", normalizeTelegramUsername("@MehrNews"))
        assertEquals("irna_1313", normalizeTelegramUsername("https://t.me/s/IRNA_1313/123"))
        assertEquals("iribnews", normalizeTelegramUsername("telegram.me/iribnews?ref=app"))
    }

    @Test
    fun `rejects private invites and invalid usernames`() {
        assertNull(normalizeTelegramUsername("https://t.me/+privateHash"))
        assertNull(normalizeTelegramUsername("https://t.me/joinchat/example"))
        assertNull(normalizeTelegramUsername("bad name"))
        assertNull(normalizeTelegramUsername("abc"))
    }
}
