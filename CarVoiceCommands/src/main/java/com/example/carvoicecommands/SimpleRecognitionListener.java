package com.example.carvoicecommands;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class SimpleRecognitionListener implements RecognitionListener {

    private  Consumer<String> commandHandler;

    public void setCommandHandler(Consumer<String> commandHandler)
    {
        this.commandHandler = commandHandler;
    }

    protected SimpleRecognitionListener() {
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("SpeechRecognizer", "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("SpeechRecognizer", "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d("SpeechRecognizer", "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("SpeechRecognizer", "onEndOfSpeech");
        handleSpeechEnd();
    }

    @Override
    public void onError(int error) {
        Log.d("SpeechRecognizer", "onError" + error);
        handleSpeechEnd();
    }

    @Override
    public void onResults(Bundle results) {
        Log.d("SpeechRecognizer", "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d("SpeechRecognizer", "matches: " + matches);
        if (matches != null && !matches.isEmpty()) {
            String command = matches.get(0);
            Log.d("SpeechRecognizer", "Wykryto komendę: " + command);
            commandHandler.accept(command);
        }
        handleSpeechEnd();
    }

    @Override
    public void onSegmentResults(@NonNull Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            String command = matches.get(0);
            Log.d("SpeechRecognizer", "(onSegmentResults) Wykryto komendę: " + command);
            commandHandler.accept(command);
        }
    }

    @Override
    public void onEndOfSegmentedSession() {
        Log.d("SpeechRecognizer", "Zakończono sesję segmentową");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d("SpeechRecognizer", "onPartialResults");
        ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            String partialText = matches.get(0);
            Log.d("SpeechRecognizer", "Wykryto częściową komendę: " + partialText);
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d("SpeechRecognizer", "onEvent");
    }

    public abstract void handleSpeechEnd();

}
