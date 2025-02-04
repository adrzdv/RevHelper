package com.example.revhelper.activity.revision;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.example.revhelper.R;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.server.AppVpnService;
import com.example.revhelper.server.HttpClient;
import com.example.revhelper.server.HttpServer;
import com.example.revhelper.sys.AppRev;
import com.example.revhelper.sys.SharedViewModel;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.io.IOException;
import java.net.InetAddress;

import fi.iki.elonen.NanoHTTPD;

public class RevisionSendOrReceiveFragment extends Fragment implements View.OnClickListener {

    private HttpClient client;
    private HttpServer server;
    private SharedViewModel sharedViewModel;
    private OrderDtoParcelable order;
    private final ActivityResultLauncher<Intent> vpnPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    startVpnService();
                } else {
                    AppRev.showToast(requireContext(), "Невозможно запустить VpnService");
                    Log.d("VPN", "Разрешение на VPN отклонено");
                }
            });


    public RevisionSendOrReceiveFragment() {
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        order = sharedViewModel.getOrder();

        client = new HttpClient();

        MaterialSwitch switcher = getView().findViewById(R.id.revision_client_server_switcher);
        switcher.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
        return inflater.inflate(R.layout.fragment_revision_send_or_receive, container, false);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.revision_client_server_switcher) {
            MaterialSwitch switcher = getView().findViewById(R.id.revision_client_server_switcher);
            if (switcher.isChecked()) {
                startVpn(); // Запускаем VPN

                new Thread(() -> {
                    try {
                        waitForVpnInterface("10.8.0.1"); // Ждём, пока появится tun0
                        startServer(); // Запускаем сервер
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            } else {
                stopServer();
                stopVpnService();
            }
        }
    }

    private void waitForVpnInterface(String ipAddress) throws InterruptedException {
        int maxTries = 10; // Количество попыток
        int delayMs = 500; // Задержка между попытками (мс)

        for (int i = 0; i < maxTries; i++) {
            if (isIpAvailable(ipAddress)) {
                Log.d("VPN", "VPN-интерфейс " + ipAddress + " доступен!");
                return;
            }
            Log.d("VPN", "Ожидание поднятия VPN-интерфейса...");
            Thread.sleep(delayMs);
        }
        Log.e("VPN", "Не удалось дождаться поднятия VPN!");
    }

    private boolean isIpAvailable(String ipAddress) {
        try {
            return InetAddress.getByName(ipAddress).isReachable(100);
        } catch (IOException e) {
            return false;
        }
    }

    private void startServer() throws IOException {
        server = new HttpServer("10.8.0.1", 8181);
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        requireActivity().runOnUiThread(() -> {
            if (server.wasStarted()) {
                AppRev.showToast(requireContext(), "Сервер запущен");
                Log.d("HttpServer", "Сервер запущен на " + server.getHostname());
            } else {
                AppRev.showToast(requireContext(), "RUNTIME ERROR");
            }
        });
    }

    private void startVpnService() {
        Intent serviceIntent = new Intent(requireContext(), AppVpnService.class);
        requireContext().startService(serviceIntent);
    }

    private void startVpn() {
        // Запрашиваем разрешение через registerForActivityResult()
        // Разрешение уже есть, запускаем VPN

        Intent intent = VpnService.prepare(getContext());
        if (intent != null) {
            vpnPermissionLauncher.launch(intent);
        } else {
            startVpnService();
        }
    }

    private void stopServer() {

        server.stop();
        requireActivity().runOnUiThread(() -> {
            if (!server.isAlive()) {
                AppRev.showToast(requireContext(), "Сервер остановлен");
            } else {
                AppRev.showToast(requireContext(), "RUNTIME ERROR");
            }
        });
    }

    private void stopVpnService() {

        Intent intent = new Intent(requireContext(), AppVpnService.class);
        intent.setAction("STOP_VPN");
        requireContext().startService(intent);
    }
}