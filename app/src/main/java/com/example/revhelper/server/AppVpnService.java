package com.example.revhelper.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.system.OsConstants;

import androidx.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppVpnService extends VpnService implements Runnable {
    private Thread thread;
    private ParcelFileDescriptor vpnInterface;
    private boolean isRunning = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && "STOP_VPN".equals(intent.getAction())) {
            disconnectVpn();
            return START_NOT_STICKY;
        }

        if (thread == null) {
            thread = new Thread(this, "VPNService");
            thread.start();
        }
        return START_STICKY;
    }

    private final BroadcastReceiver stopVpnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("STOP_VPN".equals(intent.getAction())) {
                stopVpn();
            }
        }
    };

    @Override
    public void run() {

        Builder builder = getBuilder();
        vpnInterface = builder.establish();

        try {
            Process process = Runtime.getRuntime().exec("iptables -t nat -A PREROUTING -p tcp --dport 8181 -j DNAT --to-destination 10.8.0.1:8181");
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileInputStream in = new FileInputStream(vpnInterface.getFileDescriptor());
             FileOutputStream out = new FileOutputStream(vpnInterface.getFileDescriptor())) {
            byte[] buffer = new byte[32767];
            while (true) {
                // Чтение пакетов
                byte[] packet = new byte[32767];
                int length = in.read(packet);
                if (length > 0) {
                    // Обработка пакетов, например, передача на сервер
                    out.write(packet, 0, length); // Запись обратно в туннель
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @NonNull
    private Builder getBuilder() {
        Builder builder = new Builder();
        builder.addAddress("10.8.0.1", 32);
        builder.addRoute("10.8.0.0", 24);
        builder.addDnsServer("8.8.8.8");
        builder.setSession("MyVPN");
        builder.addRoute("10.8.0.1", 32);
        builder.addRoute("192.168.43.0", 24);
        builder.allowBypass();
        try {
            builder.addDisallowedApplication(getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        builder.allowFamily(OsConstants.AF_INET);
        builder.setBlocking(true);
        return builder;
    }

    @Override
    public void onDestroy() {
        isRunning = false; // Останавливаем VPN
        if (vpnInterface != null) {
            try {
                vpnInterface.close();
            } catch (Exception ignored) {
            }
        }
        super.onDestroy();
    }

    public void disconnectVpn() {
        if (vpnInterface != null) {
            try {
                vpnInterface.close(); // Закрываем VPN-интерфейс
            } catch (IOException e) {
                e.printStackTrace();
            }
            vpnInterface = null;
        }
        stopSelf(); // Останавливаем сервис
    }

    public void stopVpn() {
        if (vpnInterface != null) {
            try {
                vpnInterface.close(); // Закрываем VPN-интерфейс
            } catch (IOException e) {
                e.printStackTrace();
            }
            vpnInterface = null;
        }
        stopSelf(); // Останавливаем сервис
    }
}
