package model.struct;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import model.Attendance;

public class CalendarStruct {
    private Date select_date;
    private Attendance select_attendance;
    private Date next_month;
    private Date prev_month;
    private Date[][] calendar_date;
    private Attendance[][] attendance;

    // コンストラクタ
    public CalendarStruct()
    {

    }

    public CalendarStruct(Calendar calendar)
    {
        this.select_date = new Date(calendar.getTime().getTime());
        setNextAndPrevInfo(calendar);
    }

    public Date getSelect_date() {
        return select_date;
    }
    public void setSelect_date(Date select_date) {
        this.select_date = select_date;
    }
    public Attendance getSelect_attendance() {
        return select_attendance;
    }
    public void setSelect_attendance(Attendance select_attendance) {
        this.select_attendance = select_attendance;
    }
    public Date getNext_month() {
        return next_month;
    }
    public void setNext_month(Date next_month) {
        this.next_month = next_month;
    }
    public Date getPrev_month() {
        return prev_month;
    }
    public void setPrev_month(Date prev_month) {
        this.prev_month = prev_month;
    }
    public Date[][] getDate() {
        return calendar_date;
    }
    public void setDate(Date[][] calendar_date) {
        this.calendar_date = calendar_date;
    }
    public Attendance[][] getAttendance() {
        return attendance;
    }
    public void setAttendance(Attendance[][] attendance) {
        this.attendance = attendance;
    }

    // 先月と来月の年月を設定
    public void setNextAndPrevInfo(Calendar calendar)
    {
        Calendar next_c = (Calendar)calendar.clone();
        Calendar prev_c = (Calendar)calendar.clone();

        next_c.add(Calendar.MONTH, 1);
        next_c.set(Calendar.DATE, 1);   // 1日しておく

        prev_c.add(Calendar.MONTH, -1);
        prev_c.set(Calendar.DATE, 1);   // 1日しておく

        this.next_month = new Date(next_c.getTime().getTime());
        this.prev_month = new Date(prev_c.getTime().getTime());
    }

    // カレンダーデータの作成
    public void createCalenderData(Calendar calendar, List<Attendance> attemdance)
    {
        final Integer CALENDER_COL = 6; // カレンダー縦
        final Integer CALENDER_ROW = 7; // カレンダー横

        Integer week;
        Integer max_date = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        this.calendar_date = new Date[CALENDER_COL][CALENDER_ROW];
        this.attendance    = new Attendance[CALENDER_COL][CALENDER_ROW];

        // 月のスタート位置(曜日)を取得
        calendar.set(Calendar.DATE, 1);
        week = calendar.get(Calendar.DAY_OF_WEEK);

        Integer day_count = 1;

        // その月の日付を上記で取得した開始位置から配列に格納
        for (int i = 0; i < CALENDER_COL; i++)
        {
            for (int j = week-1; j < CALENDER_ROW; j++)
            {
                this.calendar_date[i][j] = new Date(calendar.getTime().getTime());
                for (Attendance a : attemdance)
                {// 勤怠データのリスト分ループ
                    if (a.getDate().toString().equals(calendar_date[i][j].toString()))
                    {
                        // 日付とマッチングしたデータがあった場合に格納
                        attendance[i][j] = a;
                    }
                }
                day_count++;
                if (day_count > max_date)
                {
                    // 日付終了のため格納処理終了
                    return;
                }
                calendar.add(Calendar.DATE, 1);
            }
            week = 1;
        }

    }


    // 指定月の初日を取得
    public Date getFirstDate(Calendar calendar)
    {
        Calendar c = calendar;

        c.set(Calendar.DATE, 1);
        Date d = new Date(c.getTime().getTime());

        return d;
    }

    // 指定月の最終日を取得
    public Date getMonthLastDate(Calendar calendar)
    {
        Calendar c = calendar;

        Integer last_date = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DATE, last_date);
        Date d = new Date(c.getTime().getTime());

        return d;
    }
}

