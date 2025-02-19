package com.revhelper.revhelper.mapper;

import com.revhelper.revhelper.model.dto.TrainDtoParcelable;
import com.revhelper.revhelper.model.dto.TrainDto;

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
