package com.example.common.screen;

import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.ActionStrip;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.ClickableSpan;
import androidx.car.app.model.InputCallback;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.ParkedOnlyOnClickListener;
import androidx.car.app.model.Template;
import androidx.car.app.model.signin.InputSignInMethod;
import androidx.car.app.model.signin.PinSignInMethod;
import androidx.car.app.model.signin.ProviderSignInMethod;
import androidx.car.app.model.signin.QRCodeSignInMethod;
import androidx.car.app.model.signin.SignInTemplate;

import com.example.common.R;

public class SignInExampleScreen extends Screen {
    public SignInExampleScreen(CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {

        OnClickListener actionListener = ParkedOnlyOnClickListener.create(() -> CarToast.makeText(getCarContext(),
                getCarContext().getString(R.string.action_clicked),
                CarToast.LENGTH_LONG).show());

        OnClickListener spanListener = () -> CarToast.makeText(getCarContext(),
                getCarContext().getString(R.string.clickable_span_clicked),
                CarToast.LENGTH_LONG).show();

        Action.Builder moreActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.more))
                .setBackgroundColor(CarColor.YELLOW)
                .setOnClickListener(actionListener);

        Action.Builder settingsActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.settings))
                .setOnClickListener(actionListener);

        Action.Builder infoActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.information))
                .setOnClickListener(actionListener);

        SpannableString clickableString = new SpannableString(getCarContext().getString(R.string.terms_of_service_clickable_span));
        clickableString.setSpan(ClickableSpan.create(spanListener), 0, clickableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        InputCallback inputCallback = new InputCallback() {
            @Override
            public void onInputSubmitted(@NonNull String text) { //text - tekst wprowadzony przez użytkownika - pusty "" jak nic nie wpisane
                //Powiadamia, gdy użytkownik zakończy wprowadzanie
                //tekstu w polu tekstowym i zatwierdzi je klawiszem Enter
                //Wywoływane w odpowiedzi na zakończenie wpisywania i naciśnięcie klawisza Enter.

            }

            @Override
            public void onInputTextChanged(@NonNull String text) {
                // Metoda powiadamia, że zawartość pola tekstowego została zmieniona przez użytkownika.
                //ywoływana jest w trakcie wpisywania tekstu przez użytkownika, jednak częstotliwość
                // wywołań nie jest gwarantowana po każdym pojedynczym naciśnięciu klawisza.
                // Host może zdecydować się na wysyłanie aktualizacji po kilku znakach zamiast po
                // każdym wpisanym znaku.
            }
        };

        InputSignInMethod inputSignInMethod = new InputSignInMethod.Builder(inputCallback)
                .setDefaultValue(getCarContext().getString(R.string.name))
                //.setErrorMessage("Błąd [opis błędu]")
                .setHint(getCarContext().getString(R.string.hint_name))
                .setInputType(InputSignInMethod.INPUT_TYPE_DEFAULT)
                .setKeyboardType(InputSignInMethod.KEYBOARD_DEFAULT)
                .setShowKeyboardByDefault(false)
                .build();

        PinSignInMethod pinSignInMethod = new PinSignInMethod("kod PIN");

        QRCodeSignInMethod qrSignInMethod = new QRCodeSignInMethod(Uri.parse("https://developer.android.com/reference/androidx/car/app/model/signin/QRCodeSignInMethod"));

        Action.Builder googleLogInActionBuilder = new Action.Builder()
                .setTitle(getCarContext().getString(R.string.google_signin))
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(actionListener);

        ProviderSignInMethod providerSignInMethod = new ProviderSignInMethod(googleLogInActionBuilder.build());

        return new SignInTemplate.Builder(inputSignInMethod)
                .setTitle(getCarContext().getString(R.string.signin_template_screen_title))
                .setHeaderAction(Action.BACK)
                .setActionStrip(new ActionStrip.Builder().addAction(settingsActionBuilder.build()).build())
                .addAction(moreActionBuilder.build())
                .addAction(infoActionBuilder.build())
                .setAdditionalText(clickableString)
                .setInstructions(getCarContext().getString(R.string.instructions))
                .setLoading(false)
                .build();
    }
}
