package model;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "regularTime")

@NamedQueries(
        {
            @NamedQuery(
                    name  = "getAllRegularTime",
                    query = "select rt from RegularTime as rt"
            )
        }
)

@Entity

public class RegularTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "regular_start", nullable = false)
    private Time regular_start;

    @Column(name = "regular_finish", nullable = false)
    private Time regular_finish;

    @Column(name = "break_start", nullable = false)
    private Time break_start;

    @Column(name = "break_finish", nullable = false)
    private Time break_finish;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Time getRegular_start() {
        return regular_start;
    }

    public void setRegular_start(Time regular_start) {
        this.regular_start = regular_start;
    }

    public Time getRegular_finish() {
        return regular_finish;
    }

    public void setRegular_finish(Time regular_finish) {
        this.regular_finish = regular_finish;
    }

    public Time getBreak_start() {
        return break_start;
    }

    public void setBreak_start(Time break_start) {
        this.break_start = break_start;
    }

    public Time getBreak_finish() {
        return break_finish;
    }

    public void setBreak_finish(Time break_finish) {
        this.break_finish = break_finish;
    }

}
