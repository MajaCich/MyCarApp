package com.example.common.screen;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

import android.text.SpannableString;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.CarIconSpan;
import androidx.car.app.model.Distance;
import androidx.car.app.model.DistanceSpan;
import androidx.car.app.model.DurationSpan;
import androidx.car.app.model.ForegroundCarColorSpan;
import androidx.car.app.model.Header;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;

public class CarSpanScreen extends Screen {
    public CarSpanScreen(@NonNull CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        Pane.Builder pane = new Pane.Builder();

        /*
         // Można użyć w SignInTemplate - CliclableSpan nie działa dla Row.addText - jest zabronione
         OnClickListener listener = () -> {
            CarToast.makeText(getCarContext(),
                    "Kliknięto napis z ClickableSpan",
                    CarToast.LENGTH_LONG).show();
        };
        SpannableString clickableString = new SpannableString("Text with a clickable span");
        clickableString.setSpan(ClickableSpan.create(listener), 0, clickableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        Row.Builder clickableSpanRow = new Row.Builder()
                .setTitle("Przykład ClickableSpan")
                .addText(clickableString);

        pane.addRow(clickableSpanRow.build());
         */

        SpannableString string = new SpannableString(getCarContext().getString(R.string.car_icon_span_text));
        string.setSpan(
                CarIconSpan.create(new CarIcon.Builder(
                        IconCompat.createWithResource(getCarContext(), R.drawable.android)).setTint(CarColor.BLUE).build(),
                        CarIconSpan.ALIGN_CENTER),
                14, 15, SPAN_INCLUSIVE_EXCLUSIVE);

        Row.Builder carIconSpanRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.car_icon_span))
                .addText(string);

        pane.addRow(carIconSpanRow.build());

        SpannableString distanceString = new SpannableString(getCarContext().getString(R.string.distance_span_text));
        Distance distance = Distance.create(10, Distance.UNIT_KILOMETERS);
        distanceString.setSpan(DistanceSpan.create(distance), 17, 18, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        Row.Builder distanceSpanRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.distance_span))
                .addText(distanceString);

        pane.addRow(distanceSpanRow.build());

        SpannableString durationString = new SpannableString(getCarContext().getString(R.string.duration_span_text));
        durationString.setSpan(DurationSpan.create(10000), 15, 16, SPAN_INCLUSIVE_INCLUSIVE);

        Row.Builder durationSpanRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.duration_span))
                .addText(durationString);

        pane.addRow(durationSpanRow.build());

        SpannableString colorString = new SpannableString(getCarContext().getString(R.string.color_span_text));
        colorString.setSpan(ForegroundCarColorSpan.create(CarColor.RED), 2, 6,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        colorString.setSpan(ForegroundCarColorSpan.create(CarColor.YELLOW), 9, 12,  SPAN_INCLUSIVE_EXCLUSIVE);

        Row.Builder carColorSpanRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.color_span))
                .addText(colorString);
        
        pane.addRow(carColorSpanRow.build());

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.car_span_screen_title))
                .setStartHeaderAction(Action.BACK)
                .build();

        return new PaneTemplate.Builder(pane.build())
                .setHeader(header)
                .build();
    }
}
