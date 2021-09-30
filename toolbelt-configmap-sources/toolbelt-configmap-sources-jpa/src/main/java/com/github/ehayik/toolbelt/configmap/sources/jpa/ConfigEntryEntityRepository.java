package com.github.ehayik.toolbelt.configmap.sources.jpa;

import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ConfigEntryEntityRepository extends CrudRepository<ConfigEntryEntity, String> {

    @Query("select p.value from ConfigEntryEntity p where p.key = :key")
    String getValue(@Param("key") String key);

    Set<ConfigEntryEntity> findAllByKeyStartingWith(String key);
}
