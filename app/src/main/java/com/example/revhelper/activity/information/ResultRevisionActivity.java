package com.example.revhelper.activity.information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityResultRevisionBinding;
import com.example.revhelper.model.dto.RevCoach;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.model.dto.ViolationAttribute;
import com.example.revhelper.model.dto.ViolationForCoach;
import com.example.revhelper.model.dto.Worker;
import com.example.revhelper.model.enums.StatsCoachParams;
import com.example.revhelper.sys.SharedViewModel;

import java.time.format.DateTimeFormatter;
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
        binding.revisionGenerateReportBttn.setOnClickListener(this);
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

        if (v.getId() == R.id.revision_end_revision) {
            finish();
        } else if (v.getId() == R.id.revision_generate_report_bttn) {
            generateReport();
        }

    }

    private void generateReport() {
        StringBuilder result = new StringBuilder();
        Map<String, Integer> resMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        result.append(order.getDate().format(formatter)).append("\n").append(order.getTrain().getNumber())
                .append(" ").append(order.getTrain().getRoute()).append("\n");

        for (RevCoach coach : order.getCoachMap().values()) {
            for (ViolationForCoach violation : coach.getViolationList()) {
                resMap.merge(violation.getName(), violation.getAmount(), Integer::sum);
            }
        }

        for (String key : resMap.keySet()) {
            result.append(resMap.get(key)).append("-").append(key).append("\n");
        }

        shareReport(result.toString());

    }

    private void shareReport(String report) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, report);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Выберите приложение");
        startActivity(shareIntent);
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

        Map<String, RevCoach> coachMap = order.getCoachMap();
        List<RevCoach> mainTrainCoachList = coachMap.values().stream()
                .filter(coach -> !coach.isTrailingCar())
                .collect(Collectors.toList());

        List<RevCoach> trailingCoachList = coachMap.values().stream()
                .filter(RevCoach::isTrailingCar)
                .collect(Collectors.toList());

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

        mainViolationTextView.setText(makeResultString(mainTrainCoachList));
        trailingViolationTextView.setText(makeResultString(trailingCoachList));

    }

    private String makeResultString(List<RevCoach> coachList) {

        StringBuilder resultStringBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (RevCoach coach : coachList) {
            resultStringBuilder.append(coach.getCoachNumber()).append("\n")
                    .append(coach.getCoachWorker()).append("\n")
                    .append(coach.getCoachWorkerDep()).append("\n")
                    .append(coach.getRevisionTime().format(formatter)).append("\n");
            for (ViolationForCoach violation : coach.getViolationList()) {
                resultStringBuilder.append('-').append(violation.getName()).append("\n");
                if (violation.isResolved()) {
                    resultStringBuilder.append("(устранено)").append("\n");
                } else {
                    resultStringBuilder.append("(не устранено)").append("\n");
                }
                if (!violation.getAttributes().isEmpty()) {
                    resultStringBuilder.append(violation.getAttributes().stream()
                            .map(ViolationAttribute::getAttrib)
                            .collect(Collectors.toList())).append("\n");
                }
            }
            resultStringBuilder.append("\n");
        }

        return resultStringBuilder.append("\n").toString();
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

    private long countViolation(List<RevCoach> list) {

        int total = 0;

        for (RevCoach coach : list) {

            total = total + coach.getViolationList().stream()
                    .mapToInt(ViolationForCoach::getAmount)
                    .sum();
        }

        return total;
    }

    private long countSkudopp(List<RevCoach> list) {
        return list.stream()
                .filter(RevCoach::isCoachSkudopp)
                .count();
    }

    private long countAutodoors(List<RevCoach> list) {
        return list.stream()
                .filter(RevCoach::isCoachAutomaticDoor)
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

        //hide, maybe in future can be use
        //appendFeatureInfo(trainInfoString, "Попутчик", train.getHasPortal());
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

    private void appendAutoinformatorInfo(StringBuilder builder, TrainDtoParcelable train,
                                          OrderDtoParcelable order, String yesString,
                                          String noString) {
        builder.append("\nАвтоинформатор: ");
        if (train.getHasAutoinformator() == 1) {
            builder.append(yesString);
        } else if (!order.isAutoinformator()) {
            builder.append(noString);
        }
    }

    private void appendQualityPassportInfo(StringBuilder builder, OrderDtoParcelable order,
                                           String yesString, String noString,
                                           String notAvailableString) {
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