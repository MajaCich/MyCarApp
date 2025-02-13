package com.example.common.screen;

import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.Header;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.Template;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.carvoicecommands.SpeechRecognitionHandler;
import com.example.common.R;

import org.jspecify.annotations.NonNull;

public class MyLibraryTestScreen extends Screen {

    private String commandText = " ";

    private  SpeechRecognitionHandler speechRecognitionHandler;

    void handleCommand(String command)
    {
        commandText = command;
        invalidate();
    }

    public MyLibraryTestScreen(CarContext carContext)
    {
        super(carContext);

        Lifecycle lifecycle = getLifecycle();

        lifecycle.addObserver(new DefaultLifecycleObserver()  {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner){
                speechRecognitionHandler = new SpeechRecognitionHandler(getCarContext());
                speechRecognitionHandler.setCommandHandler(command -> handleCommand(command));
            }

            @Override
            public void onDestroy(@NonNull LifecycleOwner owner){
                speechRecognitionHandler.clear();
            }
        });

    }

    @Override
    public @NonNull Template onGetTemplate() {

        Action microphoneAction = speechRecognitionHandler.getPreMadeMicrophoneAction();

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.speech_screen_title))
                .setStartHeaderAction(Action.BACK)
                .addEndHeaderAction(microphoneAction)
                .build();

        return new MessageTemplate.Builder(getCarContext().getString(R.string.speech_screen_text) + " " + commandText)
                .setHeader(header)
                .build();
    }
}
