package com.example.mycarapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.car.app.connection.CarConnection;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.common.utils.LiveDataManager;


public class MainActivity extends AppCompatActivity {

    private TextView connectionStatusTextView;
    private TextView R_valueTextView;
    private TextView G_valueTextView;
    private TextView B_valueTextView;
    private TextView colorButtonTextView;
    private int r = 0;
    private int g = 0;
    private int b = 0;
    private final int alpha = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        connectionStatusTextView = findViewById(R.id.connection_status);
        R_valueTextView = findViewById(R.id.R_value);
        G_valueTextView = findViewById(R.id.G_value);
        B_valueTextView = findViewById(R.id.B_value);
        colorButtonTextView = findViewById(R.id.color_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CarConnection carConnection = new CarConnection(this);
        carConnection.getType().observe(this, connectionState -> {
            String message;
            switch (connectionState) {
                case CarConnection.CONNECTION_TYPE_NOT_CONNECTED:
                    message = "Not connected to a head unit";
                    break;
                case CarConnection.CONNECTION_TYPE_NATIVE:
                    message = "Connected to Android Automotive OS";
                    break;
                case CarConnection.CONNECTION_TYPE_PROJECTION:
                    message = "Connected to Android Auto";
                    break;
                default:
                    message = "Unknown car connection type";
                    break;
            }

            connectionStatusTextView.setText(message);
        });


        LiveDataManager.getInstance().getR_value().observe(this, value -> {
            String text = getString(R.string.r_value) + " " + value;
            R_valueTextView.setText(text);
            r = value;
            colorButtonTextView.setBackgroundColor((alpha << 24) | (r << 16) | (g << 8) | b);
        });

        LiveDataManager.getInstance().getG_value().observe(this, value -> {
            String text = getString(R.string.g_value) + " " + value;
            G_valueTextView.setText(text);
            g = value;
            colorButtonTextView.setBackgroundColor((alpha << 24) | (r << 16) | (g << 8) | b);
        });

        LiveDataManager.getInstance().getB_value().observe(this, value -> {
            String text = getString(R.string.b_value) + " " + value;
            B_valueTextView.setText(text);
            b = value;
            colorButtonTextView.setBackgroundColor((alpha << 24) | (r << 16) | (g << 8) | b);
        });
    }
}
