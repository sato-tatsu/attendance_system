package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "approve")
@NamedQueries(
        {
            @NamedQuery (
                    name = "getAllApprove",
                    query = "select a from Approve as a"
            )
        }
)


@Entity
public class Approve {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "state")
    private String state;

    @Column(name = "normal_action")
    private String normal_action;

    @Column(name = "admin_action")
    private String admin_action;

    @Column(name = "normal_next_state")
    private Integer normal_next_state;

    @Column(name = "admin_next_state")
    private Integer admin_next_state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNormal_action() {
        return normal_action;
    }

    public void setNormal_action(String normal_action) {
        this.normal_action = normal_action;
    }

    public String getAdmin_action() {
        return admin_action;
    }

    public void setAdmin_action(String admin_action) {
        this.admin_action = admin_action;
    }

    public Integer getNormal_next_state() {
        return normal_next_state;
    }

    public void setNormal_next_state(Integer normal_next_state) {
        this.normal_next_state = normal_next_state;
    }

    public Integer getAdmin_next_state() {
        return admin_next_state;
    }

    public void setAdmin_next_state(Integer admin_next_state) {
        this.admin_next_state = admin_next_state;
    }


}
