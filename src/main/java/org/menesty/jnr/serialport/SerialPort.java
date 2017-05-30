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

  SerialPort(SpPort spPort, PortMode portMode, SerialPortDriver driver) {
    this.spPort = spPort;
    this.driver = driver;

    serialInputStream = PortMode.READ == portMode || PortMode.READ_WRITE == portMode ? new SerialInputStream() : null;
    serialOutputStream = PortMode.WRITE == portMode || PortMode.READ_WRITE == portMode ? new SerialOutputStream() : null;
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

  public void close() throws IOException {
    serialInputStream.close();
    serialOutputStream.close();
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
    private boolean closed;

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
      checkStreamClose();

      int availableNumberBytes = driver.getByteReadyInput(spPort);

      if (availableNumberBytes == 0) {
        driver.waitForPortData(eventSet, readTimeOut);
        checkStreamClose();
        availableNumberBytes = driver.getByteReadyInput(spPort);
      }

      int length = availableNumberBytes > buff.length ? buff.length : availableNumberBytes;

      if (length != 0) {
        driver.nonBlockingRead(spPort, buff, length);
      }

      return length;
    }

    @Override
    public void close() throws IOException {
      super.close();
      closed = true;
      driver.freeEventSet(eventSet);
    }

    void checkStreamClose() throws IOException {
      if (closed) {
        throw new IOException("stream closed");
      }
    }
  }

  private class SerialOutputStream extends OutputStream {
    private Pointer eventSet;
    private boolean closed;

    SerialOutputStream() {
      eventSet = driver.addEventListener(spPort, SerialPortEvent.WRITE_READY);
    }

    @Override
    public void write(int b) throws IOException {
      write(new byte[]{(byte) b});
    }

    private void drain() throws IOException {

      int waitingByteNumbers = driver.getWatingForWriteBytes(spPort);
      while (waitingByteNumbers != 0) {
        driver.waitWriteFinish(eventSet, writeTimeOut);
      }
    }

    @Override
    public void write(byte[] buff) throws IOException {
      System.out.print("Write data to serial port: ");

      for (byte aBuff : buff) {
        System.out.print(aBuff);
      }
      System.out.println();

      driver.nonBlockingWrite(spPort, buff, buff.length);
      drain();
    }

    @Override
    public void close() throws IOException {
      super.close();
      closed = true;
      driver.freeEventSet(eventSet);
    }
  }
}
