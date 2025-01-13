package com.example.common.screen;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.GridItem;
import androidx.car.app.model.GridTemplate;
import androidx.car.app.model.Header;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.Template;

import com.example.common.R;

public class IconExampleScreen extends Screen {
    public IconExampleScreen(CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        ItemList.Builder gridItemList = new ItemList.Builder();

        GridItem alertIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon_alert))
                .setImage(CarIcon.ALERT, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.icom_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        GridItem backIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon_back))
                .setImage(CarIcon.BACK, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.icom_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        GridItem errorIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon_error))
                .setImage(CarIcon.ERROR, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.icom_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        GridItem appIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon_app))
                .setImage(CarIcon.APP_ICON, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.icom_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        GridItem panIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon_pan))
                .setImage(CarIcon.PAN, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.icom_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        GridItem composeIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon_compose_message))
                .setImage(CarIcon.COMPOSE_MESSAGE, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.icom_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        gridItemList.addItem(alertIconItem);
        gridItemList.addItem(backIconItem);
        gridItemList.addItem(errorIconItem);
        gridItemList.addItem(appIconItem);
        gridItemList.addItem(panIconItem);
        gridItemList.addItem(composeIconItem);

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.car_icon_screen_title))
                .setStartHeaderAction(Action.BACK)
                .build();

        return new GridTemplate.Builder()
                .setHeader(header)
                .setSingleList(gridItemList.build())
                .setLoading(false)
                .build();
    }
}
