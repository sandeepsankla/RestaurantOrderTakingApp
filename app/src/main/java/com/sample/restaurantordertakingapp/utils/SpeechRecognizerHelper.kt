package com.sample.restaurantordertakingapp.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

sealed class VoiceState {
    object Idle : VoiceState()
    object Listening : VoiceState()
    data class Success(val spokenText: String) : VoiceState()
    data class Error(val message: String) : VoiceState()
}

class SpeechRecognizerHelper(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null

    private val _voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val voiceState: StateFlow<VoiceState> = _voiceState

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _voiceState.value = VoiceState.Error("Speech recognition is not supported on this device")
            return
        }

        stopListening() // Clean up existing instance if any

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _voiceState.value = VoiceState.Listening
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    _voiceState.value = VoiceState.Idle
                }

                override fun onError(error: Int) {
                    val errorMsg = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Please try speaking clearly."
                        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network error. Please check your internet connection."
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error."
                        else -> "Speech recognition failed. Try again."
                    }
                    _voiceState.value = VoiceState.Error(errorMsg)
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val spokenText = matches[0]
                        _voiceState.value = VoiceState.Success(spokenText)
                    } else {
                        _voiceState.value = VoiceState.Error("Could not understand voice command.")
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("hi", "IN").toString())
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Order bolye (e.g. 2 half chowmein)...")
        }

        _voiceState.value = VoiceState.Listening
        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
        _voiceState.value = VoiceState.Idle
    }
}
