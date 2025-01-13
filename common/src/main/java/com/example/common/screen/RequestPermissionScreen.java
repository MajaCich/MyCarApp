package com.example.common.screen;

import static android.content.pm.PackageManager.FEATURE_AUTOMOTIVE;
import static androidx.car.app.model.Action.BACK;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.car.app.CarAppPermission;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.LongMessageTemplate;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.ParkedOnlyOnClickListener;
import androidx.car.app.model.Template;

import com.example.common.R;

import java.util.ArrayList;
import java.util.List;

public class RequestPermissionScreen extends Screen {
    public RequestPermissionScreen(CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        Action refreshAction = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.refresh_action_title))
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(this::invalidate)
                .build();

        List<String> permissions = new ArrayList<>();
        String[] declaredPermissions;

        try {
            PackageInfo info =
                    getCarContext().getPackageManager().getPackageInfo(
                            getCarContext().getPackageName(),
                            PackageManager.GET_PERMISSIONS);
            declaredPermissions = info.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            return new MessageTemplate.Builder(
                    getCarContext().getString(R.string.package_not_found_error_msg))
                    .setHeaderAction(Action.BACK)
                    .addAction(refreshAction)
                    .build();
        }

        if (declaredPermissions != null) {
            for (String declaredPermission : declaredPermissions) {
                // Ignoruje uprawienia zwiąane z car app host, normalne oznacza, że stanowią małe ryzyko dla prywatności
                // są to permisje związane z car app library
                if (declaredPermission.startsWith("androidx.car.app")) {
                    continue;
                }
                try {
                    CarAppPermission.checkHasPermission(getCarContext(), declaredPermission);
                } catch (SecurityException e) {
                    permissions.add(declaredPermission);
                }
            }
        }

        if (permissions.isEmpty()) {
            return new MessageTemplate.Builder(
                    getCarContext().getString(R.string.permissions_granted_msg))
                    .setHeaderAction(Action.BACK)
                    .setTitle(getCarContext().getString(R.string.permission_screen_title))
                    .addAction(new Action.Builder()
                            .setTitle(getCarContext().getString(R.string.close_action_title))
                            .setOnClickListener(this::finish)
                            .build())
                    .build();
        }

        //Tworzenie wiadomości, która zawiera wszystkie permisje, które potrzebują zgody
        StringBuilder message = new StringBuilder()
                .append(getCarContext().getString(R.string.needs_access_msg_prefix));
        for (String permission : permissions) {
            message.append("\n");
            message.append(permission);
        }

        //tworzony jest listner
        OnClickListener listener = ParkedOnlyOnClickListener.create(() -> {
            //jest to callback - funkcja zwrotna - approved to lista uprawnień zaakceptowanych,
            //a rejected lista uprawnień odrzuconych przez użytwkonika - zdefiniowana jako wyrażenie lambda
            getCarContext().requestPermissions(
                    permissions,
                    (approved, rejected) -> {
                        CarToast.makeText(
                                getCarContext(),
                                String.format("Approved: %s Rejected: %s", approved, rejected),
                                CarToast.LENGTH_LONG).show();
                    });
            if (!getCarContext().getPackageManager().hasSystemFeature(FEATURE_AUTOMOTIVE)) { //bo w AAOS można przyznać na ekranie samochodu uprawnienia
                CarToast.makeText(getCarContext(),
                        getCarContext().getString(R.string.phone_screen_permission_msg),
                        CarToast.LENGTH_LONG).show();
            }
        });

        Action action = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.grant_access_action_title))
                .setBackgroundColor(CarColor.PRIMARY)
                .setOnClickListener(listener)
                .build();

        return new LongMessageTemplate.Builder(message)
                .setTitle(getCarContext().getString(R.string.permission_screen_title))
                .setHeaderAction(BACK)
                .addAction(action)
                .build();
    }
}
