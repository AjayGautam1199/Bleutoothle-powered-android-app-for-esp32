package com.ag.gautam;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

public class SecondActivity extends Activity {

    private BluetoothLEManager mBluetoothLEManager;
    private TextView mDataTextView;

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.example.bluetoothle.ACTION_DATA_AVAILABLE".equals(action)) {
                byte[] data = intent.getByteArrayExtra("com.example.bluetoothle.EXTRA_DATA");
                // Handle the received data, update UI, etc.
                String dataString = new String(data);
                Log.d("BluetoothLE", "Received data: " + dataString);

                mDataTextView.setText(dataString);
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        findViewById(android.R.id.content).startAnimation(fadeIn);
        mBluetoothLEManager = new BluetoothLEManager(this);
        mDataTextView = findViewById(R.id.dataTextView);

        // Uncomment the following line to request Bluetooth permissions
        requestBluetoothPermissions();

        // Replace "YOUR_DEVICE_ADDRESS" with the actual Bluetooth device address
       // mBluetoothLEManager.connectToDevice("48:E7:29:94:0B:92");
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLEManager != null) {
            mBluetoothLEManager.disconnect();
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.bluetoothle.ACTION_DATA_AVAILABLE");
        return intentFilter;
    }

    // Add this code in SecondActivity.java
// Add this code in SecondActivity.java
// Add this code in SecondActivity.java
    private static final int REQUEST_ENABLE_BT = 1;


    private void requestBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    REQUEST_ENABLE_BT);
        } else {
            // Permission has already been granted
        }
    }

    // Call this method where needed, e.g., in onCreate
    //requestBluetoothPermissions();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    // Permission denied
                    // Handle accordingly, e.g., show a message to the user
                }
                return;
            }
            // Other cases for additional permissions can be added here
        }
    }

    // Add this method to handle device connection
    private void connectToDevice() {
        BluetoothDevice device = mBluetoothLEManager.getBluetoothAdapter().getRemoteDevice("48:E7:29:94:0B:92");
        mBluetoothLEManager.connectToDevice("48:E7:29:94:0B:92");
    }

    // Existing code...

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second_activity, menu);
        return true;
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_reconnect) {
            // Handle reconnect action
            reconnectToDevice();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Add this method to handle the reconnect action
    private void reconnectToDevice() {
        // Replace "YOUR_DEVICE_ADDRESS" with the actual Bluetooth device address
        connectToDevice();
    }
}