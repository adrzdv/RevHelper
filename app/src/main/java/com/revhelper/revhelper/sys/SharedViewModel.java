package com.revhelper.revhelper.sys;

import androidx.lifecycle.ViewModel;

import com.revhelper.revhelper.model.dto.RevCoach;
import com.revhelper.revhelper.model.dto.OrderDtoParcelable;
import com.revhelper.revhelper.model.dto.TrainDtoParcelable;
import com.revhelper.revhelper.model.entity.MainCoach;

public class SharedViewModel extends ViewModel {
    private OrderDtoParcelable order;
    private TrainDtoParcelable train;
    private MainCoach informator;
    private RevCoach revCoach;

    public OrderDtoParcelable getOrder() {
        return order;
    }

    public void setOrder(OrderDtoParcelable order) {
        this.order = order;
    }

    public TrainDtoParcelable getTrain() {
        return train;
    }

    public void setTrain(TrainDtoParcelable train) {
        this.train = train;
    }

    public MainCoach getInformator() {
        return informator;
    }

    public void setInformator(MainCoach informator) {
        this.informator = informator;
    }

    public RevCoach getCoachOnRevision() {
        return revCoach;
    }

    public void setCoachOnRevision(RevCoach revCoach) {
        this.revCoach = revCoach;
    }
}
