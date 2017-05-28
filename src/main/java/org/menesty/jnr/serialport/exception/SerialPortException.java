package org.menesty.jnr.serialport.exception;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class SerialPortException extends RuntimeException {
  private final String libraryError;

  public SerialPortException(String message, String libraryError) {
    super(String.format("%s, library error: %s", message, libraryError));
    this.libraryError = libraryError;
  }

  public String getLibraryError() {
    return libraryError;
  }
}
