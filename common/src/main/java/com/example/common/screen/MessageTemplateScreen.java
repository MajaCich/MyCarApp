package com.example.common.screen;

import static androidx.car.app.model.CarIcon.APP_ICON;

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
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;

public class MessageTemplateScreen extends Screen {
    public MessageTemplateScreen(CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        OnClickListener listener = () -> CarToast.makeText(getCarContext(),
                getCarContext().getString(R.string.more_clicked),
                CarToast.LENGTH_LONG).show();

        OnClickListener infoListener = () -> CarToast.makeText(getCarContext(),
                getCarContext().getString(R.string.information),
                CarToast.LENGTH_LONG).show();

        Action.Builder moreActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.more))
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(listener);

        CarIcon carIconAndroidStudio = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.android_studio))
                .build();

        Action.Builder iconActionBuilder = new Action.Builder()
                .setIcon(carIconAndroidStudio)
                .setOnClickListener(listener);

        CarIcon carIconAccount = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.account))
                .build();

        Action.Builder settingsActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.settings))
                .setIcon(carIconAccount)
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(listener);

        Action.Builder infoActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.information))
                .setIcon(APP_ICON)
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(infoListener)
                .setFlags(Action.FLAG_PRIMARY)
                .setEnabled(true);

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.message_template_screen_title))
                .setStartHeaderAction(Action.BACK)
                .addEndHeaderAction(settingsActionBuilder.build())
                .addEndHeaderAction(iconActionBuilder.build())
                .build();

        MessageTemplate.Builder messageBuilder = new MessageTemplate.Builder(getCarContext().getString(R.string.message_template_message))
                .setHeader(header)
                .setIcon(CarIcon.ALERT)
                .addAction(infoActionBuilder.build())
                .addAction(moreActionBuilder.build())
                .setLoading(false);

        return messageBuilder.build();
    }
}
