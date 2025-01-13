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
import androidx.car.app.model.GridItem;
import androidx.car.app.model.GridTemplate;
import androidx.car.app.model.Header;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;
import com.example.common.R;

public class GridTemplateExampleScreen extends Screen {

    public GridTemplateExampleScreen(CarContext carContext) {
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

        GridItem appIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon))
                .setText(getCarContext().getString(R.string.app_icon))
                .setImage(CarIcon.APP_ICON, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.icom_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        Resources resources = getCarContext().getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.simple_car);
        IconCompat image = IconCompat.createWithBitmap(bitmap);

        GridItem imageItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.image))
                .setText(getCarContext().getString(R.string.image))
                .setImage(new CarIcon.Builder(image).build(), GridItem.IMAGE_TYPE_LARGE)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.image_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        GridItem loadingItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.element))
                .setText(getCarContext().getString(R.string.element_loading))
                .setLoading(true)
                .build();

        ItemList gridItemList = new ItemList.Builder()
                .addItem(appIconItem)
                .addItem(imageItem)
                .addItem(loadingItem)
                .build();

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.grid_template_screen))
                .setStartHeaderAction(Action.BACK)
                .addEndHeaderAction(settingsActionBuilder.build())
                .addEndHeaderAction(iconActionBuilder.build())
                .build();

        return new GridTemplate.Builder()
                .setHeader(header)
                .setSingleList(gridItemList)
                .setLoading(false)
                .build();
    }
}
