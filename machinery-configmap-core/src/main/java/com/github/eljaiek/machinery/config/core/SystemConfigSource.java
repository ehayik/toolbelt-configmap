package com.github.eljaiek.machinery.config.core;

import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toMap;

/** A ConfigSource for reading and updating {@link System} properties. */
public class SystemConfigSource implements ConfigSource {

    /** {@inheritDoc} */
    @Override
    public String getValue(String key) {
        assertInput(key, "key");
        return System.getProperty(key);
    }

    private static void assertInput(String input, String inputName) {

        if (input == null) throw new IllegalArgumentException(inputName + " can't be null");

        if (input.isBlank()) throw new IllegalArgumentException(inputName + " can't be empty");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> groupBy(String namespace) {
        assertInput(namespace, "namespace");
        return System.getProperties().entrySet().stream()
                .filter(x -> keyStartsWith(x, namespace))
                .collect(toMap(x -> x.getKey().toString(), x -> x.getValue().toString()));
    }

    private static boolean keyStartsWith(Entry<Object, Object> entry, String namespace) {
        return entry.getKey().toString().startsWith(namespace);
    }

    /** {@inheritDoc} */
    @Override
    public void save(String key, String value) {
        assertInput(key, "key");
        System.setProperty(key, value);
    }

    /** {@inheritDoc} */
    @Override
    public void save(Map<String, String> properties) {
        if (properties == null) throw new IllegalArgumentException("configEntries can't be null");

        properties.forEach(this::save);
    }
}
