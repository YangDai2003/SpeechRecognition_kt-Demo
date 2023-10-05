package com.yangdai.speechrecognition_kt

import android.os.Bundle
import android.speech.RecognitionListener
import android.util.Log
import androidx.compose.runtime.MutableState

class MyRecognitionListener(private val result: MutableState<String>) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onBeginningOfSpeech() {
        Log.i("onBeginningOfSpeech", "onBeginningOfSpeech")
        result.value = ""
    }

    override fun onRmsChanged(rmsdB: Float) {
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(error: Int) {
    }

    override fun onResults(results: Bundle?) {
        val resultArray = results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
        result.value = resultArray?.get(0) ?: ""
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val resultArray = partialResults?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
        result.value = resultArray?.get(0) ?: ""
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }
}

