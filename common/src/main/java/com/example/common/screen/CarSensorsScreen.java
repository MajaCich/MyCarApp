package com.example.common.screen;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.hardware.CarHardwareManager;
import androidx.car.app.hardware.common.CarValue;
import androidx.car.app.hardware.common.OnCarDataAvailableListener;
import androidx.car.app.hardware.info.Accelerometer;
import androidx.car.app.hardware.info.CarInfo;
import androidx.car.app.hardware.info.CarSensors;
import androidx.car.app.hardware.info.EnergyLevel;
import androidx.car.app.hardware.info.Gyroscope;
import androidx.car.app.hardware.info.Mileage;
import androidx.car.app.hardware.info.Speed;
import androidx.car.app.hardware.info.Compass;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class CarSensorsScreen extends Screen {
    final Executor carHardwareExecutor;
    Float battery = null;
    Float fuel = null;
    Integer distanceDisplayUnit = null;
    Float range = null;
    Boolean isEnergyLow = null;
    private boolean hasEnergyLevelPermission;
    OnCarDataAvailableListener<EnergyLevel> energyLevelListener = (data) -> {
        if (data.getBatteryPercent().getStatus() == CarValue.STATUS_SUCCESS) {
            battery = data.getBatteryPercent().getValue();
            Log.d("CarSensorsScreen", "Baterry: " + battery);
        } else {
            Log.e("CarSensorsScreen", "Error retrieving battery data");
        }
        if (data.getFuelPercent().getStatus() == CarValue.STATUS_SUCCESS) {
            fuel = data.getFuelPercent().getValue();
            Log.d("CarSensorsScreen", "Fuel: " + fuel);
        }
        else {
            Log.e("CarSensorsScreen", "Error retrieving fuel data");

        }
        if (data.getDistanceDisplayUnit().getStatus() == CarValue.STATUS_SUCCESS) {
            distanceDisplayUnit = data.getDistanceDisplayUnit().getValue();
            Log.d("CarSensorsScreen", "Distance Display Unit: " + distanceDisplayUnit);
        }
        else {
            Log.e("CarSensorsScreen", "Error retrieving distance display unit data");
        }
        if (data.getRangeRemainingMeters().getStatus() == CarValue.STATUS_SUCCESS) {
            range = data.getRangeRemainingMeters().getValue();
            Log.d("CarSensorsScreen", "Range: " + range);
        }
        else {
            Log.e("CarSensorsScreen", "Error retrieving range data");
        }
        if (data.getEnergyIsLow().getStatus()== CarValue.STATUS_SUCCESS) {
            isEnergyLow = data.getEnergyIsLow().getValue();
            Log.d("CarSensorsScreen", "Is Energy Low: " + isEnergyLow);
        }
        else {
            Log.e("CarSensorsScreen", "Error retrieving energy is low data");
        }
        invalidate();
    };
    Float speed = null;
    private boolean hasSpeedPermission;
    OnCarDataAvailableListener<Speed> speedListener = (data) -> {
        if (data.getDisplaySpeedMetersPerSecond().getStatus() == CarValue.STATUS_SUCCESS) {
            speed = data.getDisplaySpeedMetersPerSecond().getValue();
            Log.d("CarSensorsScreen", "Speed: " + speed);
        } else {
            Log.e("CarSensorsScreen", "Error retrieving speed data");
        }
        invalidate();
    };
    private Float odometer = null;
    private Integer odometerDistanceUnit = null;
    private boolean hasMileagePermission;
    OnCarDataAvailableListener<Mileage> mileageListener = (data) -> {
        if (data.getOdometerMeters().getStatus() == CarValue.STATUS_SUCCESS) {
            odometer = data.getOdometerMeters().getValue();
            Log.d("CarSensorsScreen", "Odometer: " + odometer);
        }
        else {
            Log.e("CarSensorsScreen", "Error retrieving odometer data");
        }
        if (data.getDistanceDisplayUnit().getStatus() == CarValue.STATUS_SUCCESS) {
            odometerDistanceUnit = data.getDistanceDisplayUnit().getValue();
            Log.d("CarSensorsScreen", "Odometer Distance Unit: " + odometerDistanceUnit);
        }
        else {
            Log.e("CarSensorsScreen", "Error retrieving odometer distance unit data");
        }
        invalidate();
    };
    private boolean hasCompassPermission;
    private List<Float> orientations = new ArrayList<>();
    OnCarDataAvailableListener<Compass> compassListener = (data) -> {
        if (data.getOrientations().getStatus() == CarValue.STATUS_SUCCESS) {
            orientations = data.getOrientations().getValue();
            Log.d("CarSensorsScreen", "Orientations: " + orientations);
        } else {
            Log.e("CarSensorsScreen", "Error retrieving compass data");
        }
        invalidate();
    };
    private List<Float> forces = new ArrayList<>();
    private boolean hasAccelerometerPermission;
    OnCarDataAvailableListener<Accelerometer> accelerometerListener = (data) -> {
        if (data.getForces().getStatus() == CarValue.STATUS_SUCCESS) {
            forces = data.getForces().getValue();
            Log.d("CarSensorsScreen", "Forces: " + forces);
        } else {
            Log.e("CarSensorsScreen", "Error retrieving accelerometer data");
        }
        invalidate();
    };
    private List<Float> rotations = new ArrayList<>();
    private boolean hasGyroscopePermission;
    OnCarDataAvailableListener<Gyroscope> gyroscopeListener = (data) -> {
        if (data.getRotations().getStatus() == CarValue.STATUS_SUCCESS) {
            rotations = data.getRotations().getValue();
            Log.d("CarSensorsScreen", "Rotations: " + rotations);
        } else {
            Log.e("CarSensorsScreen", "Error retrieving gyroscope data");
        }
        invalidate();
    };

    public CarSensorsScreen(CarContext carContext) {
        super(carContext);

        carHardwareExecutor = getCarContext().getMainExecutor();
        Lifecycle lifecycle = getLifecycle();

        lifecycle.addObserver(new DefaultLifecycleObserver()  {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {

                CarHardwareManager carHardwareManager =
                        getCarContext().getCarService(CarHardwareManager.class);

                CarInfo carInfo = carHardwareManager.getCarInfo();
                CarSensors carSensors = carHardwareManager.getCarSensors();

                try {
                    carInfo.addEnergyLevelListener(carHardwareExecutor, energyLevelListener);
                    hasEnergyLevelPermission = true;
                } catch (SecurityException e) {
                    hasEnergyLevelPermission = false;
                    Log.e("CarSensorsScreen", "No permission to energy level data");
                }

                try {
                    carInfo.addSpeedListener(carHardwareExecutor, speedListener);
                    hasSpeedPermission = true;
                } catch (SecurityException e) {
                    hasSpeedPermission = false;
                    Log.e("CarSensorsScreen", "No permission to speed data");
                }

                try {
                    carInfo.addMileageListener(carHardwareExecutor, mileageListener);
                    hasMileagePermission = true;
                } catch (SecurityException e) {
                    hasMileagePermission = false;
                    Log.e("CarSensorsScreen", "No permission to odometer/mileage data");
                }

                try {
                    carSensors.addCompassListener(CarSensors.UPDATE_RATE_FASTEST, carHardwareExecutor, compassListener);
                    hasCompassPermission = true;
                } catch (SecurityException e) {
                    hasCompassPermission = false;
                    Log.e("CarSensorsScreen", "No permission to compass data");
                }

                try {
                    carSensors.addAccelerometerListener(CarSensors.UPDATE_RATE_NORMAL, carHardwareExecutor, accelerometerListener);
                    hasAccelerometerPermission = true;
                } catch (SecurityException e) {
                    hasAccelerometerPermission = false;
                    Log.e("CarSensorsScreen", "No permission to accelerometer data");
                }

                try {
                    carSensors.addGyroscopeListener(CarSensors.UPDATE_RATE_NORMAL, carHardwareExecutor, gyroscopeListener);
                    hasGyroscopePermission = true;
                } catch (SecurityException e) {
                    hasGyroscopePermission = false;
                    Log.e("CarSensorsScreen", "No permission to gyroscope data");
                }
            }
        });

        lifecycle.addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                CarHardwareManager carHardwareManager =
                        getCarContext().getCarService(CarHardwareManager.class);

                CarInfo carInfo = carHardwareManager.getCarInfo();
                carInfo.removeEnergyLevelListener(energyLevelListener);
                carInfo.removeSpeedListener(speedListener);
                carInfo.removeMileageListener(mileageListener);

                CarSensors carSensors = carHardwareManager.getCarSensors();
                carSensors.removeCompassListener(compassListener);
                carSensors.removeAccelerometerListener(accelerometerListener);
                carSensors.removeGyroscopeListener(gyroscopeListener);
            }
        });
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        StringBuilder energyInfo = new StringBuilder();
        Row.Builder energyRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.energy_level_info));

        if(!hasEnergyLevelPermission)
            energyRowBuilder.addText(getCarContext().getString(R.string.no_permission));

        if(fuel!=null)
        {
            energyInfo.append(getCarContext().getString(R.string.fuel_level)).append(fuel.toString()).append(" %");
        }
        if(battery!=null)
        {
            energyInfo.append(getCarContext().getString(R.string.battery_level)).append(battery.toString()).append(" %");
        }
        if(distanceDisplayUnit!=null)
        {
            energyInfo.append(getCarContext().getString(R.string.distance_display_unit)).append(distanceDisplayUnit.toString());
        }
        if(range!=null)
        {
            energyInfo.append(getCarContext().getString(R.string.range)).append(range.toString()).append(" m");
        }
        if(isEnergyLow!=null)
        {
            energyInfo.append(getCarContext().getString(R.string.is_energy_low)).append(isEnergyLow.toString());
        }
        energyRowBuilder.addText(energyInfo.toString());

        Row.Builder speedRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.speed_title));

        if(!hasSpeedPermission)
            speedRowBuilder.addText(getCarContext().getString(R.string.no_permission));

        if(speed!=null)
        {
            float speedKm = speed * 3.6f;
            speedRowBuilder.addText(speed +" m/s");
            speedRowBuilder.addText(speedKm +" km/h");
        }

        Row.Builder compassRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.compass_title));

        if(!hasCompassPermission)
            compassRowBuilder.addText(getCarContext().getString(R.string.no_permission));

        if(!orientations.isEmpty())
        {
            StringBuilder compassInfo = new StringBuilder();
            String azimuth = orientations.get(0).toString();
            String pitch = orientations.get(1).toString();
            String roll = orientations.get(2).toString();
            compassInfo.append(getCarContext().getString(R.string.azimuth)).append(azimuth);
            compassInfo.append(getCarContext().getString(R.string.pitch)).append(pitch);
            compassInfo.append(getCarContext().getString(R.string.roll)).append(roll);
            compassRowBuilder.addText(compassInfo.toString());
        }

        Row.Builder accelerometerRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.accelerometer_title));

        if(!hasAccelerometerPermission)
            accelerometerRowBuilder.addText(getCarContext().getString(R.string.no_permission));

        if(!forces.isEmpty())
        {
            StringBuilder accelerometerInfo = new StringBuilder();
            String x = forces.get(0).toString();
            String y = forces.get(1).toString();
            String z = forces.get(2).toString();
            accelerometerInfo.append(getCarContext().getString(R.string.x)).append(x);
            accelerometerInfo.append(getCarContext().getString(R.string.y)).append(y);
            accelerometerInfo.append(getCarContext().getString(R.string.z)).append(z);
            accelerometerRowBuilder.addText(accelerometerInfo.toString());
        }
        Row.Builder gyroscopeRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.gyroscope_title));

        if(!hasGyroscopePermission)
            gyroscopeRowBuilder.addText(getCarContext().getString(R.string.no_permission));

        if(!rotations.isEmpty())
        {
            StringBuilder gyroscopeInfo = new StringBuilder();
            String x = rotations.get(0).toString();
            String y = rotations.get(1).toString();
            String z = rotations.get(2).toString();
            gyroscopeInfo.append(getCarContext().getString(R.string.x)).append(x);
            gyroscopeInfo.append(getCarContext().getString(R.string.y)).append(y);
            gyroscopeInfo.append(getCarContext().getString(R.string.z)).append(z);
            gyroscopeRowBuilder.addText(gyroscopeInfo.toString());
        }

        Row.Builder mileageRowBuilder = new Row.Builder()
                .setTitle(getCarContext().getString(R.string.mileage_title));

        if(!hasMileagePermission)
            mileageRowBuilder.addText(getCarContext().getString(R.string.no_permission));

        if(odometer!=null)
        {
            String unit = "km";
            if(odometerDistanceUnit!=null) unit = odometerDistanceUnit.toString();
            mileageRowBuilder.addText(odometer.toString()+" "+unit);
        }

        Pane.Builder pane = new Pane.Builder();
        pane.addRow(energyRowBuilder.build());
        pane.addRow(speedRowBuilder.build());
        pane.addRow(compassRowBuilder.build());
        pane.addRow(accelerometerRowBuilder.build());
        pane.addRow(gyroscopeRowBuilder.build());
        pane.addRow(mileageRowBuilder.build());

        Header header = new Header.Builder()
                .setTitle(getCarContext().getString(R.string.car_info_screen))
                .setStartHeaderAction(Action.BACK)
                .build();

        return new PaneTemplate.Builder(pane.build())
                .setHeader(header)
                .build();
    }
}
