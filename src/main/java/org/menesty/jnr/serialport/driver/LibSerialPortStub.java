package org.menesty.jnr.serialport.driver;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.PointerByReference;
import jnr.ffi.types.size_t;
import jnr.ffi.types.u_int16_t;
import jnr.ffi.types.u_int8_t;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public interface LibSerialPortStub {
  SpReturn sp_list_ports(@Out PointerByReference referencePointerStruct);

  String sp_get_port_name(SpPort port);

  String sp_get_port_description(SpPort port);

  SpTransport sp_get_port_transport(SpPort port);

  String sp_get_port_bluetooth_address(SpPort port);

  SpReturn sp_get_port_by_name(String portName, @Out PointerByReference referencePointerStruct);

  SpReturn sp_open(SpPort port, SpMode mode);

  SpReturn sp_close(SpPort port);

  SpReturn sp_new_config(@Out PointerByReference referencePointerStruct);

  void sp_free_config(SpPortConfig spConfig);

  void sp_free_port(SpPort port);

  SpReturn sp_set_baudrate(SpPort port, int baudrate);

  SpReturn sp_set_bits(SpPort port, int bits);

  SpReturn sp_set_parity(SpPort port, SpParity spParity);

  SpReturn sp_set_stopbits(SpPort port, int stopBits);

  String sp_get_port_usb_manufacturer(SpPort port);

  String sp_get_port_usb_product(SpPort port);

  String sp_get_port_usb_serial(SpPort port);

  int sp_input_waiting(SpPort port);

  SpReturn sp_new_event_set(@Out PointerByReference referencePointerStruct);

  void sp_free_event_set(Pointer eventSet);

  SpReturn sp_add_port_events(Pointer eventSet, SpPort port, SpEvent mask);

  int sp_nonblocking_read(SpPort port, byte[] buff, @size_t int count);

  SpReturn sp_wait(Pointer eventSet, @u_int8_t int timeout_ms);

  int sp_nonblocking_write(SpPort port, byte[] buff, @size_t int count);

  int sp_output_waiting(SpPort port);

  String sp_last_error_message();

  SpReturn sp_set_config(SpPort spPort, @In SpPortConfig spPortConfig);

  SpReturn sp_set_config_baudrate(SpPortConfig spPortConfig, @u_int8_t int speed);

  SpReturn sp_set_config_bits(SpPortConfig spPortConfig, int bits);

  SpReturn sp_set_config_parity(SpPortConfig spPortConfig, SpParity spParity);

  SpReturn sp_set_config_stopbits(SpPortConfig spPortConfig, int stopBits);
}
