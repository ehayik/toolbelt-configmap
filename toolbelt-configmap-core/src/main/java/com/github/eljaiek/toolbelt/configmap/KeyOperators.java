package com.github.eljaiek.toolbelt.configmap;

import static lombok.AccessLevel.PRIVATE;

import java.util.function.BinaryOperator;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = PRIVATE)
public class KeyOperators {

    public static BinaryOperator<String> key() {
        return (prefix, key) -> key;
    }

    public static BinaryOperator<String> removePrefix() {
        return removePrefix("");
    }

    public static BinaryOperator<String> removePrefix(@NonNull String endInclusive) {
        return (prefix, key) -> key.replace(prefix + endInclusive, "");
    }
}
