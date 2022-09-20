
package com.example.app_garbager_iot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_garbager_iot.ml.Modelo10;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.schema.Model;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class camera_predictor extends AppCompatActivity {
    ImageView imgCapture;
    TextView txtInfo;
    Button btnCamara, btnGalery,btnPredecir;
    int imageSize = 32;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_predictor);
        imgCapture = findViewById(R.id.imgCapture);
        btnCamara = findViewById(R.id.btnCapturar);
        btnGalery = findViewById(R.id.btnCamera);
        btnPredecir = findViewById(R.id.btnPredecir);
        txtInfo = findViewById(R.id.txtInfo);

        btnCamara.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
             Intent intent = new Intent();
             intent.setAction(Intent.ACTION_GET_CONTENT);
             intent.setType("image/*");
             startActivityForResult(intent,10);
            }
        });
        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 12);
            }
        });
         btnPredecir.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 try {
                     Modelo10 model = Modelo10.newInstance(camera_predictor.this);

                     // Creates inputs for reference.
                     TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                    bitmap =Bitmap.createScaledBitmap(bitmap,224,224,true);
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
                     String[] classes ={"METAL","PAPEL","PLASTICO","VIDRIO"};
                    txtInfo.setText(classes[maxPos]);
                     model.close();

                 } catch (IOException e) {
                     // TODO Handle the exception
                 }
             }

         });
txtInfo.setText("");
    }
    void getPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(camera_predictor.this,new String[]{Manifest.permission.CAMERA},11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
 if(grantResults.length>0){
     if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
         this.getPermission();
     }
 }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(requestCode==10){
           if(data!=null){
               Uri uri = data.getData();
               try {
                   bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                   imgCapture.setImageBitmap(bitmap);
               }catch (IOException e){
e.printStackTrace();
               }
           }
       }else if(requestCode==12){
           bitmap =(Bitmap)data.getExtras().get("data");
           imgCapture.setImageBitmap(bitmap);
       }
        super.onActivityResult(requestCode, resultCode, data);
    }
}