package com.example.revhelper.services;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.example.revhelper.services.util.MapCoachTypeToken;
import com.example.revhelper.services.util.LocalDateTimeSerializer;
import com.example.revhelper.model.dto.CoachOnRevision;
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
import java.util.List;
import java.util.Map;

public class ResultParser {

    public void exportData(Context context, OrderDtoParcelable order) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();

        String exportString = gson.toJson(order.getCoachMap());

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFolder = new File(downloadsDir, "revhelper");

        if (!myFolder.exists()) {
            boolean isCreated = myFolder.mkdirs();
            if (isCreated) {

            }
        }

        String fileName = order.getNumber().replace('/', '_');
        File tempFile = new File(myFolder, fileName + ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(exportString);
            AppRev.showToast(context, "Файл " + fileName + " создан");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Map<String, CoachOnRevision> importData(Context context, Uri uri) throws FileNotFoundException {

        Map<String, CoachOnRevision> importedData;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();


        InputStream input = context.getContentResolver().openInputStream(uri);
        if (input == null) {
            AppRev.showToast(context, "Failed to open InputStream from Uri");
            throw new FileNotFoundException("Failed to open InputStream from Uri");
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
