package com.github.ehayik.toolbelt.configmap;

import static lombok.AccessLevel.PRIVATE;

import java.util.function.BinaryOperator;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = PRIVATE)
public class KeyOperators {

    public static BinaryOperator<String> key() {
        return (prefix, key) -> key;
    }

    public static BinaryOperator<String> removeNamespace(@NonNull String endInclusive) {
        return (namespace, key) -> key.replace(namespace + endInclusive, "");
    }
}
