package org.menesty.jnr.serialport;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class SerialPortConfiguration {
  public enum Parity {
    INVALID, NONE, ODD, EVEN, MARK, SPACE
  }

  private final int speed;
  private final int bits;
  private final int stopBits;
  private final Parity parity;

  public SerialPortConfiguration(int speed, int bits, int stopBits, Parity parity) {
    this.speed = speed;
    this.bits = bits;
    this.stopBits = stopBits;
    this.parity = parity;
  }

  public int getSpeed() {
    return speed;
  }

  public int getBits() {
    return bits;
  }

  public int getStopBits() {
    return stopBits;
  }

  public Parity getParity() {
    return parity;
  }
}
