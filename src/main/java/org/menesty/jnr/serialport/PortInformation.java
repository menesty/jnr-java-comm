package org.menesty.jnr.serialport;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class PortInformation {
  public enum TransportType {
    NATIVE, USB, BLUETOOTH
  }

  private String name;
  private String description;
  private TransportType transportType;
  private String usbManufacturer;
  private String usbProduct;
  private String usbSerial;

  public PortInformation() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TransportType getTransportType() {
    return transportType;
  }

  public void setTransportType(TransportType transportType) {
    this.transportType = transportType;
  }

  public String getUsbManufacturer() {
    return usbManufacturer;
  }

  public void setUsbManufacturer(String usbManufacturer) {
    this.usbManufacturer = usbManufacturer;
  }

  public String getUsbProduct() {
    return usbProduct;
  }

  public void setUsbProduct(String usbProduct) {
    this.usbProduct = usbProduct;
  }

  public String getUsbSerial() {
    return usbSerial;
  }

  public void setUsbSerial(String usbSerial) {
    this.usbSerial = usbSerial;
  }


  @Override
  public String toString() {
    return "PortInformation{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", transportType=" + transportType +
            ", usbManufacturer='" + usbManufacturer + '\'' +
            ", usbProduct='" + usbProduct + '\'' +
            ", usbSerial='" + usbSerial + '\'' +
            '}';
  }
}
