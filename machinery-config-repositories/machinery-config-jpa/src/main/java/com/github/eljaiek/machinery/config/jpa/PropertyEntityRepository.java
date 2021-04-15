package com.github.eljaiek.machinery.config.jpa;

import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PropertyEntityRepository extends CrudRepository<PropertyEntity, String> {

    @Query("select p.value from PropertyEntity p where p.key = :key")
    String getValue(@Param("key") String key);

    Set<PropertyEntity> findAllByKeyStartingWith(String key);

    void deleteAllByKeyStartingWith(String key);
}
