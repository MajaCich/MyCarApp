package com.example.common.screen;

import static androidx.car.app.model.Action.BACK;
import static androidx.car.app.model.CarColor.BLUE;
import static androidx.car.app.model.CarColor.DEFAULT;
import static androidx.car.app.model.CarColor.GREEN;
import static androidx.car.app.model.CarColor.RED;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.GridItem;
import androidx.car.app.model.GridTemplate;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.utils.LiveDataManager;
import com.example.common.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomColorScreen extends Screen {

    private int R_value = 0;
    private int G_value = 0;
    private int B_value = 0;
    private int i = 1;
    private String i_text = "+10";
    public CustomColorScreen(CarContext carContext) { super(carContext); }

    @NonNull
    @Override
    public Template onGetTemplate() {

        List<CarColor> RGB_colors = new ArrayList<>(Arrays.asList(RED, GREEN, BLUE));

        ItemList.Builder gridItemList = new ItemList.Builder();

        for (CarColor color : RGB_colors) {
            int value = (color == RED) ? R_value : (color == GREEN) ? G_value : B_value;

            GridItem minusButton = new GridItem.Builder()
                    .setTitle(" ")
                    .setOnClickListener(() -> {
                        if (color == RED) {
                            R_value = Math.max(R_value - i, 0);
                            LiveDataManager.getInstance().incrementR_value(-i);
                        } else if (color == GREEN) {
                            G_value = Math.max(G_value - i, 0);
                            LiveDataManager.getInstance().incrementG_value(-i);
                        } else {
                            B_value = Math.max(B_value - i, 0);
                            LiveDataManager.getInstance().incrementB_value(-i);
                        }
                        invalidate();
                    })
                    .setImage(new CarIcon.Builder(
                            IconCompat.createWithResource(
                                    getCarContext(),
                                    R.drawable.minus_circle))
                            .setTint(DEFAULT)
                            .build())
                    .build();

            GridItem valueItem = new GridItem.Builder()
                    .setTitle("Wartość: " + value)
                    .setImage(new CarIcon.Builder(
                            IconCompat.createWithResource(
                                    getCarContext(),
                                    R.drawable.square))
                            .setTint(color)
                            .build())
                    .build();

            GridItem plusButton = new GridItem.Builder()
                    .setTitle(" ")
                    .setOnClickListener(() -> {
                        if (color == RED) {
                            R_value = Math.min(R_value + i, 255);
                            LiveDataManager.getInstance().incrementR_value(i);
                        } else if (color == GREEN) {
                            G_value = Math.min(G_value + i, 255);
                            LiveDataManager.getInstance().incrementG_value(i);
                        } else {
                            B_value = Math.min(B_value + i, 255);
                            LiveDataManager.getInstance().incrementB_value(i);
                        }
                        invalidate();
                    })
                    .setImage(new CarIcon.Builder(
                            IconCompat.createWithResource(
                                    getCarContext(),
                                    R.drawable.plus_circle))
                            .setTint(DEFAULT)
                            .build())
                    .build();

            gridItemList.addItem(minusButton);
            gridItemList.addItem(valueItem);
            gridItemList.addItem(plusButton);
        }

        CarIcon carIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.square))
                .setTint(BLUE)
                .build();


        int alpha = 255;
        int red = R_value;
        int green = G_value;
        int blue = B_value;
        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        CarColor MY_CUSTOM_COLOR = CarColor.createCustom(color, color);

        GridItem custom_color = new GridItem.Builder()
                .setTitle("Kolor wyjściowy")
                .setImage(new CarIcon.Builder(
                        IconCompat.createWithResource(
                                getCarContext(),
                                R.drawable.square))
                        .setTint(MY_CUSTOM_COLOR)
                        .build())
                .build();

        GridItem blank = new GridItem.Builder()
                .setTitle(i_text)
                .setOnClickListener(() -> {
                    if(i==1) {
                        i = 10;
                        i_text = "+1";
                        invalidate();
                    }
                    else {
                        i = 1;
                        i_text = "+10";
                        invalidate();
                    }
                })
                .setImage(new CarIcon.Builder(
                        IconCompat.createWithResource(
                                getCarContext(),
                                R.drawable.numeric_10))
                        .setTint(BLUE)
                        .build())
                .build();

        GridItem reset = new GridItem.Builder()
                .setTitle("Resetuj")
                .setOnClickListener(() -> {
                    LiveDataManager.getInstance().incrementR_value(-R_value);
                    LiveDataManager.getInstance().incrementB_value(-B_value);
                    LiveDataManager.getInstance().incrementG_value(-G_value);
                    R_value = 0;
                    G_value = 0;
                    B_value = 0;
                    invalidate();
                })
                .setImage(new CarIcon.Builder(
                        IconCompat.createWithResource(
                                getCarContext(),
                                R.drawable.restore))
                        .setTint(RED)
                        .build())
                .build();

        gridItemList.addItem(reset);
        gridItemList.addItem(custom_color);
        gridItemList.addItem(blank);

        return new GridTemplate.Builder()
                .setTitle(getCarContext().getString(R.string.custom_color_screen_title))
                .setSingleList(gridItemList.build())
                .setHeaderAction(BACK)
                .build();
    }
}

