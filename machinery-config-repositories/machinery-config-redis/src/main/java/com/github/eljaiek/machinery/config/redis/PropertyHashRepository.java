package com.github.eljaiek.machinery.config.redis;

import java.util.Set;
import org.springframework.data.repository.CrudRepository;

public interface PropertyHashRepository extends CrudRepository<PropertyHash, String> {

  Set<PropertyHash> findAllByKeyStartingWith(String key);

  void deleteAllByKeyStartingWith(String key);
}
