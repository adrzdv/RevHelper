package com.example.revhelper.mapper;

import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.model.dto.TrainDto;
import com.example.revhelper.model.entity.Train;

public class TrainMapper {

    public static TrainDtoParcelable toParceFromDto(TrainDto train) {

        return new TrainDtoParcelable(train.getNumber(),
                train.getRoute(),
                train.getDepName(),
                train.getBranchName(),
                train.getHasRegistrator(),
                train.getHasProgressive(),
                train.getHasPortal());
    }

    public static TrainDto fromParcelableToDto(TrainDtoParcelable train) {
        return new TrainDto(train.getNumber(),
                train.getRoute(),
                train.getBranchName(),
                train.getDepName(),
                train.getHasRegistrator(),
                train.getHasProgressive(),
                train.getHasPortal());
    }

}
