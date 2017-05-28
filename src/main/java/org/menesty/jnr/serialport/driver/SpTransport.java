package org.menesty.jnr.serialport.driver;

import jnr.ffi.util.EnumMapper;

/**
 * Created by Menesty
 * on 5/24/17.
 */
public enum SpTransport implements EnumMapper.IntegerEnum {
  SP_TRANSPORT_NATIVE(0), SP_TRANSPORT_USB(1), SP_TRANSPORT_BLUETOOTH(2);
  private final int value;

  SpTransport(int value) {
    this.value = value;
  }

  @Override
  public int intValue() {
    return value;
  }
}
