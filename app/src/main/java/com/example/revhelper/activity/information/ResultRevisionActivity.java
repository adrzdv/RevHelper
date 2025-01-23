package com.example.revhelper.activity.information;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityResultRevisionBinding;
import com.example.revhelper.mapper.ViolationMapper;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.dto.TrainDto;
import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.model.dto.ViolationForCoach;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.entity.MainNodes;
import com.example.revhelper.model.entity.Violation;
import com.example.revhelper.model.enums.AdditionalParams;
import com.example.revhelper.model.enums.RevisionType;
import com.example.revhelper.model.enums.WorkerJob;
import com.example.revhelper.sys.AppRev;
import com.example.revhelper.sys.SharedViewModel;

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultRevisionActivity extends AppCompatActivity {

    private OrderDtoParcelable order;
    private SharedViewModel sharedViewModel;
    private ActivityResultRevisionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultRevisionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initData();
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (order != null) {
            sharedViewModel.setOrder(order);
        }
    }

    private void initData() {
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        if (sharedViewModel.getOrder() != null) {
            order = sharedViewModel.getOrder();
        } else if (getIntent().getParcelableExtra("ORDER") != null) {
            order = getIntent().getParcelableExtra("ORDER");
            sharedViewModel.setOrder(order);
        }

        makeResult(order);
    }

    private void makeResult(OrderDtoParcelable order) {
        TextView mainTrainInfo = binding.revisionTrainInfo;
        TextView mainTrainStatsTextView = binding.revisionMainTrainStatsTextView;
        TextView trailingStatsTextView = binding.revisionTrailingStatsTextView;
        TextView mainViolationTextView = binding.revisionMainTrainViolationsTextView;
        TextView trailingViolationTextView = binding.revisionTrailingViolationsTextView;

        Map<String, CoachOnRevision> coachMap = order.getCoachMap();
        List<CoachOnRevision> mainTrainCoachList = coachMap.values().stream()
                .filter(coach -> !coach.isTrailingCar())
                .collect(Collectors.toList());

        List<CoachOnRevision> trailingCoachList = coachMap.values().stream()
                .filter(CoachOnRevision::isTrailingCar)
                .collect(Collectors.toList());

        List<Violation> violationList = AppRev.getDb().violationDao().getAllViolations();

        long autoDoorsCountTrailing = countAutodoors(trailingCoachList);
        long skudoppCountTrailing = countSkudopp(trailingCoachList);
        long autoDoorsCount = countAutodoors(mainTrainCoachList);
        long skudoppCount = countSkudopp(mainTrainCoachList);

        TrainDtoParcelable train = order.getTrain();
        String trainString = makeTrainInfoString(order, train);

        mainTrainInfo.setText(trainString);
        mainTrainStatsTextView.setText(makeStaticString(skudoppCount, autoDoorsCount));
        trailingStatsTextView.setText(makeStaticString(skudoppCountTrailing, autoDoorsCountTrailing));

        mainViolationTextView.setText(makeViolationString(violationList, mainTrainCoachList, order));
        trailingViolationTextView.setText(makeViolationString(violationList, trailingCoachList, order));

    }

    private String makeViolationString(List<Violation> violationList, List<CoachOnRevision> coachList, OrderDtoParcelable order) {
        StringBuilder resultStringBuilder = new StringBuilder();
        RevisionType rType = RevisionType.fromString(order.getRevisionType());
        Map<String, List<String>> resStringMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        List<ViolationForCoach> violationsForWorkList = getViolationForCoachListByRevisionType(violationList, rType);

        for (ViolationForCoach violation : violationsForWorkList) {
            List<String> coachStringList = new ArrayList<>();
            for (CoachOnRevision coach : coachList) {
                if (coach.getViolationList().contains(violation)) {
                    StringBuilder coachString = new StringBuilder();
                    coachString.append(coach.getCoachNumber()).append(" ")
                            .append(coach.getRevisionTime().format(formatter))
                            .append(" ").append(coach.getCoachWorker()).append(" ")
                            .append(coach.getCoachWorkerDep()).append("\n");
                    coachStringList.add(coachString.toString());
                    resStringMap.put(violation.getName(), coachStringList);
                }
            }

        }

        for (String key : resStringMap.keySet()) {
            resultStringBuilder.append(key).append("\n");
            for (String value : resStringMap.get(key)) {
                resultStringBuilder.append(value).append("\n");
            }
        }

        return resultStringBuilder.toString();
    }

    private List<ViolationForCoach> getViolationForCoachListByRevisionType(List<Violation> violationList, RevisionType revisionType) {
        List<ViolationForCoach> compairingList = new ArrayList<>();

        if (revisionType == RevisionType.IN_TRANSIT) {
            compairingList = violationList.stream()
                    .filter(violation -> violation.getInTransit() == 1)
                    .map(ViolationMapper::fromEntityToForCouch)
                    .collect(Collectors.toList());

        } else if (revisionType == RevisionType.AT_START_POINT) {

            compairingList = violationList.stream()
                    .filter(violation -> violation.getAtStartPoint() == 1)
                    .map(ViolationMapper::fromEntityToForCouch)
                    .collect(Collectors.toList());

        } else if (revisionType == RevisionType.AT_TURNROUND_POINT) {

            compairingList = violationList.stream()
                    .filter(violation -> violation.getAtTurnroundPoint() == 1)
                    .map(ViolationMapper::fromEntityToForCouch)
                    .collect(Collectors.toList());

        } else if (revisionType == RevisionType.AT_TICKET_OFFICE) {

            compairingList = violationList.stream()
                    .filter(violation -> violation.getAtTicketOffice() == 1)
                    .map(ViolationMapper::fromEntityToForCouch)
                    .collect(Collectors.toList());

        }

        return compairingList;
    }

    private String makeStaticString(long doorsCount, long skudoppCount) {

        StringBuilder staticStringResult = new StringBuilder();
        staticStringResult.append(AdditionalParams.AUTO_DOOR.getAdditionalParamTitle()).append(":").append(doorsCount).append("\n")
                .append(AdditionalParams.SKUDOPP.getAdditionalParamTitle()).append(": ").append(skudoppCount).append("\n");

        return staticStringResult.toString();

    }

    private long countSkudopp(List<CoachOnRevision> list) {
        return list.stream()
                .filter(CoachOnRevision::isCoachSkudopp)
                .count();
    }

    private long countAutodoors(List<CoachOnRevision> list) {
        return list.stream()
                .filter(CoachOnRevision::isCoachAutomaticDoor)
                .count();
    }

    private String makeTrainInfoString(OrderDtoParcelable order, TrainDtoParcelable train) {
        StringBuilder trainInfoString = new StringBuilder();
        String yesString = "ДА";
        String noString = "НЕТ";
        trainInfoString.append(train.getNumber()).append(" ").append(train.getRoute()).append("\n")
                .append(train.getDepName()).append(" ").append(train.getBranchName()).append("\n");

        trainInfoString.append("Попутчик: ");
        if (train.getHasPortal() == 1) {
            trainInfoString.append(yesString);
        } else {
            trainInfoString.append(noString);
        }

        trainInfoString.append("\n").append("Видеорегистратор: ");

        if (train.getHasRegistrator() == 1) {
            trainInfoString.append(yesString);
        } else {
            trainInfoString.append(noString);
        }

        trainInfoString.append("\n").append("Прогресс: ");

        if (train.getHasProgressive() == 1) {
            trainInfoString.append(yesString);
        } else {
            trainInfoString.append(noString);
        }

        trainInfoString.append("\n").append("Прейскурант: ");

        if (order.getIsPrice() != null) {
            if (order.isPrice()) {
                trainInfoString.append(yesString);
            } else if (!order.isPrice()) {
                trainInfoString.append(noString);
            }
        } else {
            trainInfoString.append("Н/Д");
        }

        trainInfoString.append("\n").append("Радиоустановка: ");

        if (order.getIsAutoinformator() != null) {
            if (order.isAutoinformator()) {
                trainInfoString.append(yesString);
            } else if (!order.isAutoinformator()) {
                trainInfoString.append(noString);
            }

        } else {
            trainInfoString.append("Н/Д");
        }

        trainInfoString.append("\n").append("Автоинформатор: ");

        if (train.getHasAutoinformator() == 1) {
            trainInfoString.append(yesString);
        } else if (!order.isAutoinformator()) {
            trainInfoString.append(noString);
        }

        trainInfoString.append("\n").append("Паспорт качества: ");

        if (order.getIsQualityPassport() != null) {
            if (order.isQualityPassport()) {
                trainInfoString.append(yesString);
            } else if (!order.isQualityPassport()) {
                trainInfoString.append(noString);
            }

        } else {
            trainInfoString.append("Н/Д");
        }

        trainInfoString.append("\n").append("ЛНП: ");
        trainInfoString.append("\n")
                .append(order.getCrewLeaders().get(WorkerJob.LNP.getJobTitle()).getName());
        trainInfoString.append("\n").append("ПЭМ: ");
        trainInfoString.append("\n")
                .append(order.getCrewLeaders().get(WorkerJob.PEM.getJobTitle()).getName());

        trainInfoString.append("\n");

        return trainInfoString.toString();

    }
}