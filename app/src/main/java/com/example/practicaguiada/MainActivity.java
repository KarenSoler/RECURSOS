package com.example.practicaguiada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;

    //Version Android
    private TextView versionAndroid;
    private int versionSDK;

    //Batería
    private ProgressBar pbLevelBaterry;
    private TextView tvLevelBaterry;
    IntentFilter batteryFilter;

    //Conexión
    private TextView tvConexion;
    ConnectivityManager conexion;

    //Linterna
    CameraManager cameraManager;
    String cameraId;
    private Button onFlash;
    private Button offFlash;

    //File
    private EditText etStorage;
    private ClFile clFile;
    private Button ibStorage;

    //Bluetooth
    private Button bluet;
    private TextView tvBT;
    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();
        this.activity = this;
        objInit();
        onFlash.setOnClickListener(this::onLigth);
        offFlash.setOnClickListener(this::offLight);
        batteryFilter = new IntentFilter((Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(broReceiver, batteryFilter);
        bluet.setOnClickListener(this::requestBluetoothEnable);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ibStorage.setOnClickListener(this::ibStorage);
    }

    private void ibStorage(View view) {

    }


    //BLUETOOTH
    private void requestBluetoothEnable(View view) {
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.i("Bluetooth", "El dispositivo no admite Bluetooth");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                // Bluetooth no está habilitado, solicitar habilitación
                showEnableBluetoothDialog();
            }
        }
    }

    private void showEnableBluetoothDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Habilitar Bluetooth");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Usuario selecciona "Sí", iniciar la habilitación de Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Usuario selecciona "No"
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void enableBluetooth(View view) {
        try {
            if (bluetoothAdapter == null) {
                //Device doesn't support Bluetooth
                Log.i("Bluetooth", "El dispositivo no es compatible con Bluetooth.");
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    //Solicitar habilitación de Bluetooth
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }
        }catch (Exception e){
            Log.i("Bluetooth", "Error al habilitar Bluetooth: " + e.getMessage());
        }
    }

    //Bateria
    BroadcastReceiver broReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            pbLevelBaterry.setProgress(levelBattery);
            tvLevelBaterry.setText("Level Battery:"+levelBattery+" %");
        }
    };

    //Version Android
    @Override
    protected void onResume() {
        super.onResume();
        String versionSO = Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versionAndroid.setText("Versión SO:"+versionSO+" / SDK"+versionSDK);
        checkConnection();
    }

    //Flash light, Método ON
    private void onLigth(View view) {
        try {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        }catch (CameraAccessException e) {
            throw new RuntimeException("En la linterna"+e);

        }
    }
    //Método off de Flash Ligth
    private void offLight(View view) {
        try {
            cameraManager.setTorchMode(cameraId, false);

        }catch (CameraAccessException e) {
            throw new RuntimeException("En la linterna"+e);

        }
    }
    private void objInit(){
        this.versionAndroid = findViewById(R.id.tvAndroidVersion);
        this.pbLevelBaterry = findViewById(R.id.pbBattery);
        this.tvLevelBaterry = findViewById(R.id.tvBatteryLB);
        this.tvConexion = findViewById(R.id.tvConectionLB);
        this.onFlash = findViewById(R.id.btLightOn);
        this.offFlash = findViewById(R.id.btLightOff);
        this.nameFile = findViewById(R.id.etNameFile);
        this.bluet = findViewById(R.id.btnBT);
        this.tvBT = findViewById(R.id.tvBT);
        this.etStorage = findViewById(R.id.etStorage);
    }

    //Conexión
    private void checkConnection(){
        try {
            conexion = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(conexion !=null) {
                NetworkInfo networkInfo = conexion.getActiveNetworkInfo();
                boolean statNet = networkInfo != null && networkInfo.isConnectedOrConnecting();
                if (statNet) tvConexion.setText("State ON");
                else tvConexion.setText("State Off");
            }else {
                tvConexion.setText("NO INFO");
            }
        }catch (Exception e){
            Log.i("Con",e.getMessage());
        }
    }
}