package com.example.common.screen;

import static androidx.car.app.model.Action.BACK;
import static androidx.car.app.model.CarColor.BLUE;
import static androidx.car.app.model.CarColor.DEFAULT;
import static androidx.car.app.model.CarColor.GREEN;
import static androidx.car.app.model.CarColor.PRIMARY;
import static androidx.car.app.model.CarColor.RED;
import static androidx.car.app.model.CarColor.SECONDARY;
import static androidx.car.app.model.CarColor.YELLOW;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.ForegroundCarColorSpan;
import androidx.car.app.model.Header;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;
import com.example.common.R;
public class ColorScreen extends Screen {
    public ColorScreen(CarContext carContext) {
        super(carContext);
    }

    private static CharSequence colorize(@NonNull String s, @NonNull CarColor color, int start, int end)
    {
        SpannableString string = new SpannableString(s);
        string.setSpan(ForegroundCarColorSpan.create(color), start, end,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        ItemList.Builder listBuilder = new ItemList.Builder();

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_red))
                        .addText(colorize(getCarContext().getString(R.string.red_text),
                                RED, 0, getCarContext().getString(R.string.red_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(RED)
                                .build())
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_yellow))
                        .addText(colorize(getCarContext().getString(R.string.yellow_text),
                                YELLOW, 0, getCarContext().getString(R.string.yellow_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(YELLOW)
                                .build())
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_blue))
                        .addText(colorize(getCarContext().getString(R.string.blue_text),
                                BLUE, 0, getCarContext().getString(R.string.blue_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(BLUE)
                                .build())
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_green))
                        .addText(colorize(getCarContext().getString(R.string.green_text),
                                GREEN, 0, getCarContext().getString(R.string.green_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(GREEN)
                                .build())
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_primary))
                        .addText(colorize(getCarContext().getString(R.string.primary_color_text),
                                PRIMARY, 0, getCarContext().getString(R.string.primary_color_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(PRIMARY)
                                .build())
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_secondary))
                        .addText(colorize(getCarContext().getString(R.string.secondary_color_text),
                                SECONDARY, 0, getCarContext().getString(R.string.secondary_color_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(SECONDARY)
                                .build())
                        .build());

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_default))
                        .addText(colorize(getCarContext().getString(R.string.secondary_color_text),
                                DEFAULT, 0, getCarContext().getString(R.string.secondary_color_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(DEFAULT)
                                .build())
                        .build());

        String hexColor = "#25888F";
        int color = Color.parseColor(hexColor);

        String hexColorDark = "#094347";
        int colorDark = Color.parseColor(hexColor);

        CarColor MY_CUSTOM_COLOR = CarColor.createCustom(color, colorDark);

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(getCarContext().getString(R.string.color_custom))
                        .addText(colorize(getCarContext().getString(R.string.custom_color_text),
                                MY_CUSTOM_COLOR, 0, getCarContext().getString(R.string.custom_color_text).length()))
                        .setImage(new CarIcon.Builder(
                                IconCompat.createWithResource(
                                        getCarContext(),
                                        R.drawable.ic_gas_station))
                                .setTint(MY_CUSTOM_COLOR)
                                .build())
                        .build());

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.color_example_title))
                .setStartHeaderAction(BACK)
                .build();

        return new ListTemplate.Builder()
                .setSingleList(listBuilder.build())
                .setHeader(header)
                .build();
    }
}
