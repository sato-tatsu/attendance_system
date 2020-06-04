package model.analysis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;

import model.Attendance;
import model.Employee;
import util.DBUtil;

public class AttendanceAnalysis {

    public static Boolean calucAttendance_time(Attendance attendance)
    {
        final Double BASE_WORK_TIME = 8.0;
        final Double START_WORK_PM = 13.0;
        final Double END_WORK_AM = 12.0;

        Boolean check_validate_time_flag = true;

        Double overtime = 0.0;                     // 所定外勤務時間
        Double absence = 0.0;                      // 欠勤時間
        Double break_time = 0.0;                   // 休憩時間

        // 定時時間と勤務時間と休憩時間を文字列として取得
        String r_s_time_str = attendance.getEmployee().getRegular_start().toString();
        String r_f_time_str = attendance.getEmployee().getRegular_finish().toString();
        String s_time_str = attendance.getBegin_time().toString();
        String f_time_str = attendance.getFinish_time().toString();

        // 各要素を分単位に変換
        // 開始定時
        String[] temp_str = r_s_time_str.split(":");
        Double r_s_time = Double.parseDouble(temp_str[0]) * 60 + Integer.parseInt(temp_str[1]);
        // 終了定時
        temp_str = r_f_time_str.split(":");
        Double r_f_time = Double.parseDouble(temp_str[0]) * 60 + Integer.parseInt(temp_str[1]);
        // 開始勤務時間
        temp_str = s_time_str.split(":");
        Double s_time = Double.parseDouble(temp_str[0]) * 60 + Integer.parseInt(temp_str[1]);
        // 終了勤務時間
        temp_str = f_time_str.split(":");
        Double f_time = Double.parseDouble(temp_str[0]) * 60 + Integer.parseInt(temp_str[1]);

        // 1日の総時間を算出
        Double total_time = f_time - s_time;
        if (total_time > BASE_WORK_TIME * 60)
        {// 8時間超過労度
            // 所定休憩時間1時間を追加
            break_time += 60;
        }
        else if (total_time <=  BASE_WORK_TIME * 60 && total_time >= 360)
        {// 6時間～8時間労働
            // 所定休憩時間45分を追加
            break_time += 45;
        }
        // 休憩時間を引いて、総勤務時間を算出
        total_time -= break_time;

        // 勤怠種別を確認
        switch(attendance.getType())
        {
            case 0 :    // 通常
                // 特に処理なし
                break;

            case 1 :    // AM休
                // 定時開始時間を13時に設定
                r_s_time = START_WORK_PM * 60;
                break;

            case 2 :    // PM休
                // 定時終了時間を12時に設定
                r_f_time = END_WORK_AM * 60;
                break;

            case 3 :    // 有給
                // 特に処理なし
                return false;

            case 4 :    // 休日出勤
                // 総勤務時間が所定外勤務時間となる
                attendance.setOvertime(convMinutsToHour(total_time));
                return check_validate_time_flag;

            case 5 :    // 欠勤
                // 基本労働時間が欠勤時間となる
                attendance.setAbsence(BASE_WORK_TIME);
                return false;
            default:
                break;
        }

        // 時間外労働時間解析
        if (r_s_time > s_time)
        {// 定時前出勤
            overtime = overtime + (r_s_time - s_time);
        }

        if (r_f_time < f_time)
        {// 残業
            overtime = overtime + (f_time - r_f_time);
        }

        // 欠勤時間解析
        if (r_s_time < s_time)
        {// 遅刻
            absence = absence + (s_time - r_s_time);
        }

        if (r_f_time > f_time)
        {// 早退
            absence = absence + (r_f_time - f_time);
        }

        // 時間外労働時間と欠勤時間のセット
        attendance.setOvertime(convMinutsToHour(overtime));
        attendance.setAbsence(convMinutsToHour(absence));

        // 所定内時間の取得とセット
        Double regulation_time = total_time - overtime;
        attendance.setRegulation_time(convMinutsToHour(regulation_time));

        return check_validate_time_flag;
    }

    private static Double convMinutsToHour(Double minuts)
    {
        Double hour;

        hour = minuts / 60;
        BigDecimal bd = new BigDecimal(String.valueOf(hour));
        // 小数点第3位を切り捨て
        hour = bd.setScale(2, RoundingMode.DOWN).doubleValue();

        return hour;

    }

    // 有給残日数の更新
    public static void updatePaid(Integer type, Attendance attendance)
    {
        Employee e = attendance.getEmployee();
        Double e_paid = 0.0;
        Double new_paid = 0.0;
        Double old_paid = 0.0;

        // 更新有給状態
        switch(type)
        {
            case 1:
            case 2:
                // 半休
                new_paid = 0.5;
                break;

            case 3:
                // 全休
                new_paid = 1.0;
                break;
            default:
                // 有給日数の更新なし
                break;
        }
        if (attendance.getType() != null)
        {
            // 既存有給状態
            switch(attendance.getType())
            {
                case 1:
                case 2:
                    // 半休
                    old_paid = 0.5;
                    break;

                case 3:
                    // 全休
                    old_paid = 1.0;
                    break;
                default:
                    break;
            }
        }

        // 有給の更新
        e_paid = e.getPaid();
        e.setPaid(e_paid - (new_paid - old_paid));
    }

    // 所定内勤務時間の合計取得
    public static Double getTotalRegularTime(Employee e, Date start, Date end)
    {
        Double time = 0.0;
        EntityManager em = DBUtil.createEntityManager();
        time = em.createNamedQuery("getTotalRegulationTime", Double.class).setParameter("employee", e).setParameter("start", start).setParameter("end", end).getSingleResult();
        if (time == null)
        {
            time = 0.0;
        }
        em.close();

        return time;
    }

    // 所定外勤務時間の合計時間取得
    public static Double getTotalOvertime(Employee e, Date start, Date end)
    {
        Double time = 0.0;
        EntityManager em = DBUtil.createEntityManager();
        time = em.createNamedQuery("getTotalOverTime", Double.class).setParameter("employee", e).setParameter("start", start).setParameter("end", end).getSingleResult();
        if (time == null)
        {
            time = 0.0;
        }
        em.close();

        return time;
    }

    // 欠勤時間の合計時間取得
    public static Double getTotalAbsenceTime(Employee e, Date start, Date end)
    {
        Double time;
        EntityManager em = DBUtil.createEntityManager();
        time = em.createNamedQuery("getTotalAbsenceTime", Double.class).setParameter("employee", e).setParameter("start", start).setParameter("end", end).getSingleResult();
        if (time == null)
        {
            time = 0.0;
        }
        em.close();

        return time;
    }

    // 有給取得日数の合計取得
    public static Double getTotalPaid(List<Attendance> attendance)
    {
        Double paid = 0.0;
        for (Attendance a : attendance)
        {
            switch(a.getType())
            {
                case 1:
                case 2:
                    // 半休
                    paid += 0.5;
                    break;

                case 3:
                    // 全休
                    paid += 1.0;
                    break;
            }
        }

        return paid;
    }

}
