package com.example.revhelper.services;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.coach.Coach;
import com.example.revhelper.model.train.Train;
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

    private final CheckService service = new CheckService();

    public void parseXml(Context context, Uri uri, AppDatabase appDb) throws CustomException {

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final StringBuilder resThreed = new StringBuilder();


        ResultCallback callback = message -> {
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

            if (elmFlag.equals("add")) {

                executor.execute(() -> {
                    List<Train> trains;
                    List<Coach> coaches;

                    final String message;
                    String temp;

                    try {
                        trains = getTrainListFromDoc(document);
                        coaches = getCoachesListFromDoc(document);

                        if (!trains.isEmpty()) {
                            appDb.trainDao().insertTrains(trains);
                        }
                        if (!coaches.isEmpty()) {
                            appDb.coachDao().insertCoaches(coaches);
                        }

                        temp = "Данные успешно загружены";
                    } catch (CustomException e) {
                        temp = "Ошибка загрузки данных";
                    }
                    message = temp;
                    handler.post(() -> callback.onResult(message));
                    handler.post(() -> sendMessage(context, resThreed.toString()));
                });

            } else if (elmFlag.equals("rebase")) {

                executor.execute(() -> {

                    final String message;
                    String temp;

                    List<Train> trains;
                    List<Coach> coaches;

                    try {
                        trains = getTrainListFromDoc(document);
                        coaches = getCoachesListFromDoc(document);
                        if (!trains.isEmpty()) {
                            appDb.trainDao().cleanTrainTable();
                            appDb.trainDao().cleanKeys();
                            appDb.trainDao().insertTrains(trains);
                        }
                        if (!coaches.isEmpty()) {
                            appDb.coachDao().cleanCoachTable();
                            appDb.coachDao().cleanKeys();
                            appDb.coachDao().insertCoaches(coaches);
                        }
                        temp = "Данные успешно загружены";
                    } catch (CustomException e) {
                        temp = "Ошибка загрузки данных";
                    }

                    message = temp;
                    handler.post(() -> callback.onResult(message));
                    handler.post(() -> sendMessage(context, resThreed.toString()));

                });

            } else if (elmFlag.equals("update")) {
                input.close();
            } else {
                input.close();
                throw new CustomException("Некорректный флаг начала файла");
            }

            input.close();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<Train> getTrainListFromDoc(Document doc) throws CustomException {

        List<Train> res = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName("train");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element elm = (Element) nodeList.item(i);
            Train train = new Train();
            if (!service.checkTrainRegex(elm.getElementsByTagName("number")
                    .item(0).getTextContent())) {
                throw new CustomException("Ошибка парсинга");
            } else {
                train.setNumber(elm.getElementsByTagName("number")
                        .item(0).getTextContent());
            }

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
        }

        return res;

    }

    private List<Coach> getCoachesListFromDoc(Document doc) throws CustomException {

        List<Coach> res = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName("coach");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element el = (Element) nodeList.item(i);
            Coach coach = new Coach();

            if (!service.checkCoachRegex(el.getElementsByTagName("coach-number")
                    .item(0).getTextContent())) {
                throw new CustomException("Ошибка парсинга");
            } else {
                coach.setCoachNumber(el.getElementsByTagName("coach-number")
                        .item(0).getTextContent());
            }
            coach.setDep(Integer.parseInt(el.getElementsByTagName("branch")
                    .item(0).getTextContent()));

            res.add(coach);

        }

        return res;

    }

    private void sendMessage(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }


}
