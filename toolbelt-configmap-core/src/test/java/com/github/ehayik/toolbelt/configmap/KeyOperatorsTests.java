package com.github.ehayik.toolbelt.configmap;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class KeyOperatorsTests {

    @Test
    void removeNamespaceShouldThrowNullPointerException() {
        assertThatNullPointerException()
                .isThrownBy(() -> KeyOperators.removeNamespace(null))
                .withMessage("endInclusive is marked non-null but is null");
    }
}
