package com.github.eljaiek.toolbelt.configmap;

import java.util.Map;

/**
 * Implement this interface to provide a ConfigSource.
 *
 * <p>A <code>ConfigSource</code> provides configuration values from a specific place, such as a
 * database, a properties file, etc. A <code>ConfigSource</code> is also responsible for persisting
 * or updating the configured values.
 *
 * <p>The <code>ConfigSource</code> implementations will be use by the {@link ConfigMap} to read and
 * update {@link ConfigEntry} references.
 */
public interface ConfigSource {

    /**
     * Return the value for the specified key in this config source.
     *
     * @param key to get value from, never <code>null</code> nor empty
     * @return the value, or <code>null</code> if no value is found
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code> or empty
     */
    String getValue(String key);

    /**
     * Returns all properties for the specified namespace in this config source.
     *
     * <p>Each property is composed by a unique key and its value.
     *
     * <p>The namespace is a prefix included in each unique key. e.g. [{user.language:US, user.home:
     * /home/tom}] in where <code>user</code> is the namespace.
     *
     * @param namespace to get all properties from, never <code>null</code> nor empty
     * @return the map with all properties found within the specified namespace, never null
     * @throws IllegalArgumentException if <code>namespace</code> is <code>null</code> or empty
     */
    Map<String, String> groupBy(String namespace);

    /**
     * Saves the specified value to this config source.
     *
     * <p>If there is already a value for the specified key in this config source, then the old
     * value must be updated with the specified one.
     *
     * @param key to identify the value in this config source
     * @param value to be saved
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code> or empty
     */
    void save(String key, String value);

    /**
     * Saves all the specified properties to this config source
     *
     * @param properties map containing all properties to be saved, never <code>null</code>
     * @throws IllegalArgumentException if <code>properties</code> is <code>null</code>
     * @throws IllegalArgumentException if any of the specified keys is <code>null</code> or empty
     */
    void save(Map<String, String> properties);
}
