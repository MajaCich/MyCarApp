package com.example.common.utils;

import static android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
import static android.media.AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE;
import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioFormat.ENCODING_DEFAULT;
import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
import static androidx.car.app.media.CarAudioRecord.AUDIO_CONTENT_BUFFER_SIZE;
import static androidx.car.app.media.CarAudioRecord.AUDIO_CONTENT_SAMPLING_RATE;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import androidx.car.app.CarAppPermission;
import androidx.car.app.CarContext;
import androidx.car.app.media.CarAudioRecord;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
//content-mime - audio/l16

public class AudioRecorder {

    private final CarContext carContext;

    private final String FILE_NAME = "audio.wav";

    private CarAudioRecord carAudioRecord;

    public AudioRecorder(CarContext carContext) {
        this.carContext = carContext;
    }

    public boolean checkAudioPermission() {
        try {
            CarAppPermission.checkHasPermission(carContext, "android.permission.RECORD_AUDIO");
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    public void create()
    {
        if(checkAudioPermission())
        {
            carAudioRecord = CarAudioRecord.create(carContext);
        }
    }

    public void start() {
        Thread recordingThread =
                new Thread(
                        this::record,
                        "AudioRecorder Thread");
        recordingThread.start();
    }

    private void record() {
        AudioAttributes audioAttributes =
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                        .build();

        AudioFocusRequest audioFocusRequest =
                new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)
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

        if(carAudioRecord==null) return;

        carAudioRecord.startRecording();
        Log.d("AudioRecroder", "Rozpoczęto nagrywanie");

        byte[] data = new byte[CarAudioRecord.AUDIO_CONTENT_BUFFER_SIZE];
        List<Byte> audioDataList = new ArrayList<>();

        while (carAudioRecord.read(data, 0, CarAudioRecord.AUDIO_CONTENT_BUFFER_SIZE) >= 0) {
            for (byte b : data) {
                audioDataList.add(b);
            }
        }
        writeToWavFile(audioDataList);
        carAudioRecord.stopRecording();
        Log.d("AudioRecroder", "Zakończono nagrywanie");
    }

    void writeToWavFile(List<Byte> audioData)
    {
        try {
            OutputStream outputStream = carContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            int audiolength = audioData.size() + 36;
            byte[] header = new byte[44];
            int dataElementSize = 16;
            long longSampleRate = AUDIO_CONTENT_SAMPLING_RATE;
            int channels = 1;
            int byteRate = (int) (longSampleRate * channels * dataElementSize / 8);

            header[0] = 'R';  // RIFF/WAVE header
            header[1] = 'I';
            header[2] = 'F';
            header[3] = 'F';
            header[4] = (byte) (audiolength & 0xff);
            header[5] = (byte) ((audiolength >> 8) & 0xff);
            header[6] = (byte) ((audiolength >> 16) & 0xff);
            header[7] = (byte) ((audiolength >> 24) & 0xff);
            header[8] = 'W';
            header[9] = 'A';
            header[10] = 'V';
            header[11] = 'E';
            header[12] = 'f';  // 'fmt ' chunk
            header[13] = 'm';
            header[14] = 't';
            header[15] = ' ';
            header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
            header[17] = 0;
            header[18] = 0;
            header[19] = 0;
            header[20] = 1;  // format = 1 PCM
            header[21] = 0;
            header[22] = 1; // Num channels (mono)
            header[23] = 0;
            header[24] = (byte) (longSampleRate & 0xff); // sample rate
            header[25] = (byte) ((longSampleRate >> 8) & 0xff);
            header[26] = (byte) ((longSampleRate >> 16) & 0xff);
            header[27] = (byte) ((longSampleRate >> 24) & 0xff);

            header[28] = (byte) (byteRate & 0xff);
            header[29] = (byte) ((byteRate >> 8) & 0xff);
            header[30] = (byte) ((byteRate >> 16) & 0xff);
            header[31] = (byte) ((byteRate >> 24) & 0xff);

            header[32] = (byte) (channels * dataElementSize / 8);
            header[33] = 0;
            header[34] = (byte) (dataElementSize & 0xff);
            header[35] = (byte) ((dataElementSize >> 8) & 0xff);

            header[36] = 'd';
            header[37] = 'a';
            header[38] = 't';
            header[39] = 'a';
            header[40] = (byte) (audiolength & 0xff);
            header[41] = (byte) ((audiolength >> 8) & 0xff);
            header[42] = (byte) ((audiolength >> 16) & 0xff);
            header[43] = (byte) ((audiolength >> 24) & 0xff);

            outputStream.write(header, 0, 44);

            for (Byte b : audioData) {
                outputStream.write(b);
            }
            outputStream.flush();
            outputStream.close();

            Log.d("AudioRecorder", "Audio zapisano do pliku");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void playAudio() {

        AudioAttributes audioAttributes =
                new AudioAttributes.Builder()
                        .setContentType(CONTENT_TYPE_MUSIC)
                        .setUsage(USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                        .build();

        AudioFocusRequest audioFocusRequest =
                new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)
                        .setAudioAttributes(audioAttributes)
                        .setOnAudioFocusChangeListener(state -> {
                            if (state == AudioManager.AUDIOFOCUS_LOSS) {
                                return;
                            }
                        })
                        .build();

        AudioManager audioManager = carContext.getSystemService(AudioManager.class);
        if (audioManager.requestAudioFocus(audioFocusRequest) != AUDIOFOCUS_REQUEST_GRANTED) {
            Log.e("AudioRecorder", "Nie udało się uzyskać Audio Focus do odtwarzania");
            return;
        }

        InputStream inputStream;
        try {
            inputStream = carContext.openFileInput(FILE_NAME);
        } catch (FileNotFoundException e) {
            Log.e("AudioRecorder", "Nie znaleziono pliku audio");
            return;
        }

        AudioTrack audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                        .setContentType(CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(ENCODING_DEFAULT)
                        .setSampleRate(AUDIO_CONTENT_SAMPLING_RATE)
                        .setChannelMask(CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(AUDIO_CONTENT_BUFFER_SIZE)
                .build();
        audioTrack.play();
        try {
            Log.d("AudioRecorder", "Tu juz powinno odtwarzać");
            while (inputStream.available() > 0) {
                byte[] audioData = new byte[AUDIO_CONTENT_BUFFER_SIZE];
                int size = inputStream.read(audioData, 0, audioData.length);

                if (size < 0) {
                    return;
                }
                audioTrack.write(audioData, 0, size);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        audioTrack.stop();

        carContext.getSystemService(AudioManager.class).abandonAudioFocusRequest(
                audioFocusRequest);
    }
}
