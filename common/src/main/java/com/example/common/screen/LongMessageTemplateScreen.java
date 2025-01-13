package com.example.common.screen;

import static androidx.car.app.model.Action.BACK;
import static androidx.car.app.CarToast.LENGTH_LONG;
import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.Header;
import androidx.car.app.model.LongMessageTemplate;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.ParkedOnlyOnClickListener;
import androidx.car.app.model.Template;
import androidx.car.app.versioning.CarAppApiLevels;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;

public class LongMessageTemplateScreen extends Screen {

    public LongMessageTemplateScreen(CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        if (getCarContext().getCarAppApiLevel() < CarAppApiLevels.LEVEL_2) {

            Header header = new Header.Builder()
                    .setTitle(getCarContext().getString(R.string.very_long_message_title))
                    .setStartHeaderAction(Action.BACK)
                    .build();

            return new MessageTemplate.Builder(getCarContext().getString(R.string.no_support_long_message))
                    .setHeader(header)
                    .build();
        }

        OnClickListener moreListener = ParkedOnlyOnClickListener.create(() -> CarToast.makeText(getCarContext(), getCarContext().getString(R.string.more_toast_msg), LENGTH_LONG).show());
        OnClickListener nextListener = ParkedOnlyOnClickListener.create(() -> CarToast.makeText(getCarContext(), getCarContext().getString(R.string.next), LENGTH_LONG).show());
        CarIcon carIcon1 = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.android))
                .build();

        Action.Builder moreActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.more))
                .setIcon(carIcon1)
                .setOnClickListener(moreListener);

        Action.Builder next = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.next))
                .setBackgroundColor(CarColor.GREEN)
                .setOnClickListener(nextListener);

        return new LongMessageTemplate.Builder(getCarContext().getString(R.string.very_long_message))
                .setTitle(getCarContext().getString(R.string.very_long_message_title))
                .setHeaderAction(BACK)
                .addAction(moreActionBuilder.build())
                .addAction(next.build())
                .build();
    }
}
