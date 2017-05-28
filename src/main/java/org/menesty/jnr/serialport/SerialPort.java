package org.menesty.jnr.serialport;

import jnr.ffi.Pointer;
import org.menesty.jnr.serialport.driver.SerialPortDriver;
import org.menesty.jnr.serialport.driver.SpPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class SerialPort {
  private SerialPortDriver driver;
  private SpPort spPort;
  private SerialPortConfiguration portConfiguration;
  private int readTimeOut = 0;
  private long writeTimeOut = TimeUnit.SECONDS.toMillis(5);

  private final SerialInputStream serialInputStream;
  private final SerialOutputStream serialOutputStream;

  SerialPort(SpPort spPort, SerialPortDriver driver) {
    this.spPort = spPort;
    this.driver = driver;
    serialInputStream = new SerialInputStream();
    serialOutputStream = new SerialOutputStream();
  }

  public void enableReceiveTimeout(int readTimeOut) {
    this.readTimeOut = readTimeOut;
  }

  public InputStream getInputStream() {
    return serialInputStream;
  }

  public OutputStream getOutputStream() {
    return serialOutputStream;
  }

  public void close() {
    driver.freeEventSet(serialInputStream.eventSet);
    driver.freeEventSet(serialOutputStream.eventSet);
    driver.closePort(spPort);
  }

  public void setPortParams(int speed, int bits, int stopBits, SerialPortConfiguration.Parity parity) {
    driver.setPortConfiguration(spPort, portConfiguration = new SerialPortConfiguration(speed, bits, stopBits, parity));
  }

  public SerialPortConfiguration getPortConfiguration() {
    return portConfiguration;
  }

  private class SerialInputStream extends InputStream {
    private Pointer eventSet;

    SerialInputStream() {
      eventSet = driver.addEventListener(spPort, SerialPortEvent.READ_READY);
    }

    @Override
    public int read() throws IOException {
      int availableNumberBytes = driver.getByteReadyInput(spPort);

      if (availableNumberBytes == 0) {
        while ((driver.getByteReadyInput(spPort)) == 0) {
          driver.waitForPortData(eventSet, readTimeOut);
        }
      }

      byte[] bytes = new byte[1];
      driver.nonBlockingRead(spPort, bytes, bytes.length);

      return bytes[0];
    }


    @Override
    public int read(byte[] buff) throws IOException {
      int availableNumberBytes = driver.getByteReadyInput(spPort);

      if (availableNumberBytes == 0) {
        driver.waitForPortData(eventSet, readTimeOut);
        availableNumberBytes = driver.getByteReadyInput(spPort);
      }

      int length = availableNumberBytes > buff.length ? buff.length : availableNumberBytes;

      if (length != 0) {
        driver.nonBlockingRead(spPort, buff, length);
      }

      return length;
    }
  }

  private class SerialOutputStream extends OutputStream {
    private Pointer eventSet;

    SerialOutputStream() {
      eventSet = driver.addEventListener(spPort, SerialPortEvent.WRITE_READY);
    }

    @Override
    public void write(int b) throws IOException {
      write(new byte[]{(byte) b});
    }

    private void drain() {
      int waitingByteNumbers = driver.getWatingForWriteBytes(spPort);
      while (waitingByteNumbers != 0) {
        driver.waitWriteFinish(eventSet, writeTimeOut);
      }
    }

    @Override
    public void write(byte[] buff) throws IOException {
      driver.nonBlockingWrite(spPort, buff, buff.length);
      drain();
    }
  }
}
