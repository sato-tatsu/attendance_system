package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Table(name = "admin")

@NamedQueries(
        {
            @NamedQuery(
                    name  = "getAllAdmin",
                    query = "select a from Admin as a"
            ),

            @NamedQuery(
                    name  = "getAdminCount",
                    query = "select count(a) from Admin as a"
            ),

            @NamedQuery(
                    name  = "getAdminSelectStrong",
                    query = "select a from Admin as a where a.strong = :strong"
            )
        }
)

@Entity
public class Admin {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "admin_str")
    private String admin_str;

    @Column(name = "strong")
    private Integer strong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdmin_str() {
        return admin_str;
    }

    public void setAdmin_str(String admin_str) {
        this.admin_str = admin_str;
    }

    public Integer getStrong() {
        return strong;
    }

    public void setStrong(Integer strong) {
        this.strong = strong;
    }
}
