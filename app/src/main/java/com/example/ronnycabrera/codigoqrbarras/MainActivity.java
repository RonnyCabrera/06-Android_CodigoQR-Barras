package com.example.ronnycabrera.codigoqrbarras;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUESTCAMERA=1;
    private ZXingScannerView scannerView;
    private static int casID= Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);//pasar de unactivity a otro activity y atodo y con vista un pedazo
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //CONTROLAR LOS PERMISOS
            if(verficarPermisos()){
                Toast.makeText(getApplicationContext(),"permiso otorgado",Toast.LENGTH_LONG).show();
            }else{
                solicitarPermisos();
            }
        }
    }

    public boolean verficarPermisos(){
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED; //ALGO EN ESPECIFICO;
    }

    public void solicitarPermisos(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUESTCAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int apiVersion=Build.VERSION.SDK_INT;
        if(apiVersion>= Build.VERSION_CODES.M) {
            if (verficarPermisos()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else{
                solicitarPermisos();

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

    }
}
