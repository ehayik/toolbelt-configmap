package com.github.ehayik.toolbelt.configmap.sources.redis;

import org.springframework.data.repository.CrudRepository;

public interface ConfigEntryHashRepository extends CrudRepository<ConfigEntryHash, String> {}
