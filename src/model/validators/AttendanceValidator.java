package model.validators;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class AttendanceValidator {

    public static List<String> validator(Time start, Time end, Double rema_paid, Boolean check_validate_time_flag )
    {
        List<String> errors = new ArrayList<String>();
        String error = null;

        // 入力時間のチェック
        error = validateTime(start, end);
        if (error != null && check_validate_time_flag == true)
        {
            errors.add(error);
        }

        // 有給取得のチェック
        error = validatePaid(rema_paid);
        if (error != null)
        {
            errors.add(error);
        }

        return errors;
    }

    private static String validateTime(Time start, Time end)
    {
        String error = null;

        String startTime_str = start.toString();
        String endTime_str   = end.toString();

        String[] temp = startTime_str.split(":");
        Integer startTime = Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]);
        temp = endTime_str.split(":");
        Integer endTime = Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]);

        Integer check_time = endTime - startTime;
        if (check_time == 0)
        {
            error = "出勤時間と退勤時間が同じです。";
        }
        else if (check_time < 0)
        {
            error = "出勤時間が退勤時間より遅いです。";
        }

        return error;
    }

    private static String validatePaid(Double rema_paid)
    {
        String error = null;

        if (rema_paid < 0)
        {
            error = "残有給日数が少ないため、有給が取得できません。";
        }

        return error;
    }

}
