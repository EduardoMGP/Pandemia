package org.pandemia.info;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class Utils {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar nowCalendar = Calendar.getInstance();
    private static final String nowDate = formatDate(nowCalendar);

    public static Date now() {
        return nowCalendar.getTime();
    }

    public static String formatDate(Calendar calendar) {
        return formatDate(calendar.getTime());
    }

    public static String formatDate(Date date) {
        return formatter.format(date);
    }

    public static String formatNowDate() {
        return nowDate;
    }

    public static Calendar nowIncremented(int days) {
        Calendar calendar1 = (Calendar) nowCalendar.clone();
        calendar1.add(Calendar.DAY_OF_MONTH, days);
        return calendar1;
    }

    public static Calendar nowDecremented(int days) {
        Calendar calendar1 = (Calendar) nowCalendar.clone();
        calendar1.add(Calendar.DAY_OF_MONTH, -days);
        return calendar1;
    }

    public static String nowIncrementedFormat(int days) {
        return formatDate(nowIncremented(days));
    }

    public static String nowDecrementedFormat(int days) {
        return formatDate(nowDecremented(days));
    }


    public static Optional<ButtonType> showAlert(String title, String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    public static void showConfirm(String message, AlertExec exec) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK)) {
                exec.execute();
            }
        });
    }
}
