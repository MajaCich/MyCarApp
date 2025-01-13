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

public class SelectableGridExampleScreen extends Screen {

    public SelectableGridExampleScreen(CarContext carContext) {
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

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.selectable_grid_screen_title))
                .setStartHeaderAction(Action.BACK)
                .addEndHeaderAction(settingsActionBuilder.build())
                .addEndHeaderAction(iconActionBuilder.build())
                .build();

        GridItem appIconItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon))
                .setText(getCarContext().getString(R.string.app_icon))
                .setImage(CarIcon.APP_ICON, GridItem.IMAGE_TYPE_ICON)
                .setLoading(false)
                .build();

        Resources resources = getCarContext().getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.simple_car);
        IconCompat image = IconCompat.createWithBitmap(bitmap);

        GridItem imageItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.icon))
                .setText(getCarContext().getString(R.string.app_icon))
                .setImage(new CarIcon.Builder(image).build(), GridItem.IMAGE_TYPE_LARGE)
                .setLoading(false)
                .build();

        ItemList.OnSelectedListener onSelectedListener = selectedIndex -> CarToast.makeText(
                getCarContext(),
                getCarContext().getString(R.string.selected_element_text)+" " + selectedIndex,
                CarToast.LENGTH_LONG
        ).show();

        ItemList gridItemList = new ItemList.Builder()
                .addItem(appIconItem)
                .addItem(imageItem)
                .setOnSelectedListener(onSelectedListener)
                .setSelectedIndex(1)
                .build();

        return new GridTemplate.Builder()
                .setHeader(header)
                .setSingleList(gridItemList)
                .setLoading(false)
                .build();
    }
}
