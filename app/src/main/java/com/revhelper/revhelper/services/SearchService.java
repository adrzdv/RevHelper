package com.revhelper.revhelper.services;

import android.text.Editable;

import com.revhelper.revhelper.exceptions.CustomException;
import com.revhelper.revhelper.model.entity.MainCoach;
import com.revhelper.revhelper.model.dto.TrainDto;
import com.revhelper.revhelper.sys.AppDatabase;

/**
 * Search service class
 */
public class SearchService {

    private final AppDatabase appDb;
    private final CheckService checkService;

    public SearchService(AppDatabase appDb, CheckService checkService) {
        this.checkService = checkService;
        this.appDb = appDb;
    }

    /**
     * Searching train in database
     *
     * @param search search string
     * @return TrainDto object
     * @throws CustomException
     */
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

    /**
     * Search coach with radio in database
     *
     * @param search search string
     * @return MainCoach object
     * @throws CustomException
     */
    public MainCoach searchCoach(String search) throws CustomException {

        if (search.equals("")) {

            throw new CustomException("Введите номер штабного вагона");

        } else if (!checkService.checkCoachRegex(search)) {

            throw new CustomException("Неверный формат ввода вагона");
        }

        return appDb.coachDao().findByCoach(search);

    }
}
