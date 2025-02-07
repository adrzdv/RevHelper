package com.example.revhelper.services;

import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.ViolationAttribute;
import com.example.revhelper.model.dto.ViolationForCoach;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for parsing xls-file with violations for making reinspection process
 */
public class ParseTable {
    private static String docNumber;
    private static String docDate;

    public static String getDocNumber() {
        return docNumber;
    }

    public static String getDocDate() {
        return docDate;
    }

    /**
     * Read xls-file and make result in Map
     *
     * @param inputStream input stream for reading
     * @return Map of CoachOnRevision objects
     */
    public static Map<String, CoachOnRevision> readExcel(InputStream inputStream) {

        Map<String, CoachOnRevision> prevInspectionCoachMap = new HashMap<>();

        try {

            Workbook workbook;

            try {
                workbook = new XSSFWorkbook(inputStream);
            } catch (OfficeXmlFileException e) {
                inputStream.reset();
                workbook = new HSSFWorkbook(inputStream);
            }

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getCell(0) != null) {
                    docDate = getCellValue(row, 0);
                    docNumber = getCellValue(row, 1);
                    String violationString = getCellValue(row, 2);
                    String attrib = getCellValue(row, 3);
                    String resolved = getCellValue(row, 4);
                    String coachNumber = getCellValue(row, 5);
                    int amount = parseIntSafe(getCellValue(row, 6), 1);

                    CoachOnRevision coach = prevInspectionCoachMap.getOrDefault(coachNumber,
                            new CoachOnRevision.Builder()
                                    .setCoachNumber(coachNumber)
                                    .setRevisionTime(LocalDateTime.now())
                                    .setRevisionEndTime(LocalDateTime.now())
                                    .setViolationList(new ArrayList<>())
                                    .build()
                    );

                    ViolationForCoach violation = parseViolation(violationString, amount, resolved);
                    updateCoachViolations(coach, violation, attrib);

                    prevInspectionCoachMap.put(coachNumber, coach);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return prevInspectionCoachMap;
    }

    /**
     * Method for reading values in cell
     *
     * @param row       row object
     * @param cellIndex index of cell
     * @return String of value
     */
    private static String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return (cell != null) ? cell.toString().trim() : "";
    }

    /**
     * Method for safety parsing integer values
     *
     * @param value        String value
     * @param defaultValue default value for result
     * @return String value parsed in integer
     */
    private static int parseIntSafe(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Method for parsing values in ViolationForCoach objects
     *
     * @param violationString String value from cell
     * @param amount          amount values if violation
     * @param resolved        resolved status
     * @return ViolationFroCoach object
     */
    private static ViolationForCoach parseViolation(String violationString, int amount, String resolved) {
        String[] violationParts = violationString.split(" ", 2);
        int code = Integer.parseInt(violationParts[0].substring(violationParts[0].indexOf(".") + 1));

        String description = violationParts.length > 1 ? violationParts[1] : "";

        ViolationForCoach violation = new ViolationForCoach(code, code, description, 0, amount);
        violation.setResolved(resolved.equalsIgnoreCase("да"));
        return violation;
    }

    /**
     * Method for making attributes for violations
     *
     * @param coach     CoachOnRevision object
     * @param violation ViolationForCoach object, where need to add attribute
     * @param attrib    attribute String value
     */
    private static void updateCoachViolations(CoachOnRevision coach, ViolationForCoach violation, String attrib) {
        if (!coach.getViolationList().contains(violation)) {
            ViolationAttribute attribute = new ViolationAttribute(attrib, 1);
            if (!attrib.isBlank()) {
                violation.getAttributes().add(attribute);
            }
            coach.getViolationList().add(violation);
        } else if (!attrib.isBlank()) {
            ViolationAttribute attribute = new ViolationAttribute(attrib, 1);
            for (ViolationForCoach viol : coach.getViolationList()) {
                if (viol.equals(violation)) {
                    viol.getAttributes().add(attribute);
                }
            }
        }
    }

}
