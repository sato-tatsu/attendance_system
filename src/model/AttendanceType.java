package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "attendanceType")

@NamedQueries(
        {
            @NamedQuery(
                    name  = "getAllType",
                    query = "select at from AttendanceType as at"
            )
        }
)

@Entity
public class AttendanceType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "type_str", nullable = false)
    private String type_str;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getType_str() {
        return type_str;
    }

    public void setType_str(String type_str) {
        this.type_str = type_str;
    }
}
