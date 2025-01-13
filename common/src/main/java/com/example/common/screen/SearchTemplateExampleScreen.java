package com.example.common.screen;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.Item;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.Row;
import androidx.car.app.model.SearchTemplate;
import androidx.car.app.model.Template;

import com.example.common.R;

import java.util.List;

public class SearchTemplateExampleScreen extends Screen {
    private final ItemList.Builder allItemsList;
    private ItemList.Builder filteredItemsList;

    public SearchTemplateExampleScreen(@NonNull CarContext carContext) {
        super(carContext);

        allItemsList = new ItemList.Builder();
        for (int i = 1; i <= 10; ++i) {
            allItemsList.addItem(
                    new Row.Builder()
                            .setTitle(getCarContext().getString(R.string.row_number) + i)
                            .addText(getCarContext().getString(R.string.additional_text))
                            .build());
        }

        allItemsList.addItem(new Row.Builder()
                .setTitle(getCarContext().getString(R.string.row_to_search))
                .addText(getCarContext().getString(R.string.special_row))
                .build());

        filteredItemsList = new ItemList.Builder()
                .setNoItemsMessage(getCarContext().getString(R.string.no_elements));
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        SearchTemplate.SearchCallback searchCallback =
                new SearchTemplate.SearchCallback() {
                    @Override
                    public void onSearchTextChanged(@NonNull String searchText) {
                        //Metoda wywoływana gdy, obecna fraza wyszukiwania się zmieni
                        //Częstotliwość zależy od systemu samochodwego/Android Auto i nie musi być
                        //wywoływana po każdym klawiszu, może być wywołana dopiero po kilku znakach
                    }

                    @Override
                    public void onSearchSubmitted(@NonNull String searchText) {
                        //Metoda wywoływana gdy, użytkownik zatwierdzi wyszukiwanie
                        //String w argumencie jest ostateczną frazą wysszukiwaia
                        filteredItemsList = new ItemList.Builder()
                                .setNoItemsMessage(getCarContext().getString(R.string.no_elements));
                        List<Item> allItems = allItemsList.build().getItems();
                        for (Item item : allItems) {
                            if (item instanceof Row) {
                                Row row = (Row) item;
                                if (row.getTitle().toString().contains(searchText.toLowerCase())) {
                                    filteredItemsList.addItem(row);
                                    invalidate();
                                }
                            }
                        }
                    }
                };

        return new SearchTemplate.Builder(searchCallback)
                .setInitialSearchText(getCarContext().getString(R.string.search))
                .setSearchHint(getCarContext().getString(R.string.hint))
                .setShowKeyboardByDefault(true)
                .setItemList(filteredItemsList.build())
                .setLoading(false)
                .setHeaderAction(Action.BACK)
                .build();
    }
}
