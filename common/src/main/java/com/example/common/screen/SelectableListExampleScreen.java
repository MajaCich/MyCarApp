package com.example.common.screen;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.Header;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;

public class SelectableListExampleScreen extends Screen {
    public SelectableListExampleScreen(CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        OnClickListener listener = () -> CarToast.makeText(getCarContext(),
                getCarContext().getString(R.string.action_clicked),
                CarToast.LENGTH_LONG).show();

        Action.Builder iconActionBuilder = new Action.Builder()
                .setOnClickListener(listener)
                .setIcon(CarIcon.ERROR);

        Action settingsActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.settings))
                .setOnClickListener(listener)
                .build();

        ItemList.OnSelectedListener onSelectedListener = selectedIndex -> CarToast.makeText(
                getCarContext(),
                getCarContext().getString(R.string.selected_element_text) + " " + selectedIndex,
                CarToast.LENGTH_LONG
        ).show();

        ItemList.Builder listBuilder = new ItemList.Builder()
                .setNoItemsMessage(getCarContext().getString(R.string.no_elements))
                .setSelectedIndex(2)
                .setOnSelectedListener(onSelectedListener);

        Row simpleRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.simple_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .build();

        listBuilder.addItem(simpleRow);

        Row decorationRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.decoration_row))
                .setNumericDecoration(9)
                .addText(getCarContext().getString(R.string.additional_text))
                .build();

        listBuilder.addItem(decorationRow);

        Resources resources = getCarContext().getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.simple_car);
        IconCompat image = IconCompat.createWithBitmap(bitmap);

        Row imageRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.big_image_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setImage(new CarIcon.Builder(image).build(), Row.IMAGE_TYPE_LARGE)
                .build();

        listBuilder.addItem(imageRow);

        Row smallImageRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.small_image_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setImage(new CarIcon.Builder(image).build(), Row.IMAGE_TYPE_SMALL)
                .build();

        listBuilder.addItem(smallImageRow);

        Row disabledRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.inactive_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setEnabled(false)
                .build();

        listBuilder.addItem(disabledRow);

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.selectable_list_screen_title))
                .setStartHeaderAction(Action.BACK)
                .addEndHeaderAction(settingsActionBuilder)
                .addEndHeaderAction(iconActionBuilder.build())
                .build();

        return new ListTemplate.Builder()
                .setSingleList(listBuilder.build())
                .setHeader(header)
                .build();
    }
}
