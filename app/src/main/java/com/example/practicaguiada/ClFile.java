package com.example.practicaguiada;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.OutputStreamWriter;

public class ClFile {
    private static final int REQUEST_CODE = 23;
    private Context context;
    private Activity activity;

    public ClFile(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }
    private boolean statusPermissionES(){
        int response = ContextCompat.checkSelfPermission(this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(response == PackageManager.PERMISSION_GRANTED) return true;
        return false;
    }
    private void requestPermissionES(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            //Gestión de respuesta
            Toast.makeText(context, "Permiso otorgado", Toast.LENGTH_SHORT).show();
        }
    }
    private void createDir(File file){
        if(!file.exists()){
            file.mkdirs();
        }
    }
    public void saveFile(){
        String nameFile = etStorage.getText().toString();
        try{
            OutputStreamWriter ClFile = new OutputStreamWriter(openFileOutput(nameFile, context.MODE_PRIVATE));

        }

    }
}
