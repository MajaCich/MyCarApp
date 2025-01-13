package com.example.common.screen;

import static com.example.common.utils.EVConnectorTypeMapper.evConnectorTypeAsString;
import static com.example.common.utils.FuelTypeMapper.fuelTypeAsString;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;

import androidx.car.app.Screen;
import androidx.car.app.hardware.CarHardwareManager;
import androidx.car.app.hardware.common.CarValue;
import androidx.car.app.hardware.common.OnCarDataAvailableListener;
import androidx.car.app.hardware.info.CarInfo;
import androidx.car.app.hardware.info.EnergyProfile;
import androidx.car.app.hardware.info.Model;
import androidx.car.app.model.Action;
import androidx.car.app.model.Header;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.common.R;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class CarInfoScreen extends Screen {
    final Executor carHardwareExecutor;
    List<Integer> fuelTypes = null;
    List<Integer> evConnectorTypes = null;
    private boolean hasEnergyProfilePermission ;
    OnCarDataAvailableListener<EnergyProfile> energyProfileListener = data -> {
        if(data.getFuelTypes().getStatus() == CarValue.STATUS_SUCCESS)
        {
            fuelTypes = data.getFuelTypes().getValue();
        }
        else {
            Log.e("CarInfoScreen", "Error retrieving fuel types data");
        }
        if(data.getEvConnectorTypes().getStatus() == CarValue.STATUS_SUCCESS)
        {
            evConnectorTypes = data.getEvConnectorTypes().getValue();
        }
        else {
            Log.e("CarInfoScreen", "Error retrieving ev connector types data");
        }
        invalidate();
    };
    String modelName = null;
    String manufacturer = null;
    Integer year = null;
    private boolean hasModelPermission = true;
    OnCarDataAvailableListener<Model> modelListener = data -> {
        if(data.getName().getStatus() == CarValue.STATUS_SUCCESS)
        {
            modelName = data.getName().getValue();
            Log.d("CarInfoScreen", "Model Name: " + modelName);
        }
        else {
            Log.e("CarInfoScreen", "Error retrieving model name data");
            hasModelPermission = false;
        }
        if(data.getManufacturer().getStatus() == CarValue.STATUS_SUCCESS)
        {
            manufacturer = data.getManufacturer().getValue();
        }
        else {
            Log.e("CarInfoScreen", "Error retrieving manufacturer data");
            hasModelPermission = false;
        }
        if(data.getYear().getStatus() == CarValue.STATUS_SUCCESS)
        {
            year = data.getYear().getValue();
        }
        else {
            Log.e("CarInfoScreen", "Error retrieving year data");
            hasModelPermission = false;
        }
        invalidate();
    };
    public CarInfoScreen(CarContext carContext) {
        super(carContext);

        carHardwareExecutor = carContext.getMainExecutor();
        Lifecycle lifecycle = getLifecycle();

        lifecycle.addObserver(new DefaultLifecycleObserver()  {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner){

                CarHardwareManager carHardwareManager =
                        getCarContext().getCarService(CarHardwareManager.class);
                CarInfo carInfo = carHardwareManager.getCarInfo();

                try {
                    carInfo.fetchModel(carHardwareExecutor, modelListener);
                } catch (SecurityException e) {
                    hasModelPermission = false;
                }

                try {
                    carInfo.fetchEnergyProfile(carHardwareExecutor, energyProfileListener);
                    hasEnergyProfilePermission = true;
                } catch (SecurityException e) {
                    hasEnergyProfilePermission = false;
                }
            }
        });
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        Pane.Builder pane = new Pane.Builder();

        Row.Builder modelRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.car_info));

        if(!hasModelPermission) {
            modelRowBuilder.addText(getCarContext().getString(R.string.no_car_info_permission));
        } else {
            StringBuilder info = new StringBuilder();

            info.append(getCarContext().getString(R.string.manufacturer_info));
            info.append(Objects.requireNonNullElseGet(manufacturer, () -> getCarContext().getString(R.string.manufacturer_unavailable)));

            info.append(getCarContext().getString(R.string.model_info));
            info.append(Objects.requireNonNullElseGet(modelName, () -> getCarContext().getString(R.string.model_unavailable)));

            info.append(getCarContext().getString(R.string.year_info));
            info.append(Objects.requireNonNullElseGet(year, ()-> getCarContext().getString(R.string.year_unavailable)));

            modelRowBuilder.addText(info);
        }

        Row.Builder energyInfoRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.energy_info));

        if(!hasEnergyProfilePermission) {
            energyInfoRowBuilder.addText(getCarContext().getString(R.string.no_energy_info_permission));
        }
        else {
            StringBuilder fuelInfo = new StringBuilder();

            fuelInfo.append(getCarContext().getString(R.string.fuel_types));
            if (fuelTypes == null || fuelTypes.isEmpty()) {
                fuelInfo.append(getCarContext().getString(R.string.unavailable));
            } else {
                for (int fuelType : fuelTypes) {
                    fuelInfo.append(fuelTypeAsString(fuelType));
                    fuelInfo.append(" ");
                }
            }
            energyInfoRowBuilder.addText(fuelInfo);

            StringBuilder evInfo = new StringBuilder();
            evInfo.append(" ");
            evInfo.append(getCarContext().getString(R.string.ev_connector_types));

            if (evConnectorTypes == null || evConnectorTypes.isEmpty()) {
                evInfo.append(getCarContext().getString(R.string.unavailable));
            } else {
                evInfo.append(getCarContext().getString(R.string.ev_connector_types));
                for (int connectorType : evConnectorTypes) {
                    evInfo.append(evConnectorTypeAsString(connectorType));
                    evInfo.append(" ");
                }
            }
            energyInfoRowBuilder.addText(evInfo);
        }

        pane.addRow(modelRowBuilder.build()).addRow(energyInfoRowBuilder.build());

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.car_info_screen))
                .setStartHeaderAction(Action.BACK)
                .build();

        return new PaneTemplate.Builder(pane.build())
                .setHeader(header)
                .build();
    }
}
