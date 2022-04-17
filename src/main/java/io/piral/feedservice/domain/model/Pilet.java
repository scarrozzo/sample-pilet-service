package io.piral.feedservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PILET")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Pilet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @ToString.Include
    private String name;

    private String description;

    @Column(name = "PACKAGE_VERSION")
    private String packageVersion;

    @Column(name = "REQUIRE_REF")
    private String requireRef;

    private String integrity;

    private String author;

    private String dependencies;

    @Column(name = "LICENSE_TYPE")
    private String licenseType;

    @Column(name = "LICENSE_TEXT")
    private String licenseText;

    private String type;

    private String appshell;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pilet_id")
    private List<File> files;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Pilet pilet = (Pilet) o;
        return getName() != null && Objects.equals(getName(), pilet.getName());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
