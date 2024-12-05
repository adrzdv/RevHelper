package com.example.revhelper.activity.services;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.Coach;
import com.example.revhelper.model.Train;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.ResultCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ParseXml {

    public void parseXml(Context context, Uri uri, AppDatabase appDb,
                         ProgressBar progressBar, View v) throws CustomException {

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final StringBuilder resThreed = new StringBuilder();

        progressBar.setVisibility(View.GONE);

        ResultCallback callback = message -> {
            progressBar.setVisibility(View.GONE);
            resThreed.append(message);
        };

        try {

            InputStream input = context.getContentResolver().openInputStream(uri);

            //фабрика для построения документов Document, парсим файл
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(input));

            //нормализуем структуру
            document.getDocumentElement().normalize();

            Element elm = (Element) document.getElementsByTagName("desteny").item(0);
            String elmFlag = elm.getTextContent();


            progressBar.setVisibility(View.VISIBLE);

            if (elmFlag.equals("add")) {

                executor.execute(() -> {
                    final List<Train> trains = getTrainListFromDoc(document, progressBar);
                    final List<Coach> coaches = getCoachesListFromDoc(document, progressBar);
                    final String message;
                    String temp;
                    try {
                        if (!trains.isEmpty()) {
                            appDb.trainDao().insertTrains(trains);
                            handler.post(() -> progressBar.setProgress(50));
                        }
                        if (!coaches.isEmpty()) {
                            appDb.coachDao().insertCoaches(coaches);
                            handler.post(() -> progressBar.setProgress(95));
                        }

                        temp = "Данные успешно загружены";
                        handler.post(() -> progressBar.setProgress(100));
                    } catch (Exception e) {
                        temp = "Ошибка загрузки данных";
                        progressBar.setVisibility(View.GONE);
                    }
                    message = temp;
                    handler.post(() -> callback.onResult(message));
                    handler.post(() -> sendMessage(context, resThreed.toString()));
                });

            } else if (elmFlag.equals("rebase")) {


                executor.execute(() -> {

                    final String message;
                    String temp;

                    final List<Train> trains = getTrainListFromDoc(document, progressBar);
                    final List<Coach> coaches = getCoachesListFromDoc(document, progressBar);
                    try {
                        if (!trains.isEmpty()) {
                            appDb.trainDao().cleanTrainTable();
                            appDb.trainDao().cleanKeys();
                            appDb.trainDao().insertTrains(trains);
                            handler.post(() -> progressBar.setProgress(50));
                        }
                        if (!coaches.isEmpty()) {
                            appDb.coachDao().cleanCoachTable();
                            appDb.coachDao().cleanKeys();
                            appDb.coachDao().insertCoaches(coaches);
                            handler.post(() -> progressBar.setProgress(99));
                        }
                        temp = "Данные успешно загружены";
                        handler.post(() -> progressBar.setProgress(100));
                    } catch (Exception e) {
                        temp = "Ошибка загрузки данных";
                        progressBar.setVisibility(View.GONE);
                    }

                    message = temp;
                    handler.post(() -> callback.onResult(message));
                    handler.post(() -> sendMessage(context, resThreed.toString()));

                });

            } else if (elmFlag.equals("update")) {


            } else {

                input.close();
                throw new CustomException("Некорректный флаг начала файла");
            }

            input.close();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private List<Train> getTrainListFromDoc(Document doc, ProgressBar progressBar) {

        List<Train> res = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName("train");
        for (int i = 0; i < nodeList.getLength(); i++) {
            int perc = i / nodeList.getLength() / 2 * 100;
            Element elm = (Element) nodeList.item(i);
            Train train = new Train();
            train.setDirectNumber(elm.getElementsByTagName("number-straight")
                    .item(0).getTextContent());
            train.setReversedNumber(elm.getElementsByTagName("number-reversed")
                    .item(0).getTextContent());
            train.setRoute(elm.getElementsByTagName("route").item(0)
                    .getTextContent());
            train.setHasProgressive(Integer.parseInt(elm.getElementsByTagName("progressive")
                    .item(0).getTextContent()));
            train.setHasRegistrator(Integer.parseInt(elm.getElementsByTagName("registrator").
                    item(0).getTextContent()));
            train.setHasPortal(Integer.parseInt(elm.getElementsByTagName("portal").item(0).getTextContent()));
            train.setDep(Integer.parseInt(elm.getElementsByTagName("dep")
                    .item(0).getTextContent()));
            res.add(train);
            progressBar.setProgress(perc);
        }

        return res;

    }

    private List<Coach> getCoachesListFromDoc(Document doc, ProgressBar progressBar) {

        List<Coach> res = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName("coach");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element el = (Element) nodeList.item(i);
            int perc = i / nodeList.getLength() / 2 * 100 + 50;
            Coach coach = new Coach();

            coach.setCoachNumber(el.getElementsByTagName("coach-number").item(0).getTextContent());
            coach.setDep(Integer.parseInt(el.getElementsByTagName("branch").item(0).getTextContent()));

            res.add(coach);
            progressBar.setProgress(perc);

        }

        return res;

    }

    private void sendMessage(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }


}
