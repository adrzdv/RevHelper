package com.example.revhelper.services;

import android.text.Editable;

import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.dto.TrainDto;
import com.example.revhelper.sys.AppDatabase;

public class SearchService {

    private final AppDatabase appDb;
    private final CheckService checkService;

    public SearchService(AppDatabase appDb, CheckService checkService) {
        this.checkService = checkService;
        this.appDb = appDb;
    }

    public TrainDto searchTrain(Editable search) throws CustomException {

        if (search.toString().isEmpty()) {

            throw new CustomException("Введите номер поезда");
        }

        String trainString = search.toString().toUpperCase();

        if (!checkService.checkTrainRegex(trainString)) {

            throw new CustomException("Неверный формат ввода номера поезда");
        }

        TrainDto trainDto = appDb.trainDao().findByNumberWithDep(trainString);

        if (trainDto == null) {

            throw new CustomException("Поезд не найден");

        }

        return trainDto;
    }

    public Coach searchCoach(String search) throws CustomException {

        if (search.equals("")) {

            throw new CustomException("Введите номер штабного вагона");

        } else if (!checkService.checkCoachRegex(search)) {

            throw new CustomException("Неверный формат ввода вагона");
        }

        return appDb.coachDao().findByCoach(search);

    }
}
