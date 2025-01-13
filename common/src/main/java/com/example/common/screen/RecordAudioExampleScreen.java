package com.example.common.screen;

import static android.content.pm.PackageManager.FEATURE_AUTOMOTIVE;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.Header;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.ParkedOnlyOnClickListener;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;
import com.example.common.utils.AudioRecorder;

import java.util.ArrayList;
import java.util.List;

public class RecordAudioExampleScreen extends Screen {
    public RecordAudioExampleScreen(CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.audio_recorder_screen_title))
                .setStartHeaderAction(Action.BACK)
                .build();

        AudioRecorder audioRecorder = new AudioRecorder(getCarContext());

        if (!audioRecorder.checkAudioPermission()) {
            audioRecorder.create();
            List<String> permissions = new ArrayList<>();
            permissions.add("android.permission.RECORD_AUDIO");
            OnClickListener listener = ParkedOnlyOnClickListener.create(() -> {
                getCarContext().requestPermissions(
                        permissions,
                        (approved, rejected) -> {
                            CarToast.makeText(
                                    getCarContext(),
                                    String.format("Approved: %s Rejected: %s", approved, rejected),
                                    CarToast.LENGTH_LONG).show();
                            invalidate();
                        });
                if (!getCarContext().getPackageManager().hasSystemFeature(FEATURE_AUTOMOTIVE)) {
                    CarToast.makeText(getCarContext(),
                            getCarContext().getString(R.string.phone_screen_permission_msg),
                            CarToast.LENGTH_LONG).show();
                }
            });

            Action permissionAction = new Action.Builder()
                    .setTitle(getCarContext().getString(R.string.grant_access_action_title))
                    .setOnClickListener(listener)
                    .build();

            return new MessageTemplate.Builder(getCarContext().getString(R.string.no_audio_permission))
                    .setHeader(header)
                    .addAction(permissionAction)
                    .build();
        } else {
            CarIcon microphoneIcon = new CarIcon.Builder(
                    IconCompat.createWithResource(
                            getCarContext(),
                            R.drawable.microphone))
                    .build();

            Action microphoneAction = new Action.Builder()
                    .setTitle(getCarContext().getString(R.string.record))
                    .setIcon(microphoneIcon)
                    .setBackgroundColor(CarColor.PRIMARY)
                    .setOnClickListener(
                            () ->
                            {
                                audioRecorder.create();
                                audioRecorder.start();
                            }
                    )
                    .build();

            CarIcon playIcon = new CarIcon.Builder(
                    IconCompat.createWithResource(
                            getCarContext(),
                            R.drawable.play))
                    .build();

            Action playAction = new Action.Builder()
                    .setTitle(getCarContext().getString(R.string.replay_latest_audio))
                    .setIcon(playIcon)
                    .setBackgroundColor(CarColor.PRIMARY)
                    .setOnClickListener(
                            audioRecorder::playAudio
                    )
                    .build();

            return new MessageTemplate.Builder(getCarContext().getString(R.string.record_audio_screen_message))
                    .setHeader(header)
                    .addAction(microphoneAction)
                    .addAction(playAction)
                    .build();
        }
    }
}
