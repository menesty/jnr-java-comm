package org.menesty.jnr.serialport;

import org.junit.Test;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class TestSerialPort {
  public static final int PORT_CONNECTION_TIMEOUT = 2000;
  public static final int PORT_INPUT_TIMEOUT = 1000;
  public static final int PORT_SPEED = 9600;
  public static final int PORT_DATA_BITS = 8;
  public static final int PORT_STOP_BITS = 1;
  public static final SerialPortConfiguration.Parity PORT_PARITY = SerialPortConfiguration.Parity.NONE;

  @Test
  public void printSerialPortList() {
    Enumeration<SerialPortIdentifier> ports = SerialPortIdentifier.getPortIdentifiers();

    while (ports.hasMoreElements()) {
      SerialPortIdentifier portIdentifier = ports.nextElement();
      System.out.println(portIdentifier);
    }
  }


  //@Test
  public void connectToSerialPort() throws IOException {
    String portName = "/dev/cu.usbserial-00002014A";

    SerialPortIdentifier portIdentifier = SerialPortIdentifier.getPortIdentifier(portName);

    if (portIdentifier != null) {
      SerialPort serialPort = portIdentifier.open("Owner", PortMode.READ_WRITE);

      serialPort.setPortParams(PORT_SPEED, PORT_DATA_BITS, PORT_STOP_BITS, PORT_PARITY);
      serialPort.close();

      System.out.println("done");
    }

  }
}
