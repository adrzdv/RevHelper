package com.example.revhelper.activity.information;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.revhelper.databinding.ActivityResultRevisionBinding;
import com.example.revhelper.mapper.ViolationMapper;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.model.dto.ViolationForCoach;
import com.example.revhelper.model.dto.Worker;
import com.example.revhelper.model.entity.Violation;
import com.example.revhelper.model.enums.StatsCoachParams;
import com.example.revhelper.model.enums.RevisionType;
import com.example.revhelper.sys.AppRev;
import com.example.revhelper.sys.SharedViewModel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultRevisionActivity extends AppCompatActivity implements View.OnClickListener {

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
        binding.revisionEndRevision.setOnClickListener(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (order != null) {
            sharedViewModel.setOrder(order);
        }
    }

    @Override
    public void onClick(View v) {

        finish();

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
        long totalViolationCountTrailing = countViolation(trailingCoachList);
        long totalViolationCount = countViolation(mainTrainCoachList);

        TrainDtoParcelable train = order.getTrain();
        String trainString = makeTrainInfoString(order, train);

        mainTrainInfo.setText(trainString);
        mainTrainStatsTextView.setText(makeStaticString(totalViolationCount, skudoppCount,
                autoDoorsCount));
        trailingStatsTextView.setText(makeStaticString(totalViolationCountTrailing,
                skudoppCountTrailing, autoDoorsCountTrailing));

        mainViolationTextView.setText(makeViolationString(violationList, mainTrainCoachList, order));
        trailingViolationTextView.setText(makeViolationString(violationList, trailingCoachList, order));

    }

    private String makeViolationString(List<Violation> violationList, List<CoachOnRevision> coachList,
                                       OrderDtoParcelable order) {
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
                            .append("\n").append(coach.getCoachWorker()).append("\n");
                    coachString.append(coach.getCoachWorkerDep()).append("\n");
                    if (coach.getViolationList().get(coach.getViolationList()
                            .indexOf(violation)).isResolved()) {
                        coachString.append("Устранено").append("\n");
                    } else {
                        coachString.append("Не устранено").append("\n");
                    }
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
        resultStringBuilder.append("\n");

        return resultStringBuilder.toString();
    }

    private List<ViolationForCoach> getViolationForCoachListByRevisionType(List<Violation> violationList,
                                                                           RevisionType revisionType) {
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

    private String makeStaticString(long violationCount, long doorsCount, long skudoppCount) {

        StringBuilder staticStringResult = new StringBuilder();
        staticStringResult.append("Нарушений: ").append(violationCount).append("\n")
                .append(StatsCoachParams.AUTO_DOOR.getAdditionalParamTitle()).append(":")
                .append(doorsCount).append("\n")
                .append(StatsCoachParams.SKUDOPP.getAdditionalParamTitle()).append(": ")
                .append(skudoppCount).append("\n");

        return staticStringResult.toString();

    }

    private long countViolation(List<CoachOnRevision> list) {

        int total = 0;

        for (CoachOnRevision coach : list) {

            total = total + coach.getViolationList().stream()
                    .mapToInt(ViolationForCoach::getAmount)
                    .sum();
        }

        return total;
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
        String notAvailableString = "Н/Д";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        trainInfoString.append("Уведомление № ")
                .append(order.getNumber())
                .append(" от ")
                .append(order.getDate().format(formatter))
                .append("\n");

        trainInfoString.append("Участок проверки: ")
                .append(order.getRoute())
                .append("\n\n");

        trainInfoString.append(train.getNumber())
                .append(" ")
                .append(train.getRoute())
                .append("\n")
                .append(train.getDepName())
                .append(" ")
                .append(train.getBranchName())
                .append("\n\n");

        appendFeatureInfo(trainInfoString, "Попутчик", train.getHasPortal());
        appendFeatureInfo(trainInfoString, "Видеорегистратор", train.getHasRegistrator());
        appendFeatureInfo(trainInfoString, "Прогресс", train.getHasProgressive());
        trainInfoString.append(makeTempParamsString(order));
        appendAutoinformatorInfo(trainInfoString, train, order, yesString, noString);
        appendQualityPassportInfo(trainInfoString, order, yesString, noString, notAvailableString);
        trainInfoString.append("\n\n").append(makeCrewHeadersString(order.getCrewLeaders())).append("\n");

        return trainInfoString.toString();
    }

    private void appendFeatureInfo(StringBuilder builder, String featureName, int featureValue) {
        String yesString = "ДА";
        String noString = "НЕТ";
        builder.append(featureName).append(": ").append(featureValue == 1 ? yesString : noString).append("\n");
    }

    private void appendAutoinformatorInfo(StringBuilder builder, TrainDtoParcelable train, OrderDtoParcelable order, String yesString, String noString) {
        builder.append("\nАвтоинформатор: ");
        if (train.getHasAutoinformator() == 1) {
            builder.append(yesString);
        } else if (!order.isAutoinformator()) {
            builder.append(noString);
        }
    }

    private void appendQualityPassportInfo(StringBuilder builder, OrderDtoParcelable order, String yesString, String noString, String notAvailableString) {
        builder.append("\nПаспорт качества: ");
        if (order.getIsQualityPassport() != null) {
            builder.append(order.isQualityPassport() ? yesString : noString);
        } else {
            builder.append(notAvailableString);
        }
    }

    private String makeTempParamsString(OrderDtoParcelable order) {

        StringBuilder resultString = new StringBuilder();

        for (String key : order.getTempParams().keySet()) {
            resultString.append(key).append(": ");
            if (order.getTempParams().get(key) == null) {
                resultString.append("N/A\n");
            } else if (!order.getTempParams().get(key)) {
                resultString.append("НЕТ\n");
            } else if (order.getTempParams().get(key)) {
                resultString.append("ДА\n");
            }
        }

        return resultString.toString();
    }

    private String makeCrewHeadersString(Map<String, Worker> crewMap) {
        StringBuilder result = new StringBuilder();

        for (Worker worker : crewMap.values()) {
            result.append(worker.getJobTitle()).append(": ").append(worker.getName()).append("\n");
        }

        return result.toString();

    }
}