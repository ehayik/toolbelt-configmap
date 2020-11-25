package com.github.eljaiek.machinery.configuration.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "key")
@Entity(name = "machinery_properties")
class PropertyEntity implements Serializable {

  @Id
  @Basic(optional = false)
  @Column(name = "machinery_property_key", nullable = false, length = 500)
  private String key;

  @Basic(optional = false)
  @Column(name = "machinery_property_key", nullable = false, length = 2500)
  private String value;
}
