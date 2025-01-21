package com.example.revhelper.activity;

import androidx.lifecycle.ViewModel;

import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.model.entity.Coach;

public class SharedViewModel extends ViewModel {
    private OrderDtoParcelable order;
    private TrainDtoParcelable train;
    private Coach informator;

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

    public Coach getInformator() {
        return informator;
    }

    public void setInformator(Coach informator) {
        this.informator = informator;
    }
}
