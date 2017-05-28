package org.menesty.jnr.serialport.driver;

import jnr.ffi.util.EnumMapper;

import java.util.stream.Stream;

/**
 * Created by Menesty
 * on 5/24/17.
 */
public enum SpReturn implements EnumMapper.IntegerEnum {
  SP_OK(0), SP_ERR_ARG(-1), SP_ERR_FAIL(-2), SP_ERR_MEM(-3), SP_ERR_SUPP(-4);

  private final int value;

  SpReturn(int value) {
    this.value = value;
  }

  @Override
  public int intValue() {
    return value;
  }

  public static SpReturn getByValue(int operationResult) {
    return Stream.of(SpReturn.values()).filter(item -> item.intValue() == operationResult).findFirst().orElse(null);
  }
}
