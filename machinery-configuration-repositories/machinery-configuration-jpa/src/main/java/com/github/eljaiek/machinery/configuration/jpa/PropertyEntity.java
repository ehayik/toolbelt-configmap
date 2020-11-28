package com.github.eljaiek.machinery.configuration.jpa;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PACKAGE)
@EqualsAndHashCode(of = "key")
@Table(name = "mch_properties")
class PropertyEntity implements Serializable {

  @Id
  @Basic(optional = false)
  @Column(name = "mch_property_key", nullable = false, length = 500)
  private String key;

  @Basic(optional = false)
  @Column(name = "mch_property_value", nullable = false, length = 2500)
  private String value;
}
