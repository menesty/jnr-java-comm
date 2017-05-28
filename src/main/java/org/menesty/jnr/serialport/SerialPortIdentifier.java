package org.menesty.jnr.serialport;

import org.menesty.jnr.serialport.driver.SerialPortDriver;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class SerialPortIdentifier {
  private SerialPortDriver driver;
  private final PortInformation portInformation;

  private SerialPortIdentifier(PortInformation portInformation, SerialPortDriver driver) {
    this.portInformation = portInformation;
    this.driver = driver;
  }

  public SerialPort open(String stringOwner, PortMode portMode) {
    Objects.requireNonNull(portMode, "Port mode can't be null");
    return new SerialPort(driver.openPort(portInformation.getName(), portMode), portMode, driver);
  }

  public String getName() {
    return portInformation.getName();
  }

  public String getDescription() {
    return portInformation.getDescription();
  }

  @Override
  public String toString() {
    return "SerialPortIdentifier{" +
            "portInformation=" + portInformation +
            '}';
  }

  static Enumeration<SerialPortIdentifier> getPortIdentifiers() {
    return new SerialPortIdentifierEnumeration(SerialPortDriver.getInstance());
  }

  public static SerialPortIdentifier getPortIdentifier(String portName) {
    Enumeration<SerialPortIdentifier> portList = getPortIdentifiers();

    while (portList.hasMoreElements()) {
      SerialPortIdentifier identifier = portList.nextElement();
      if (identifier.getName().equals(portName)) {
        return identifier;
      }
    }

    return null;
  }


  private static class SerialPortIdentifierEnumeration implements Enumeration<SerialPortIdentifier> {
    private Iterator<SerialPortIdentifier> iterator;

    private SerialPortIdentifierEnumeration(SerialPortDriver driver) {
      iterator = driver.getAvailablePorts().stream().map(item -> new SerialPortIdentifier(item, driver)).iterator();
    }


    public boolean hasMoreElements() {
      return iterator.hasNext();
    }

    public SerialPortIdentifier nextElement() {
      return iterator.next();
    }
  }

}

