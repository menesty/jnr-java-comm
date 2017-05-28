package org.menesty.jnr.serialport.driver;

import jnr.ffi.util.EnumMapper;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public enum SpParity implements EnumMapper.IntegerEnum {
  SP_PARITY_INVALID(0), SP_PARITY_NONE(1), SP_PARITY_ODD(2), SP_PARITY_EVEN(3), SP_PARITY_MARK(4), SP_PARITY_SPACE(5);

  private final int value;

  SpParity(int value) {
    this.value = value;
  }

  @Override
  public int intValue() {
    return value;
  }
}
