package com.github.eljaiek.machinery.config.core;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;
import org.eclipse.collections.impl.collector.Collectors2;

@EqualsAndHashCode(of = "key")
@ToString(of = {"key", "value"})
public class ConfigEntry {

    private static final Pattern NUM_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final String READONLY_ERROR = "Entry %s is readonly";

    private final String key;
    private final @With String value;
    private final Consumer<ConfigEntry> save;

    /**
     * Creates a readonly <code>ConfigEntry</code> instance.
     *
     * <p>Readonly entries cannot be saved, hence an <code>UnsupportedOperationException</code> will
     * be thrown on save attempt.
     *
     * @param key corresponding to this configuration entry, never <code>null</code> or blank
     * @param value corresponding to this configuration entry
     * @throws NullPointerException if <code>save</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code> or blank
     */
    public ConfigEntry(String key, String value) {
        this(
                key,
                value,
                x -> {
                    throw new UnsupportedOperationException(format(READONLY_ERROR, x.key));
                });
    }

    /**
     * Creates a <code>ConfigEntry</code> instance.
     *
     * @param key corresponding to this configuration entry, never <code>null</code> or blank
     * @param value corresponding to this configuration entry
     * @param save {@link Consumer} that will accept this configuration entry on save attempt
     * @throws NullPointerException if <code>save</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code> or blank
     */
    public ConfigEntry(String key, String value, @NonNull Consumer<ConfigEntry> save) {

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("key cannot be null or blank.");
        }

        this.key = key;
        this.value = value;
        this.save = save;
    }

    public String key() {
        return key;
    }

    public Optional<String> value() {
        return Optional.ofNullable(value);
    }

    public boolean isNumeric() {
        return value != null && !value.isBlank() && NUM_PATTERN.matcher(value).matches();
    }

    /**
     * Performs save attempt.
     *
     * <p>The given save <code>Consumer</code> is responsible for persisting this configuration
     * entry
     *
     * @throws UnsupportedOperationException if this configuration entry is readonly
     */
    public void save() {
        save.accept(this);
    }

    /**
     * If a value is not present, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if a value is not present, otherwise {@code false}
     */
    @SuppressWarnings("unused")
    public boolean isEmpty() {
        return value().isEmpty();
    }

    /**
     * If a value is present, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if a value is present, otherwise {@code false}
     */
    @SuppressWarnings("unused")
    public boolean isPresent() {
        return value().isPresent();
    }

    public String asText() {
        return value().filter(x -> !x.isBlank()).orElse("");
    }

    public int asInt() {
        return map(Integer::parseInt).orElse(0);
    }

    public <T> Optional<T> map(@NonNull Function<String, T> as) {
        return value().filter(x -> !x.isBlank()).map(as);
    }

    public long asLong() {
        return map(Long::parseLong).orElse(0L);
    }

    public float asFloat() {
        return map(Float::parseFloat).orElse(0.0F);
    }

    public boolean asBoolean() {

        if (isNumeric()) {
            return value().filter(x -> !x.equals("0") && !x.equals("0.0")).isPresent();
        }

        return map(Boolean::parseBoolean).orElse(false);
    }

    public <T> List<T> asList(@NonNull Function<String, T> as) {
        return asList().stream().map(as).collect(toList());
    }

    public List<String> asList() {
        return asList(" ");
    }

    public <T> List<T> asList(@NonNull Function<String, T> as, @NonNull String splitRegex) {
        return asList(splitRegex).stream().map(as).collect(Collectors2.toList());
    }

    public List<String> asList(@NonNull String splitRegex) {
        return value().filter(x -> !x.isBlank())
                .map(val -> List.of(val.split(splitRegex)))
                .orElseGet(List::of);
    }
}
