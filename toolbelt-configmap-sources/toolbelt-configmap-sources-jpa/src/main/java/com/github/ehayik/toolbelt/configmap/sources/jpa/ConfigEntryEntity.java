package com.github.ehayik.toolbelt.configmap.sources.jpa;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PACKAGE)
@Table(name = "ts_configmap")
public class ConfigEntryEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @EqualsAndHashCode.Include
    @Column(name = "ts_configmap_key", nullable = false, length = 500)
    private String key;

    @Basic(optional = false)
    @Column(name = "ts_configmap_value", nullable = false, length = 2500)
    private String value;
}
