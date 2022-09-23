package com.example.app_garbager_iot.ui.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_garbager_iot.MainActivity;
import com.example.app_garbager_iot.Model.PersonModel;
import com.example.app_garbager_iot.R;
import com.example.app_garbager_iot.ui.registrer.RegisterActivity;
import com.example.app_garbager_iot.Retrofit.FullApis;
import com.example.app_garbager_iot.utils.Validater;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_login extends AppCompatActivity {
    Button btnLogin;
    Button btnSignUp;
    EditText txtEmailLogin, txtPasswordLogin;
    Validater validater;

    @Override
    protected void onCreate(Bundle loginInstance) {
        super.onCreate(loginInstance);
        setContentView(R.layout.login);
        validater = new Validater();
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtEmailLogin = findViewById(R.id.txtEmailLogin);
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              try {
                  accesEmailAndLogin();
              }catch (Exception e){
                  e.printStackTrace();
              }
            }
        });

        //Clic y envia al formulario de RegistrerActivity
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_login.this, RegisterActivity.class));
            }
        });


    }
    String val;
    private void accesEmailAndLogin() throws Exception {
        Call<List<PersonModel>> call = FullApis.getPersonServices().getPerson();
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {

                    if (response.isSuccessful()) {
                        List<PersonModel> listaPersona = response.body();
                        for (PersonModel person :listaPersona){

                            try {
                                val  = validater.encriptar(txtPasswordLogin.getText().toString(),validater.apiKey);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if(person.getEmail().equals(txtEmailLogin.getText().toString()) &&
                                    person.getPassword().equals(val))
                            {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                intent.putExtra("p", person);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Incorrect user or pass.", Toast.LENGTH_LONG).show();

                    }

            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al realizar la petición. " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


}
