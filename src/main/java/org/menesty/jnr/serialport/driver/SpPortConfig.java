package org.menesty.jnr.serialport.driver;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

/**
 * Created by Menesty
 * on 5/28/17.
 */
public class SpPortConfig extends Struct {
  protected SpPortConfig(Runtime runtime, jnr.ffi.Pointer pointer) {
    super(runtime);
    useMemory(pointer);
  }
}
