package org.menesty.jnr.serialport.exception;

/**
 * Created by Menesty
 * on 5/26/17.
 */
public class SerialPortException extends RuntimeException {
  private final String libraryError;
  private final String libraryErrorCode;

  public SerialPortException(String message, String libraryError, String libraryErrorCode) {
    super(String.format("%s, library error: %s , error code: %s", message, libraryError, libraryErrorCode));
    this.libraryError = libraryError;
    this.libraryErrorCode = libraryErrorCode;
  }

  public String getLibraryError() {
    return libraryError;
  }

  public String getLibraryErrorCode() {
    return libraryErrorCode;
  }
}
