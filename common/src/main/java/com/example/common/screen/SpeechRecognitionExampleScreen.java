package com.example.common.screen;

import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.car.app.CarAppPermission;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.media.CarAudioRecord;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.Header;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.common.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpeechRecognitionExampleScreen extends Screen {
    private SpeechRecognizer speechRecognizer;
    private CarAudioRecord carAudioRecord;
    private final AtomicBoolean isListening = new AtomicBoolean(false);
    private List<String> commandList = null;
    private String commandText = "";
    ParcelFileDescriptor readSide = null;
    ParcelFileDescriptor.AutoCloseOutputStream writeSide = null;

    private boolean checkAudioPermission() {
        try {
            CarAppPermission.checkHasPermission(getCarContext(), "android.permission.RECORD_AUDIO");
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    public SpeechRecognitionExampleScreen(CarContext carContext) {
        super(carContext);
        Lifecycle lifecycle = getLifecycle();

        lifecycle.addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
                CarContext carContext = getCarContext();

                commandList = Arrays.asList(
                        "cancel",
                        "home",
                        "settings",
                        "more",
                        "info"
                );

                if (checkAudioPermission()) {
                    carAudioRecord = CarAudioRecord.create(getCarContext());
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

                    speechRecognizer.setRecognitionListener(new RecognitionListener() {
                        @Override
                        public void onReadyForSpeech(Bundle params) {
                            // Wywoływane, gdy rozpoznawanie mowy jest gotowe do rozpoczęcia.
                            Log.d("SpeechRecognizer", "onReadyForSpeech");
                        }

                        @Override
                        public void onBeginningOfSpeech() {
                            // Wywoływane, gdy użytkownik zaczyna mówić.
                            Log.d("SpeechRecognizer", "onBeginningOfSpeech");
                        }

                        @Override
                        public void onRmsChanged(float rmsdB) {
                            // Wywoływane, gdy zmienia się poziom głośności głosu.
                            //Log.d("SpeechRecognizer", "onRmsChanged");
                        }

                        @Override
                        public void onBufferReceived(byte[] buffer) {
                            // Wywoływane, gdy odbierane są dane dźwiękowe.
                            Log.d("SpeechRecognizer", "onBufferReceived");
                        }

                        @Override
                        public void onEndOfSpeech() {
                            // Wywoływane, gdy użytkownik kończy mówić.
                            Log.d("SpeechRecognizer", "onEndOfSpeech");
                            handleSpeechEnd();
                        }

                        @Override
                        public void onError(int error) {
                            // Wywoływane, gdy wystąpi błąd w rozpoznawaniu mowy.
                            Log.d("SpeechRecognizer", "onError" + error);
                            handleSpeechEnd();
                        }

                        @Override
                        public void onResults(Bundle results) {
                            // Wywoływane, gdy wyniki rozpoznawania są gotowe. To wywołanie zwrotne zostanie wywołane, gdy
                            // sesja audio została zakończona, a wypowiedź użytkownika została przeanalizowana.
                            Log.d("SpeechRecognizer", "onResults");
                            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                            Log.d("SpeechRecognizer", "matches: " + matches);
                            if (matches != null && !matches.isEmpty()) {
                                String command = matches.get(0);
                                Log.d("SpeechRecognizer", "Wykryto komendę: " + command);
                                handleCommand(command);
                            }
                            handleSpeechEnd();
                        }

                        @Override
                        public void onSegmentResults(@NonNull Bundle results) {
                            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                            if (matches != null && !matches.isEmpty()) {
                                String command = matches.get(0);
                                Log.d("SpeechRecognizer", "(onSegmentResults) Wykryto komendę: " + command);
                                handleCommand(command);
                            }
                        }

                        @Override
                        public void onEndOfSegmentedSession() {
                            Log.d("SpeechRecognizer", "Zakończono sesję segmentową");
                        }

                        @Override
                        public void onPartialResults(Bundle partialResults) {
                            Log.d("SpeechRecognizer", "onPartialResults");
                            // Wywoływane, gdy dostępne są częściowe wyniki rozpoznawania mowy.
                            ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                            if (matches != null && !matches.isEmpty()) {
                                // Pobierz najbardziej pewny częściowy wynik
                                String partialText = matches.get(0);
                                Log.d("SpeechRecognizer", "Wykryto częściową komendę: " + partialText);
                            }
                        }

                        @Override
                        public void onEvent(int eventType, Bundle params) {
                            // Wywoływane dla niestandardowych zdarzeń.
                            Log.d("SpeechRecognizer", "onEvent");
                        }
                    });

                    Log.i("SpeechRecognizer", "SpeechRecognizer został zainicjalizowany.");
                } else {
                    Log.e("SpeechRecognizer", "Rozpoznawanie mowy nie jest dostępne.");
                }
            }
        });

        lifecycle.addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
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
        });
    }

    public void startRecordingToParcelFile() {

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

        if (getCarContext().getSystemService(AudioManager.class).requestAudioFocus(audioFocusRequest)
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

                getCarContext().getSystemService(AudioManager.class).abandonAudioFocusRequest(audioFocusRequest);

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("SpeechRecognitionExampleScreen", "Extra audio source");
                i.putExtra(RecognizerIntent.EXTRA_SEGMENTED_SESSION, RecognizerIntent.EXTRA_AUDIO_SOURCE);
                i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE, readSide);
                i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_CHANNEL_COUNT, 1);
                i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_ENCODING, AudioFormat.ENCODING_PCM_16BIT);
                i.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_SAMPLING_RATE, CarAudioRecord.AUDIO_CONTENT_SAMPLING_RATE);
            }
        }
        return i;
    }

    private void handleSpeechBegin() {
        if (!isListening.get()) {
            Intent intent = createIntent();
            if (carAudioRecord != null && writeSide != null) {
                new Thread(this::startRecordingToParcelFile, "CarAudioRecordingThread").start();
            }

            if (speechRecognizer != null) {
                speechRecognizer.startListening(intent);
                Log.d("SpeechRecognitionExampleScreen", "Rozpoczęto rozpoznawanie mowy z mikrofonu samochodu");
            }
        }
    }

    private void goToCarInfo() {
        getScreenManager().push(new CarInfoScreen(getCarContext()));
    }

    private void goToSettings() {
        getScreenManager().push(new SelectableListExampleScreen(getCarContext()));
    }

    private void handleCommand(String command) {
        command = command.toLowerCase();
        commandText = command;
        Log.d("SpeechRecognizer", "Handle command: " + command);

        switch (command) {
            case "more":
                goToCarInfo();
                break;
            case "settings":
                goToSettings();
                break;
            case "home":
                getScreenManager().popToRoot();
                break;
            default:
                CarToast.makeText(getCarContext(), "Nieznan komenda",
                        CarToast.LENGTH_LONG).show();
                break;
        }

        if (commandList != null && commandList.contains(command)) {
            CarToast.makeText(getCarContext(), "Executing: " + command, CarToast.LENGTH_LONG).show();
        } else {
            CarToast.makeText(getCarContext(), "Could not recognize command", CarToast.LENGTH_LONG).show();
        }
        invalidate();
    }
    private void handleSpeechEnd() {
        if (isListening.get()) {
            speechRecognizer.stopListening();
            isListening.set(false);
        }
        Log.d("SpeechRecognitionExampleScreen", "Zakończono rozpoznawanie mowy i nagrywanie");
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        CarIcon microphoneIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.microphone))
                .build();

        Action microphoneAction = new Action.Builder()
                .setIcon(microphoneIcon)
                .setBackgroundColor(CarColor.PRIMARY)
                .setOnClickListener(
                        () ->
                        {
                            if (carAudioRecord != null && speechRecognizer != null) {
                                handleSpeechBegin();
                            }
                        }
                )
                .build();

        CarIcon plusIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.plus_circle))
                .build();

        Action moreAction = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.more))
                .setIcon(plusIcon)
                .setOnClickListener(this::goToCarInfo)
                .build();

        Action infoAction = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.information))
                .build();

        Action settingsAction = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.settings))
                .setOnClickListener(this::goToSettings)
                .build();

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.speech_screen_title))
                .setStartHeaderAction(Action.BACK)
                .addEndHeaderAction(settingsAction)
                .addEndHeaderAction(microphoneAction)
                .build();

        return new MessageTemplate.Builder(getCarContext().getString(R.string.speech_screen_text) + " " + commandText)
                .setHeader(header)
                .addAction(infoAction)
                .addAction(moreAction)
                .build();
    }
}

