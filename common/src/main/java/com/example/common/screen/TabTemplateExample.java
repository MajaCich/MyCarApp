package com.example.common.screen;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.GridItem;
import androidx.car.app.model.GridTemplate;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Tab;
import androidx.car.app.model.TabContents;
import androidx.car.app.model.TabTemplate;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;

import com.example.common.R;

import java.util.HashMap;
import java.util.Map;

public class TabTemplateExample extends Screen {
    private String activeTabId = "1";
    private final Map<String, TabContents> tabContentsMap = new HashMap<>();

    public TabTemplateExample(@NonNull CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        CarIcon androidCarIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.android))
                .build();

        Tab tab1 = new Tab.Builder()
                .setContentId("1")
                .setTitle("Tab 1")
                .setIcon(androidCarIcon)
                .build();

        CarIcon androidStudioCarIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.android_studio))
                .build();

        Tab tab2 = new Tab.Builder()
                .setContentId("2")
                .setTitle("Tab 2")
                .setIcon(androidStudioCarIcon)
                .build();

        CarIcon accountCarIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.account))
                .build();

        Tab tab3 = new Tab.Builder()
                .setContentId("3")
                .setTitle("Tab 3")
                .setIcon(accountCarIcon)
                .build();

        CarIcon cloudCarIcon = new CarIcon.Builder(
                IconCompat.createWithResource(
                        getCarContext(),
                        R.drawable.apple_icloud))
                .build();

        Tab tab4 = new Tab.Builder()
                .setContentId("4")
                .setTitle("Tab 4")
                .setIcon(cloudCarIcon)
                .build();

        Row simpleRow = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.first_tab_content_text))
                .addText(getCarContext().getString(R.string.pane_template))
                .build();

        PaneTemplate paneTemplate = new PaneTemplate.Builder(new Pane.Builder().addRow(simpleRow).build())
                .build();

        TabContents contents1 = new TabContents.Builder(paneTemplate).build();

        tabContentsMap.put("1", contents1);

        MessageTemplate messageTemplate = new MessageTemplate.Builder(getCarContext().getString(R.string.second_tab_content_text))
                .setIcon(CarIcon.APP_ICON)
                .addAction(new Action.Builder().setTitle(getCarContext().getString(R.string.information)).build())
                .build();

        TabContents contents2 = new TabContents.Builder(messageTemplate)
                .build();

        tabContentsMap.put("2", contents2);

        IconCompat image = IconCompat.createWithResource(getCarContext(), R.drawable.simple_car);

        GridItem imageItem = new GridItem.Builder()
                .setTitle(getCarContext().getString(R.string.image))
                .setText(getCarContext().getString(R.string.image))
                .setImage(new CarIcon.Builder(image).build(), GridItem.IMAGE_TYPE_LARGE)
                .setLoading(false)
                .setOnClickListener(() -> CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.image_clicked),
                        CarToast.LENGTH_LONG).show())
                .build();

        ItemList gridItemList = new ItemList.Builder()
                .addItem(imageItem)
                .build();

        GridTemplate gridTemplate = new GridTemplate.Builder()
                .setSingleList(gridItemList)
                .setLoading(false)
                .build();

        TabContents contents3 = new TabContents.Builder(gridTemplate).build();
        tabContentsMap.put("3", contents3);

        MessageTemplate messageTemplateTab4 = new MessageTemplate.Builder(getCarContext().getString(R.string.fourth_tab_content_text))
                .setIcon(CarIcon.APP_ICON)
                .addAction(new Action.Builder().setTitle(getCarContext().getString(R.string.information)).build())
                .build();

        TabContents contents4 = new TabContents.Builder(messageTemplateTab4)
                .build();

        tabContentsMap.put("4", contents4);

        TabTemplate.TabCallback tabCallback = new TabTemplate.TabCallback() {
            @Override
            public void onTabSelected(@NonNull String tabId) {
                if (tabContentsMap.containsKey(tabId)) {
                    activeTabId = tabId;
                    invalidate();
                } else {
                    CarToast.makeText(getCarContext(), getCarContext().getString(R.string.unknown_tab) + " " + tabId, CarToast.LENGTH_SHORT).show();
                }
            }
        };

        return new TabTemplate.Builder(tabCallback)
                .setHeaderAction(Action.APP_ICON)
                .setActiveTabContentId(activeTabId)
                .addTab(tab1)
                .addTab(tab2)
                .addTab(tab3)
                .addTab(tab4)
                .setTabContents(tabContentsMap.get(activeTabId))
                .build();
    }
}
