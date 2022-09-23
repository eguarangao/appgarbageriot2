package com.example.app_garbager_iot.ui.prediction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_garbager_iot.Model.Capture;
import com.example.app_garbager_iot.R;
import com.example.app_garbager_iot.Retrofit.FullApis;
import com.example.app_garbager_iot.Services.PedictServices;
import com.example.app_garbager_iot.ml.Modelo10;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_camera_predictor extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myPapel = database.getReference("papel");
    DatabaseReference myMetal = database.getReference("metal");
    DatabaseReference myVidrio = database.getReference("vidrio");
    DatabaseReference myServo = database.getReference("servo");
    ImageView imgCapture;
    TextView txtInfo;
    ImageView imgView;
    Button btnCamara, btnGalery, btnPredecir;
    int imageSize = 32;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_predictor);
        imgCapture = findViewById(R.id.imgCapturePredic);
        btnCamara = findViewById(R.id.btnCamaraPredice);
        btnGalery = findViewById(R.id.btnCapturar);
        btnPredecir = findViewById(R.id.btnIniciarPrediccion);
        txtInfo = findViewById(R.id.txtInfo);

        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

            btnCamara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, 12);
                    } else {
                        Toast.makeText(activity_camera_predictor.this, "Error..", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        btnPredecir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Modelo10 model = Modelo10.newInstance(activity_camera_predictor.this);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());


                    // Runs model inference and gets result.
                    Modelo10.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    float[] confidences = outputFeature0.getFloatArray();
                    // find the index of the class with the biggest confidence.
                    int maxPos = 1;
                    float maxConfidence = 0;
                    for (int i = 0; i < confidences.length; i++) {
                        if (confidences[i] > maxConfidence) {
                            maxConfidence = confidences[i];
                            maxPos = i;
                        }
                    }
                    // Releases model resources if no longer used.
                    String[] classes = {"METAL", "PAPER", "PLASTIC", "GLASS"};
                    txtInfo.setText(classes[maxPos]);
                    model.close();
                    switch (txtInfo.getText().toString()) {
                        case "METAL":
                            getServoServo(180);
                            getServoMetal(180);
                            apiPrediction("metal");
                            break;
                        case "PAPER":
                            getServoServo(180);
                            getServoPapel(180);
                            apiPrediction("paper");
                            break;
                        case "GLASS":
                            getServoServo(180);
                            getServoVidrio(180);
                            apiPrediction("glass");
                            break;
                        case "PLASTIC":
                            getServoServo(180);
                            getServoVidrio(180);
                            apiPrediction("plastic");
                            break;

                        default:
                            txtInfo.setText("Invalido..");
                            model.close();
                    }
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                getServoServo(0);
                getServoMetal(0);

                getServoPapel(0);
                getServoVidrio(0);
            }
        });


    }
    PedictServices pedictServices;


    void apiPrediction(String valor){
        pedictServices = FullApis.gePedictServices();
        Call<List<Capture>> call = pedictServices.getPredict(valor);
        call.enqueue(new Callback<List<Capture>>() {
            @Override
            public void onResponse(Call<List<Capture>> call, Response<List<Capture>> response) {
               if(response.isSuccessful()){
                   Capture p= (Capture) response.body();
                   String urldato = "https://recycle-trash.herokuapp.com/"+p.getState();
                   Toast.makeText(activity_camera_predictor.this, urldato, Toast.LENGTH_LONG).show();

               }
            }

            @Override
            public void onFailure(Call<List<Capture>> call, Throwable t) {

            }
        });
    }
   public String enviarPhp(String tipo){
       URL url = null;
       String linea = "";
       int respuesta = 0;
       StringBuilder resul = null;
       try {
           url = new URL("https://recycle-trash.herokuapp.com/controller/prediction.php?predit="+tipo);
           HttpURLConnection connection=(HttpURLConnection)url.openConnection();
           respuesta=connection.getResponseCode();
           resul=new StringBuilder();
           if(respuesta==HttpURLConnection.HTTP_OK){
               InputStream in = new BufferedInputStream(connection.getInputStream());
               BufferedReader reader = new BufferedReader(new InputStreamReader(in));
               while((linea = reader.readLine())!=null){
                   resul.append(linea);
               }
           }
       }catch (Exception e){
           e.getMessage();
       }
       return resul.toString();
   }
   public int obtenerDatosJson(String response){
        int res = 0;
        try{
            JSONArray json = new JSONArray(response);
            if(json.length()>0){
                res=1;
            }
        }catch (Exception e){
            e.getMessage();
        }
        return res;
   }


    void getServoPapel(int valor) {
        myPapel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = valor;
                myPapel.setValue(valor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getServoMetal(int valor) {
        myMetal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = valor;
                myMetal.setValue(valor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getServoVidrio(int valor) {
        myVidrio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = valor;
                myVidrio.setValue(valor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getServoServo(int valor) {
        myServo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = valor;
                myServo.setValue(valor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity_camera_predictor.this, new String[]{Manifest.permission.CAMERA}, 11);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imgCapture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 12) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgCapture.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}