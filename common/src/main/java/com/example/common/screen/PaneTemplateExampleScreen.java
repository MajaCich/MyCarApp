package com.example.common.screen;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.Header;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.car.app.model.Toggle;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;

public class PaneTemplateExampleScreen extends Screen {

    public PaneTemplateExampleScreen(@NonNull CarContext carContext) {
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

        Action.Builder settingsActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.settings))
                .setOnClickListener(listener);

        Action.Builder infoActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.information))
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(listener);

        Action.Builder moreActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.additional))
                .setBackgroundColor(CarColor.GREEN)
                .setOnClickListener(listener);

        Row simpleRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.simple_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .addText(getCarContext().getString(R.string.more_text))
                .build();

        Row decorationRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.decoration_row))
                .setNumericDecoration(9)
                .addText(getCarContext().getString(R.string.additional_text))
                .build();

        Row enabledRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.inactive_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setEnabled(false)
                .build();

        Toggle toggle = new Toggle.Builder(
                (value) -> CarToast.makeText(getCarContext(), getCarContext().getString(R.string.toggle_clicked),
                        CarToast.LENGTH_LONG).show())
                .setChecked(false)
                .build();

        Row toggleRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.toggle_row))
                .setToggle(toggle)
                .build();

        CarIcon iconFromImage = new CarIcon.Builder(
                IconCompat.createWithResource(getCarContext(), R.drawable.simple_car)
        ).build();

        Row imageRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.big_image_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setImage(iconFromImage, Row.IMAGE_TYPE_LARGE)
                .build();

        Row imageRow2 = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.small_image_row))
                .addText(getCarContext().getString(R.string.additional_text))
                .setImage(iconFromImage, Row.IMAGE_TYPE_SMALL)
                .build();

        Pane pane = new Pane.Builder()
                .addAction(infoActionBuilder.build())
                .addAction(moreActionBuilder.build())
                .setImage(iconFromImage)
                .addRow(simpleRow)
                .addRow(imageRow)
                .addRow(imageRow2)
                .addRow(enabledRow)
                .addRow(decorationRow)
                .addRow(toggleRow)
                .build();

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.pane_template_screen_title))
                .setStartHeaderAction(Action.BACK)
                .addEndHeaderAction(settingsActionBuilder.build())
                .addEndHeaderAction(iconActionBuilder.build())
                .build();

        return new PaneTemplate.Builder(pane)
                .setHeader(header)
                .build();
    }
}
