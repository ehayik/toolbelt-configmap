package com.github.ehayik.toolbelt.configmap.sources.jpa;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PACKAGE)
@Table(name = "ts_configmap")
public class ConfigEntryEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ts_configmap_key", nullable = false, length = 500)
    private String key;

    @Basic(optional = false)
    @Column(name = "ts_configmap_value", nullable = false, length = 2500)
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConfigEntryEntity that = (ConfigEntryEntity) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
