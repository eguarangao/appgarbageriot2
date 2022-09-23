package com.example.app_garbager_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app_garbager_iot.ui.Login.activity_login;
import com.example.app_garbager_iot.ui.prediction.activity_camera_predictor;

public class activity_inicio extends AppCompatActivity {
    Button predecirInicio, iniciarInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        predecirInicio = findViewById(R.id.btnPredecirSmart);
        iniciarInicio = findViewById(R.id.btnIniciarSmart);


        predecirInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_inicio.this, activity_camera_predictor.class));
            }
        });

        iniciarInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_inicio.this, activity_login.class));
            }
        });

    }
}