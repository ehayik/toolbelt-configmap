package com.github.eljaiek.machinery.configuration.jpa;

import java.util.Set;
import org.springframework.data.repository.CrudRepository;

public interface PropertyEntityRepository extends CrudRepository<PropertyEntity, String> {

  Set<PropertyEntity> findAllByKeyStartingWith(String key);

  void removeAllByKeyStartingWith(String key);
}
