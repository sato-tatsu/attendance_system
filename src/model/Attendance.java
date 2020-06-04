package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Table(name = "attendance")
@NamedQueries(
        {
            @NamedQuery (
                    name = "getMonthAttendance",
                    query = "select a from Attendance as a where a.employee = :employee and a.date between :start and :end"
            ),

            @NamedQuery (
                    name = "getSelectAttendance",
                    query = "select a from Attendance as a where a.employee = :employee and a.date = :select"
            ),

            @NamedQuery (
                    name = "getTotalRegulationTime",
                    query = "select sum(a.regulation_time) from Attendance as a where a.employee = :employee and a.date between :start and :end"
            ),

            @NamedQuery (
                    name = "getTotalOverTime",
                    query = "select sum(a.overtime) from Attendance as a where a.employee = :employee and a.date between :start and :end"
            ),

            @NamedQuery (
                    name = "getTotalAbsenceTime",
                    query = "select sum(a.absence) from Attendance as a where a.employee = :employee and a.date between :start and :end"
            ),

            @NamedQuery (
                    name = "getAllAttendanceByAdminApprove",
                    query = "select a from Attendance as a where (a.approve = :approved or a.approve = :pull_wait) and a.employee.admin_flag <= :admin order by a.updated_at DESC"
            ),

            @NamedQuery (
                    name = "getAttendanceCountSelectEmployeeAndDate",
                    query = "select count(a) from Attendance as a where a.employee = :employee and a.date = :date"
            ),

            @NamedQuery (
                    name = "getAttendanceCountSelectApprove",
                    query = "select count(a) from Attendance as a where a.approve = :approve"
            ),

            @NamedQuery (
                    name = "getAttendanceCountSelectEmployeeAndApprove",
                    query = "select count(a) from Attendance as a where a.employee = :employee and a.approve = :approve"
            )


        }
)

@Entity
public class Attendance {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "date")
    private Date date;

    @Column(name = "begin_time")
    private Time begin_time;

    @Column(name = "finish_time")
    private Time finish_time;

    @Column(name = "type")
    private Integer type;

    @Column(name = "regulation_time")
    private Double regulation_time;

    @Column(name = "absence")
    private Double absence;

    @Column(name = "overtime")
    private Double overtime;

    @Column(name = "approve")
    private Integer approve;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "updated_at")
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Time begin_time) {
        this.begin_time = begin_time;
    }

    public Time getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Time finish_time) {
        this.finish_time = finish_time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getRegulation_time() {
        return regulation_time;
    }

    public void setRegulation_time(Double regulation_time) {
        this.regulation_time = regulation_time;
    }

    public Double getAbsence() {
        return absence;
    }

    public void setAbsence(Double absence) {
        this.absence = absence;
    }

    public Double getOvertime() {
        return overtime;
    }

    public void setOvertime(Double overtime) {
        this.overtime = overtime;
    }

    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

}
