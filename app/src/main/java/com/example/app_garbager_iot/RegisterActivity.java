package com.example.app_garbager_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_garbager_iot.Model.PersonModel;
import com.example.app_garbager_iot.Retrofit.FullApis;
import com.example.app_garbager_iot.Services.PersonServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText txtFname;
    EditText txtLname;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtRepeatPassword;
    Button btnSaveUser;
    PersonServices personServices;
    String val = "";
    FloatingActionButton flobtnAtras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        flobtnAtras = findViewById(R.id.btnButonAtras);
        flobtnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        //Asignacion de componentes
        txtEmail = findViewById(R.id.txtEmailRegistre);
        txtFname = findViewById(R.id.txtnombre);
        txtLname = findViewById(R.id.txtApellido);
        txtPassword = findViewById(R.id.txtPaswordRegistreUser);
        txtRepeatPassword = findViewById(R.id.txtValidarPassword);
        btnSaveUser = findViewById(R.id.btnSingUpRegistre);

        //Clic Registrar
        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validater validate = new Validater();

                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                PersonModel person = new PersonModel();
                person.setFirstName(txtFname.getText().toString());
                person.setLastName(txtLname.getText().toString());
                person.setEmail(txtEmail.getText().toString());
                try {
                    val = validate.encriptar(txtPassword.getText().toString(),validate.apiKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                person.setPassword(txtPassword.getText().toString());
                Matcher matcher = pattern.matcher(person.getEmail());
                String repeatPasswor = txtRepeatPassword.getText().toString();
                if ((person.getFirstName().isEmpty() || person.getLastName().isEmpty() || person.getPassword().isEmpty() || person.getEmail().isEmpty())) {
                    Toast.makeText(RegisterActivity.this, "One or more fields are empty" , Toast.LENGTH_SHORT).show();
                }else if(matcher.find() == false){
                    Toast.makeText(RegisterActivity.this, "Email error" , Toast.LENGTH_SHORT).show();
                }
                else if (person.getPassword().equals(repeatPasswor) && !(person.getFirstName().isEmpty() || person.getLastName().isEmpty() || person.getPassword().isEmpty() || person.getEmail().isEmpty())) {
                    person.setPassword(val);
                    savePerson(person);

                }
                else{

                }


            }
        });

    }

    private void savePerson(PersonModel person) {
        personServices = FullApis.getPersonServices();
        Call<PersonModel> call = personServices.addPerson(person);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {

                if (response != null) {
                    Toast.makeText(RegisterActivity.this, "Successful registration.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error...", Toast.LENGTH_LONG).show();

            }
        });

    }
}