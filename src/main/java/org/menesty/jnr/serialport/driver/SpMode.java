package org.menesty.jnr.serialport.driver;

import jnr.ffi.util.EnumMapper;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public enum SpMode implements EnumMapper.IntegerEnum {
  SP_MODE_READ(1), SP_MODE_WRITE(2), SP_MODE_READ_WRITE(3);

  private final int value;

  SpMode(int value) {
    this.value = value;
  }

  @Override
  public int intValue() {
    return value;
  }
}
