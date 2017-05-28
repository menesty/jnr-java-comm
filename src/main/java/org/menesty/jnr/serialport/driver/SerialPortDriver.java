package org.menesty.jnr.serialport.driver;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.byref.PointerByReference;
import org.menesty.jnr.serialport.PortInformation;
import org.menesty.jnr.serialport.PortMode;
import org.menesty.jnr.serialport.SerialPortConfiguration;
import org.menesty.jnr.serialport.SerialPortEvent;
import org.menesty.jnr.serialport.exception.SerialPortException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class SerialPortDriver {
  private static final String LIBRARY_NAME = "serialport";

  private LibSerialPortStub libSerialPortStub;

  private static SerialPortDriver instance;

  public List<PortInformation> getAvailablePorts() {
    PointerByReference pbr = new PointerByReference();
    SpReturn spReturn = libSerialPortStub.sp_list_ports(pbr);

    Pointer ptr = pbr.getValue();
    List<PortInformation> ports = new ArrayList<>();

    if (SpReturn.SP_OK == spReturn) {
      int offset = 0;
      int addressSize = ptr.getRuntime().addressSize();

      /*need check offset +1 position because address can start from 0*/
      while (ptr.getByte(offset) != 0 || ptr.getByte(offset + 1) != 0) {
        SpPort port = new SpPort(Runtime.getRuntime(libSerialPortStub), ptr.getPointer(offset));

        PortInformation portInformation = new PortInformation();
        portInformation.setName(libSerialPortStub.sp_get_port_name(port));
        portInformation.setDescription(libSerialPortStub.sp_get_port_description(port));
        portInformation.setTransportType(
                PortInformation.TransportType.values()[libSerialPortStub.sp_get_port_transport(port).intValue()]
        );

        if (PortInformation.TransportType.USB == portInformation.getTransportType()) {
          portInformation.setUsbManufacturer(libSerialPortStub.sp_get_port_usb_manufacturer(port));
          portInformation.setUsbProduct(libSerialPortStub.sp_get_port_usb_product(port));
          portInformation.setUsbSerial(libSerialPortStub.sp_get_port_usb_serial(port));
        }

        ports.add(portInformation);
        offset += addressSize;
        libSerialPortStub.sp_free_port(port);
      }
    }
    return ports;
  }

  public static SerialPortDriver getInstance() {
    return instance;
  }

  private void initialize() {
    try {
      libSerialPortStub = LibraryLoader.create(LibSerialPortStub.class).load(LIBRARY_NAME);
    } catch (UnsatisfiedLinkError e) {

    }
  }

  static {
    instance = new SerialPortDriver();
    instance.initialize();
  }

  public SpPort openPort(String name, PortMode portMode) {
    PointerByReference pbr = new PointerByReference();

    checkActionResult(libSerialPortStub.sp_get_port_by_name(name, pbr), "Failed find port by name");

    SpPort port = new SpPort(Runtime.getRuntime(libSerialPortStub), pbr.getValue());

    try {
      checkActionResult(libSerialPortStub.sp_open(port, SpMode.values()[portMode.ordinal()]), "Failed open port");
    } catch (Exception e) {
      libSerialPortStub.sp_free_port(port);
      throw e;
    }

    return port;
  }

  public void closePort(SpPort spPort) {
    libSerialPortStub.sp_close(spPort);
  }

  public void setPortConfiguration(final SpPort spPort, SerialPortConfiguration portConfig) {
/*
    //very often get: No such file or directory , error code: SP_ERR_ARG
    PointerByReference pbr = new PointerByReference();
    checkActionResult(libSerialPortStub.sp_new_config(pbr), "Failed create port configuration");

    SpPortConfig spPortConfig = new SpPortConfig(Runtime.getRuntime(libSerialPortStub), pbr.getValue());

    try {
      checkActionResult(libSerialPortStub.sp_set_config_baudrate(spPortConfig, portConfig.getSpeed()), "Failed set speed to SpPortConfig");
      checkActionResult(libSerialPortStub.sp_set_config_bits(spPortConfig, portConfig.getBits()), "Failed set bits to SpPortConfig");
      checkActionResult(libSerialPortStub.sp_set_config_parity(spPortConfig, SpParity.values()[portConfig.getParity().ordinal()]), "Failed set bits to SpPortConfig");
      checkActionResult(libSerialPortStub.sp_set_config_stopbits(spPortConfig, portConfig.getStopBits()), "Failed set stop bits to SpPortConfig");

      checkActionResult(libSerialPortStub.sp_set_config(spPort, spPortConfig), "Failed set port configuration");
    } finally {
      libSerialPortStub.sp_free_config(spPortConfig);
    }
*/

    setBaudrate(spPort, portConfig.getSpeed());
    setBits(spPort, portConfig.getBits());
    setParity(spPort, portConfig.getParity());
    setSopBits(spPort, portConfig.getStopBits());

  }

  public void setBaudrate(SpPort spPort, int baudrate) {
    checkActionResult(libSerialPortStub.sp_set_baudrate(spPort, baudrate), "Failed set port configuration speed");
  }

  public void setBits(SpPort spPort, int bits) {
    checkActionResult(libSerialPortStub.sp_set_bits(spPort, bits), "Failed set port configuration bits");
  }

  public void setParity(SpPort spPort, SerialPortConfiguration.Parity parity) {
    checkActionResult(libSerialPortStub.sp_set_parity(spPort, SpParity.values()[parity.ordinal()]), "Failed set port configuration parity");
  }

  public void setSopBits(SpPort spPort, int sopBits) {
    checkActionResult(libSerialPortStub.sp_set_stopbits(spPort, sopBits), "Failed set port configuration stop bits");
  }

  private boolean checkActionResult(SpReturn spReturn, String errorMessage) {
    if (SpReturn.SP_OK == spReturn) {
      return true;
    }

    throw new SerialPortException(errorMessage, libSerialPortStub.sp_last_error_message(), spReturn.name());
  }

  private int checkActionResult(int spReturn, String errorMessage) {
    if (spReturn >= 0) {
      return spReturn;
    }

    checkActionResult(SpReturn.getByValue(spReturn), errorMessage);
    return 0;
  }

  private int checkActionResultThrowIOException(int spReturn, String errorMessage) throws IOException {
    try {
      return checkActionResult(spReturn, errorMessage);
    } catch (SerialPortException e) {
      throw new IOException(e);
    }
  }

  public int getByteReadyInput(SpPort spPort) throws IOException {
    return checkActionResultThrowIOException(libSerialPortStub.sp_input_waiting(spPort), "Failed get number of bytes waiting for read");
  }

  public Pointer addEventListener(SpPort spPort, SerialPortEvent readReady) {
    PointerByReference pbr = new PointerByReference();
    checkActionResult(libSerialPortStub.sp_new_event_set(pbr), "Failed create event set");
    Pointer eventSetPointer = pbr.getValue();

    try {
      checkActionResult(libSerialPortStub.sp_add_port_events(eventSetPointer, spPort, SpEvent.values()[readReady.ordinal()]), "Failed add event set to port");
    } catch (SerialPortException e) {
      libSerialPortStub.sp_free_event_set(eventSetPointer);
      throw e;
    }

    return eventSetPointer;
  }

  public void freeEventSet(Pointer eventSetPointer) {
    libSerialPortStub.sp_free_event_set(eventSetPointer);
  }

  /**
   * @return The number of bytes read on success, or throw IOException.
   * The number of bytes returned may be any number from zero to the maximum that was requested.
   **/
  public int nonBlockingRead(SpPort spPort, byte[] bytes, int length) throws IOException {
    return checkActionResultThrowIOException(libSerialPortStub.sp_nonblocking_read(spPort, bytes, length), "Failed read bytes from port");
  }

  public void waitForPortData(Pointer eventSet, int readTimeOut) {
    checkActionResult(libSerialPortStub.sp_wait(eventSet, readTimeOut), "Failed read bytes from port");
  }

  /**
   * @return The number of bytes written on success, or throw IOException.
   * The number of bytes returned may be any number from zero to the maximum that was requested.
   **/
  public int nonBlockingWrite(SpPort spPort, byte[] buff, int count) throws IOException {
    return checkActionResultThrowIOException(libSerialPortStub.sp_nonblocking_write(spPort, buff, count), "Failed read bytes from port");
  }

  /**
   * @return Number of bytes waiting on success, or throw IOException.
   **/
  public int getWatingForWriteBytes(SpPort spPort) throws IOException {
    return checkActionResultThrowIOException(libSerialPortStub.sp_output_waiting(spPort), "Failed get number of bytes waiting");
  }

  public void waitWriteFinish(Pointer eventSet, long writeTimeOut) {
    checkActionResult(libSerialPortStub.sp_wait(eventSet, (int) writeTimeOut), "Failed to wait data write");
  }
}
