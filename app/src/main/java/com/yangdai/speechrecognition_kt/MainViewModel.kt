package com.yangdai.speechrecognition_kt

import android.app.Application
import android.speech.SpeechRecognizer
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var myRecognitionListener: MyRecognitionListener = MyRecognitionListener()
    var mySpeechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)

    init {
        mySpeechRecognizer.setRecognitionListener(myRecognitionListener)
    }

    override fun onCleared() {
        super.onCleared()
        mySpeechRecognizer.destroy()
    }
}