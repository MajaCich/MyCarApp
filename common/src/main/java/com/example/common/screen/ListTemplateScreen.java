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
import androidx.car.app.model.Toggle;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;

public class ListTemplateScreen extends Screen {
    public ListTemplateScreen(CarContext carContext)
    {
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

        ItemList.Builder listBuilder = new ItemList.Builder();

        Row simpleRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.simple_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setOnClickListener(
                        () -> CarToast.makeText(getCarContext(),
                                getCarContext().getString(R.string.row_clicked),
                                CarToast.LENGTH_LONG).show())
                .build();

        listBuilder.addItem(simpleRow);

        Toggle toggle = new Toggle.Builder(
                (value) -> CarToast.makeText(getCarContext(), getCarContext().getString(R.string.toggle_clicked),
                        CarToast.LENGTH_LONG).show())
                .setChecked(false)
                .build();

        Row toggleRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.toggle_row))
                .setToggle(toggle)
                .build();

        listBuilder.addItem(toggleRow);

        Row decorationRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.decoration_row))
                .setNumericDecoration(9)
                .addText(getCarContext().getString(R.string.additional_text))
                .setOnClickListener(
                        () -> CarToast.makeText(getCarContext(),
                                getCarContext().getString(R.string.row_clicked),
                                CarToast.LENGTH_LONG).show())
                .build();

        listBuilder.addItem(decorationRow);

        Resources resources = getCarContext().getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.simple_car);
        IconCompat image = IconCompat.createWithBitmap(bitmap);

        Row imageRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.big_image_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setImage(new CarIcon.Builder(image).build(), Row.IMAGE_TYPE_LARGE)
                .setOnClickListener(
                        () -> CarToast.makeText(getCarContext(),
                                getCarContext().getString(R.string.row_clicked),
                                CarToast.LENGTH_LONG).show())
                .build();

        listBuilder.addItem(imageRow);

        Row smallImageRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.small_image_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setImage(new CarIcon.Builder(image).build(), Row.IMAGE_TYPE_SMALL)
                .setOnClickListener(
                        () -> CarToast.makeText(getCarContext(),
                                getCarContext().getString(R.string.row_clicked),
                                CarToast.LENGTH_LONG).show())
                .build();

        listBuilder.addItem(smallImageRow);

        Row disabledRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.inactive_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setEnabled(false)
                .setOnClickListener(
                        () -> CarToast.makeText(getCarContext(),
                                getCarContext().getString(R.string.row_clicked),
                                CarToast.LENGTH_LONG).show())
                .build();

        listBuilder.addItem(disabledRow);

        Row browsabledRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.browsable_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setBrowsable(true)
                .setOnClickListener(
                        () -> CarToast.makeText(getCarContext(),
                                getCarContext().getString(R.string.row_clicked),
                                CarToast.LENGTH_LONG).show())
                .build();

        listBuilder.addItem(browsabledRow);

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.list_template_screen_title))
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

