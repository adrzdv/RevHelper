package com.example.revhelper.services;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.entity.Deps;
import com.example.revhelper.model.entity.Train;
import com.example.revhelper.model.entity.Violation;
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

        ResultCallback callback = resThreed::append;

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

            switch (elmFlag) {
                case "add":

                    executor.execute(() -> {
                        List<Train> trains;
                        List<Coach> coaches;
                        List<Violation> violations;
                        List<Deps> deps;

                        final String message;
                        String temp;

                        try {
                            trains = getTrainListFromDoc(document);
                            coaches = getCoachesListFromDoc(document);
                            violations = getViolationListFromDoc(document);
                            deps = getDepListFromDoc(document);

                            if (!trains.isEmpty()) {
                                appDb.trainDao().insertTrains(trains);
                            }
                            if (!coaches.isEmpty()) {
                                appDb.coachDao().insertCoaches(coaches);
                            }
                            if (!violations.isEmpty()) {
                                appDb.violationDao().insertViolations(violations);
                            }
                            if (!deps.isEmpty()) {
                                appDb.depoDao().insertDeps(deps);
                            }

                            temp = "Данные успешно загружены";
                        } catch (CustomException e) {
                            temp = "Ошибка загрузки данных";
                        }
                        message = temp;
                        handler.post(() -> callback.onResult(message));
                        handler.post(() -> sendMessage(context, resThreed.toString()));
                    });

                    break;
                case "rebase":

                    executor.execute(() -> {

                        final String message;
                        String temp;

                        List<Train> trains;
                        List<Coach> coaches;
                        List<Violation> violations;
                        List<Deps> deps;

                        try {
                            trains = getTrainListFromDoc(document);
                            coaches = getCoachesListFromDoc(document);
                            violations = getViolationListFromDoc(document);
                            deps = getDepListFromDoc(document);
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
                            if (!violations.isEmpty()) {
                                appDb.violationDao().cleanViolationTable();
                                appDb.violationDao().cleanKeys();
                                appDb.violationDao().insertViolations(violations);
                            }
                            if (!deps.isEmpty()) {
                                appDb.depoDao().cleanDepsTable();
                                appDb.depoDao().cleanKeys();
                                appDb.depoDao().insertDeps(deps);
                            }
                            temp = "Данные успешно загружены";
                        } catch (CustomException e) {
                            temp = "Ошибка загрузки данных";
                        }

                        message = temp;
                        handler.post(() -> callback.onResult(message));
                        handler.post(() -> sendMessage(context, resThreed.toString()));

                    });

                    break;
                case "update":
                    input.close();
                    break;
                default:
                    input.close();
                    throw new CustomException("Некорректный флаг начала файла");
            }

            input.close();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<Deps> getDepListFromDoc(Document doc) throws CustomException {
        List<Deps> depList = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName("dep");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            Deps dep = new Deps();
            dep.setName(element.getElementsByTagName("name").item(0).getTextContent());
            dep.setBranch(Integer.parseInt(element.getElementsByTagName("branch").item(0).getTextContent()));
            depList.add(dep);
        }

        return depList;
    }

    private List<Violation> getViolationListFromDoc(Document doc) throws CustomException {

        List<Violation> violationList = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName("violation");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            Violation violation = new Violation();
            violation.setName(element.getElementsByTagName("name").item(0).getTextContent());
            violation.setCode(Integer.parseInt(element.getElementsByTagName("code").item(0).getTextContent()));
            violation.setConflictiveCode(element.getElementsByTagName("conflictive_code").item(0) == null ? 0 :
                    Integer.parseInt(element.getElementsByTagName("conflictive_code").item(0).getTextContent()));
            violation.setInTransit(Integer.parseInt(element.getElementsByTagName("in_transit").item(0).getTextContent()));
            violation.setAtStartPoint(Integer.parseInt(element.getElementsByTagName("at_start_point").item(0).getTextContent()));
            violation.setAtTurnroundPoint(Integer.parseInt(element.getElementsByTagName("at_turnround_point").item(0).getTextContent()));
            violation.setAtTicketOffice(Integer.parseInt(element.getElementsByTagName("at_ticket_office").item(0).getTextContent()));
            violation.setActive(element.getElementsByTagName("active").item(0) == null ? 1 :
                    Integer.parseInt(element.getElementsByTagName("active").item(0).getTextContent()));
            violationList.add(violation);
        }

        return violationList;
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
            train.setDep(Integer.parseInt(elm.getElementsByTagName("deps")
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
