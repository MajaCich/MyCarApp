package com.example.common.screen;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.Header;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import com.example.common.MyCarAppSession;
import com.example.common.R;

public class MainScreen extends Screen {

   MyCarAppSession session;

    public MainScreen(@NonNull CarContext carContext, MyCarAppSession session) {
        super(carContext);
        this.session=session;
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        ItemList.Builder listBuilder = new ItemList.Builder();

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_example_title))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new ColorScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.custom_color_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new CustomColorScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.permission_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new RequestPermissionScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.car_info_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new CarInfoScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.car_sensors_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new CarSensorsScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.car_span_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new CarSpanScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.car_icon_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new IconExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.long_message_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new LongMessageTemplateScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.message_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new MessageTemplateScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.list_template_screen))
                        .setOnClickListener(
                                () -> getScreenManager()
                                                .push( new ListTemplateScreen( getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.pane_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new PaneTemplateExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.tab_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new TabTemplateExample(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.grid_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new GridTemplateExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.selectable_list_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new SelectableListExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.selectable_grid_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new SelectableGridExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.signin_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new SignInExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.search_template_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new SearchTemplateExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.record_audio_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new RecordAudioExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.speech_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new SpeechRecognitionExampleScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.library_usage_screen))
                        .setOnClickListener(
                                () ->
                                        getScreenManager()
                                                .push(
                                                        new MyLibraryTestScreen(
                                                                getCarContext())))
                        .setBrowsable(true)
                        .build());

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.main_screen_title))
                .setStartHeaderAction(Action.APP_ICON)
                .build();

        return new ListTemplate.Builder()
                .setSingleList(listBuilder.build())
                .setHeader(header)
                .build();
    }
}

