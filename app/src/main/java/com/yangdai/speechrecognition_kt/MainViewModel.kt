package com.yangdai.speechrecognition_kt

import android.app.Application
import android.speech.SpeechRecognizer
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var mySpeechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)

    override fun onCleared() {
        super.onCleared()
        mySpeechRecognizer.destroy()
    }
}