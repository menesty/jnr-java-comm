package org.menesty.jnr.serialport.driver;

import jnr.ffi.util.EnumMapper;

/**
 * Created by Menesty
 * on 5/27/17.
 */
public enum SpEvent implements EnumMapper.IntegerEnum {
  SP_EVENT_RX_READY(0), SP_EVENT_TX_READY(1), SP_EVENT_ERROR(3);
  private final int value;

  SpEvent(int value) {
    this.value = value;
  }

  @Override
  public int intValue() {
    return value;
  }
}
