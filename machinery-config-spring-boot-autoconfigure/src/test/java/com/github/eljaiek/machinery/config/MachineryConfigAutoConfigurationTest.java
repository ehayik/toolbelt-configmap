package com.github.eljaiek.machinery.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.eljaiek.machinery.config.core.MutablePropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.MutablePropertiesBagFactoryImpl;
import com.github.eljaiek.machinery.config.core.MutablePropertyFactory;
import com.github.eljaiek.machinery.config.core.MutablePropertyFactoryImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@ContextConfiguration(classes = MachineryConfigAutoConfiguration.class)
class MachineryConfigAutoConfigurationTest {

  @Autowired ApplicationContext applicationContext;

  @Test
  void propertiesBagFactoryShouldBePresentWhenContextIsBootstrapped() {
    assertThat(applicationContext.getBean(MutablePropertiesBagFactory.class))
        .isNotNull()
        .isInstanceOf(MutablePropertiesBagFactoryImpl.class);
  }

  @Test
  void propertyFactoryShouldBePresentWhenContextIsBootstrapped() {
    assertThat(applicationContext.getBean(MutablePropertyFactory.class))
        .isNotNull()
        .isInstanceOf(MutablePropertyFactoryImpl.class);
  }
}
