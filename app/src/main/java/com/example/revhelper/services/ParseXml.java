package com.example.revhelper.services;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.revhelper.R;
import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.entity.Deps;
import com.example.revhelper.model.entity.Train;
import com.example.revhelper.model.entity.Violation;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Base class for parsing xml-files with additional information
 */

public class ParseXml {

    private final CheckService service = new CheckService();

    /**
     * Method for parsing xml-files
     * File's restriction: root element "destiny" could be only in one or three variants:
     * rebase, add, update
     * Rebase - full rebase info in an existing table;
     * Add - add new information in an existing table;
     * Update - update an existing information in a table (at now not released);
     *
     * @param context context
     * @param uri     file's uri
     * @param appDb   application's database
     * @throws CustomException
     */
    public void parseXml(Context context, Uri uri, AppDatabase appDb) throws CustomException {

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        ProgressBar progressBar = ((Activity) context).findViewById(R.id.progressBarMainActivity);
        ;
        View dimBackground = ((Activity) context).findViewById(R.id.dimBackgroundMainActivity);
        dimBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        try {

            InputStream input = context.getContentResolver().openInputStream(uri);
            Document document = parseDocument(input);

            executor.execute(() -> {

                String flag = "";

                try {
                    flag = getFlagFromDocument(document);
                } catch (CustomException e) {
                    handler.post(() -> {
                        AppRev.showToast(context, "ERROR. Incorrect file.");
                        progressBar.setVisibility(View.GONE);
                        dimBackground.setVisibility(View.GONE);
                    });
                    return;
                }

                Map<String, List<Object>> dataFromDocument = getDataFromDocument(context, document);

                switch (flag) {
                    case "add":
                        for (String key : dataFromDocument.keySet()) {
                            addNewElement(dataFromDocument.get(key), appDb);
                        }
                        handler.post(() -> {
                            AppRev.showToast(context, "Данные успешно загружены");
                            progressBar.setVisibility(View.GONE);
                            dimBackground.setVisibility(View.GONE);
                        });
                        break;
                    case "rebase":
                        for (String key : dataFromDocument.keySet()) {
                            addingWithRebase(dataFromDocument.get(key), appDb);
                            break;
                        }
                        handler.post(() -> {
                            AppRev.showToast(context, "Данные успешно загружены");
                            progressBar.setVisibility(View.GONE);
                            dimBackground.setVisibility(View.GONE);
                        });
                        break;
                    case "update":
                        break;
                    default:
                        handler.post(() -> AppRev.showToast(context, "Ошибка данных"));
                        break;
                }
            });

            if (input != null) {
                input.close();
            }

        } catch (Exception e) {
            AppRev.showToast(context, e.getMessage());
        }
    }

    /**
     * Making list of depot from selected xml file
     *
     * @param doc xml document
     * @return list of depots
     * @throws CustomException
     */
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

    /**
     * Making list of violations from selected xml file
     *
     * @param doc xml document
     * @return list of violations
     * @throws CustomException
     */
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

    /**
     * Make list of trains from selected xml file
     *
     * @param doc xml file
     * @return list of trains
     * @throws CustomException
     */
    @NonNull
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

    /**
     * Make list of coaches with radio from selected xml file
     *
     * @param doc xml file
     * @return list of coaches
     * @throws CustomException
     */
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

    /**
     * Parse input stream in a Document
     *
     * @param input input stream
     * @return Document
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    @NonNull
    private Document parseDocument(InputStream input) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(input));
        document.getDocumentElement().normalize();

        return document;
    }

    /**
     * Getting flag for future work
     *
     * @param document Document where need to find a flag
     * @return flag from document
     * @throws CustomException
     */
    private String getFlagFromDocument(@NonNull Document document) throws CustomException {
        Element elm = (Element) document.getElementsByTagName("destiny").item(0);
        if (elm == null) {
            throw new CustomException("Flag 'destiny' not found");
        }
        return elm.getTextContent();
    }

    /**
     * Get all existing data from document
     *
     * @param context  Context
     * @param document Document for processing
     * @return Map of lists with get objects
     */
    private Map<String, List<Object>> getDataFromDocument(Context context, Document document) {

        List<Train> trains;
        List<Coach> coaches;
        List<Violation> violations;
        List<Deps> deps;
        Map<String, List<Object>> resultMap = new HashMap<>();

        try {
            trains = getTrainListFromDoc(document);
            if (!trains.isEmpty()) {
                resultMap.put("TRAINS", Collections.singletonList(trains));
            }
            coaches = getCoachesListFromDoc(document);
            if (!coaches.isEmpty()) {
                resultMap.put("COACHES", Collections.singletonList(coaches));
            }
            violations = getViolationListFromDoc(document);
            if (!violations.isEmpty()) {
                resultMap.put("VIOLATIONS", Collections.singletonList(violations));
            }
            deps = getDepListFromDoc(document);
            if (!deps.isEmpty()) {
                resultMap.put("DEPS", Collections.singletonList(deps));
            }
        } catch (CustomException e) {
            AppRev.showToast(context, "Ошибка загрузки данных");
        }

        return resultMap;

    }

    /**
     * Adding data with rebasing tables in database
     *
     * @param list  list with data for add
     * @param appDb AppDatabase object
     */
    private <T> void addingWithRebase(@NonNull List<T> list, AppDatabase appDb) {

        List<T> listFormList = (List) list.get(0);

        if (listFormList.get(0) instanceof Coach) {

            appDb.coachDao().cleanCoachTable();
            appDb.coachDao().cleanKeys();
            appDb.coachDao().insertCoaches((List<Coach>) listFormList);

        } else if (listFormList.get(0) instanceof Train) {

            appDb.trainDao().cleanTrainTable();
            appDb.trainDao().cleanKeys();
            appDb.trainDao().insertTrains((List<Train>) listFormList);

        } else if (listFormList.get(0) instanceof Violation) {

            appDb.violationDao().cleanViolationTable();
            appDb.violationDao().cleanKeys();
            appDb.violationDao().insertViolations((List<Violation>) listFormList);

        } else if (listFormList.get(0) instanceof Deps) {

            appDb.depoDao().cleanDepsTable();
            appDb.depoDao().cleanKeys();
            appDb.depoDao().insertDeps((List<Deps>) listFormList);

        } else {
            throw new IllegalArgumentException("Unsupported type: " + list.get(0)
                    .getClass().getName());
        }
    }

    /**
     * Adding data without rebasing tables in database
     *
     * @param list  list with data for add
     * @param appDb AppDatabase object
     */
    private <T> void addNewElement(@NonNull List<T> list, AppDatabase appDb) {

        if (list.get(0) instanceof Coach) {
            appDb.coachDao().insertCoaches((List<Coach>) list);
        } else if (list.get(0) instanceof Train) {
            appDb.trainDao().insertTrains((List<Train>) list);
        } else if (list.get(0) instanceof Violation) {
            appDb.violationDao().insertViolations((List<Violation>) list);
        } else if (list.get(0) instanceof Deps) {
            appDb.depoDao().insertDeps((List<Deps>) list);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + list.get(0)
                    .getClass().getName());
        }
    }


}
