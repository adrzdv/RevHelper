package com.example.revhelper.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.example.revhelper.model.dto.RevCoach;
import com.example.revhelper.services.util.MapCoachTypeToken;
import com.example.revhelper.services.util.LocalDateTimeSerializer;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.sys.AppRev;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Map;

public class ResultParser {

    /**
     * Exports a collected data in JSON-file to send on another device for compiling results in one
     *
     * @param context Context
     * @param order   Order object
     */
    public static void exportData(Context context, OrderDtoParcelable order) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();

        String exportString = gson.toJson(order.getCoachMap());

        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File myFolder = new File(documentsDir, "revhelper");

        if (!myFolder.exists()) {
            boolean isCreated = myFolder.mkdirs();
            if (!isCreated) {
                AppRev.showToast(context, "Ошибка создания директории");
                return;
            }
        }

        String fileName = order.getNumber().replace('/', '_');
        File tempFile = new File(myFolder, fileName + ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(exportString);
            AppRev.showToast(context, "Файл " + fileName + " создан");
            shareFile(context, tempFile);
        } catch (IOException e) {
            AppRev.showToast(context, "IOException");
        }

    }

    /**
     * Sharing prepared file with data
     *
     * @param context context
     * @param file    file to send
     */
    private static void shareFile(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(intent, "Выберите приложение"));
    }

    /**
     * Importing data from received data in JSON-file to unite data
     *
     * @param context Context
     * @param uri     file's path
     * @return Map of RevCoach objects
     * @throws FileNotFoundException
     */
    public static Map<String, RevCoach> importData(Context context, Uri uri) throws FileNotFoundException {

        Map<String, RevCoach> importedData;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();


        InputStream input = context.getContentResolver().openInputStream(uri);
        if (input == null) {
            String message = "Failed to open InputStream from Uri";
            AppRev.showToast(context, message);
            throw new FileNotFoundException(message);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonData = jsonBuilder.toString();
            Type mapTypeToken = new MapCoachTypeToken().getType();
            importedData = gson.fromJson(jsonData, mapTypeToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return importedData;
    }
}
