package com.example.carvoicecommands;

import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.ParcelFileDescriptor;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import androidx.car.app.CarAppPermission;
import androidx.car.app.CarContext;
import androidx.car.app.media.CarAudioRecord;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarIcon;
import androidx.core.graphics.drawable.IconCompat;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SpeechRecognitionHandler {
    private CarContext carContext;
    private SpeechRecognizer speechRecognizer;
    private CarAudioRecord carAudioRecord;
    private final AtomicBoolean isListening = new AtomicBoolean(false);
    private ParcelFileDescriptor readSide = null;
    private ParcelFileDescriptor.AutoCloseOutputStream writeSide = null;
    SimpleRecognitionListener recognitionListener;

    public Action getPreMadeMicrophoneAction() {
        CarIcon microphoneIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        carContext,
                        R.drawable.microphone))
                .build();

        return new Action.Builder()
                .setIcon(microphoneIcon)
                .setOnClickListener(
                        () ->
                        {
                            if (carAudioRecord != null && speechRecognizer != null) {
                                handleSpeechBegin();
                            }
                        }
                )
                .build();
    }

    private boolean checkAudioPermission() {
        try {
            CarAppPermission.checkHasPermission(carContext, "android.permission.RECORD_AUDIO");
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    public SpeechRecognitionHandler(CarContext carContext) {
        this.carContext = carContext;


        if (checkAudioPermission()) {
            carAudioRecord = CarAudioRecord.create(carContext);
            Log.e("SpeechRecognitionExampleScreen", "carAudioRecord został zainicjalizowany.");
        }

        try {
            ParcelFileDescriptor[] fileDescriptors = ParcelFileDescriptor.createPipe();
            readSide = fileDescriptors[0];
            writeSide = new ParcelFileDescriptor.AutoCloseOutputStream(fileDescriptors[1]);
        } catch (IOException e) {
            Log.e("SpeechRecognitionExampleScreen", "Błąd tworzenia ParcelFileDescriptor", e);
            throw new RuntimeException(e);
        }

        if (SpeechRecognizer.isRecognitionAvailable(carContext)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(carContext);

            recognitionListener = new SimpleRecognitionListener() {
                @Override
                public void handleSpeechEnd() {
                    speechEnd();
                }
            };

            speechRecognizer.setRecognitionListener(recognitionListener);

            Log.i("SpeechRecognizer", "SpeechRecognizer został zainicjalizowany.");
        } else {
            Log.e("SpeechRecognizer", "Rozpoznawanie mowy nie jest dostępne.");
        }

    }

    public void setCommandHandler(Consumer<String> commandHandler) {
        recognitionListener.setCommandHandler(commandHandler);
    }

    public void clear() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            Log.i("SpeechRecognizer", "SpeechRecognizer został zniszczony.");
        }

        if (writeSide != null) {
            try {
                writeSide.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.d("SpeechRecognizerScreen", "outputStream został zamknięty.");
        }
    }


    private void startRecordingToParcelFile() {

        AudioAttributes audioAttributes =
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                        .build();

        AudioFocusRequest audioFocusRequest =
                new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                        .setAudioAttributes(audioAttributes)
                        .setOnAudioFocusChangeListener(state -> {
                            if (state == AudioManager.AUDIOFOCUS_LOSS) {
                                carAudioRecord.stopRecording();
                            }
                        })
                        .build();

        if (carContext.getSystemService(AudioManager.class).requestAudioFocus(audioFocusRequest)
                != AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }

        try {
            carAudioRecord.startRecording();
            isListening.set(true);
            Log.d("AudioRecorder", "Rozpoczęto nagrywanie za pomocą startRecording");
            byte[] buffer = new byte[CarAudioRecord.AUDIO_CONTENT_BUFFER_SIZE];
            while (isListening.get()) {
                int bytesRead = carAudioRecord.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    writeSide.write(buffer, 0, bytesRead);
                } else {
                    Log.d("AudioRecorder", "Brak danych audio, zatrzymuję nagrywanie - koniec pętli");
                    isListening.set(false);
                }
            }
        } catch (IOException e) {
            Log.e("AudioRecorder", "Błąd podczas zapisu audio", e);
        } finally {
            if (carAudioRecord != null) {
                carAudioRecord.stopRecording();

                carContext.getSystemService(AudioManager.class).abandonAudioFocusRequest(audioFocusRequest);

                Log.d("AudioRecorder", "Nagrywanie zakończone i zasoby zostały zwolnione.");
            }
            isListening.set(false);
        }
    }

    private Intent createIntent() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pl-PL");

        if (readSide != null) {
            Log.d("SpeechRecognitionExampleScreen", "Extra audio source");
            i.putExtra(RecognizerIntent.EXTRA_SEGMENTED_SESSION, RecognizerIntent.EXTRA_AUDIO_SOURCE);
            i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE, readSide);
            i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_CHANNEL_COUNT, 1);
            i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_ENCODING, AudioFormat.ENCODING_PCM_16BIT);
            i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_SAMPLING_RATE, CarAudioRecord.AUDIO_CONTENT_SAMPLING_RATE);
        }
        return i;
    }

    private void handleSpeechBegin() {
        if (!isListening.get()) {
            Intent intent = createIntent();
            if (carAudioRecord != null && speechRecognizer != null && writeSide != null) {
                new Thread(this::startRecordingToParcelFile, "CarAudioRecordingThread").start();
                speechRecognizer.startListening(intent);
                Log.d("SpeechRecognitionExampleScreen", "Rozpoczęto rozpoznawanie mowy z mikrofonu samochodu");
            }
        }
    }
    
    public void startVoiceControl() {
        handleSpeechBegin();
    }

    private void speechEnd() {
        if (isListening.get()) {
            speechRecognizer.stopListening();
            isListening.set(false);
        }
        Log.d("SpeechRecognitionExampleScreen", "Zakończono rozpoznawanie mowy i nagrywanie");
    }

}

