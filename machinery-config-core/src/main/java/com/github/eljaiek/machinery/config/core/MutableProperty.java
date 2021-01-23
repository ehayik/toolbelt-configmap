package com.github.eljaiek.machinery.config.core;

public interface MutableProperty extends Property {

  void save();

  void remove();
}
