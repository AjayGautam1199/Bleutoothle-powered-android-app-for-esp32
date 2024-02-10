package com.ag.gautam;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

public class BluetoothLEManager {

    private BluetoothLEService mBluetoothLEService;
    private BluetoothGatt mBluetoothGatt;
    public BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothLEManager(Context context) {
        mBluetoothLEService = new BluetoothLEService();
        context.bindService(new Intent(context, BluetoothLEService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void connectToDevice(String deviceAddress) {
       // BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        if (mBluetoothLEService != null) {
            mBluetoothLEService.connectToDevice(deviceAddress);
        }
    }
    private static final String TAG = "BluetoothLEManager"; // Use an appropriate tag for your class
    @SuppressLint("MissingPermission")
    public void disconnect() {
        if (mBluetoothLEService == null) {
            Log.e(TAG, "BluetoothLEService is null. Unable to disconnect.");
            return;
        }

        if (mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothGatt is null. Unable to disconnect.");
            return;
        }

        mBluetoothGatt.disconnect();
    }


    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(android.content.ComponentName componentName, android.os.IBinder service) {
            BluetoothLEService.LocalBinder binder = (BluetoothLEService.LocalBinder) service;
            mBluetoothLEService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(android.content.ComponentName componentName) {
            mBluetoothLEService = null;
        }
    };
}