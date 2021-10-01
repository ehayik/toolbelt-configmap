package com.github.ehayik.toolbelt.configmap.sources.redis;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("mch_config_entries")
@EqualsAndHashCode(of = "key")
public final class ConfigEntryHash implements Serializable {

    private @Id String key;
    private String value;
}
