package com.example.newsfeed.worker

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSManager(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("vi", "VN"))
                when (result) {
                    TextToSpeech.LANG_MISSING_DATA,
                    TextToSpeech.LANG_NOT_SUPPORTED -> {
                        Log.e("TTS", "Thiếu gói giọng đọc tiếng Việt trên máy này")
                        // có thể mở màn hình cài đặt TTS data:
                        // context.startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA))
                    }
                    else -> {
                        isReady = true
                        Log.d("TTS", "Sẵn sàng phát âm")
                    }
                }
            } else {
                Log.e("TTS", "Khởi tạo TTS thất bại, status=$status")
            }
        }
    }

    fun speak(text: String) {
        if (!isReady) {
            Log.w("TTS", "TTS chưa sẵn sàng, bỏ qua")
            return
        }
        if (text.isBlank()) {
            Log.w("TTS", "Nội dung rỗng, không có gì để đọc")
            return
        }
        val maxLen = tts?.let { TextToSpeech.getMaxSpeechInputLength() } ?: 4000
        Log.d("TTS", "Độ dài nội dung = ${text.length}, giới hạn cho phép = $maxLen")

        tts?.stop()

        // Nếu dài hơn giới hạn, chia nhỏ thành nhiều đoạn và đọc nối tiếp
        if (text.length > maxLen) {
            val chunks = text.chunked(maxLen)
            chunks.forEachIndexed { index, chunk ->
                val queueMode = if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
                val result = tts?.speak(chunk, queueMode, null, "article_$index")
                Log.d("TTS", "speak() đoạn $index, kết quả = $result")
            }
        } else {
            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "article")
            Log.d("TTS", "speak() kết quả = $result (0 = SUCCESS, -1 = ERROR)")
        }
    }

    fun stop() = tts?.stop()

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}