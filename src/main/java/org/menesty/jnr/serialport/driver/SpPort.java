package org.menesty.jnr.serialport.driver;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

/**
 * Created by Menesty
 * on 5/24/17.
 */
public class SpPort extends Struct {
  SpPort(Runtime runtime, jnr.ffi.Pointer pointer) {
    super(runtime);
    useMemory(pointer);
  }
}