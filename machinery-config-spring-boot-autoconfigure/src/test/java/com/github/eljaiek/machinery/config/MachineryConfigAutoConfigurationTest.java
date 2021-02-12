package com.github.eljaiek.machinery.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.eljaiek.machinery.config.core.PropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.PropertiesBagFactoryImpl;
import com.github.eljaiek.machinery.config.core.PropertyFactory;
import com.github.eljaiek.machinery.config.core.PropertyFactoryImpl;
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
    assertThat(applicationContext.getBean(PropertiesBagFactory.class))
        .isNotNull()
        .isInstanceOf(PropertiesBagFactoryImpl.class);
  }

  @Test
  void propertyFactoryShouldBePresentWhenContextIsBootstrapped() {
    assertThat(applicationContext.getBean(PropertyFactory.class))
        .isNotNull()
        .isInstanceOf(PropertyFactoryImpl.class);
  }
}
