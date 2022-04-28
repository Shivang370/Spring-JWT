package org.example.exception;

public class UnauthorizedApiSessionException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String messageCode;
  private String messageDesc;

  public UnauthorizedApiSessionException(String messageCode, String messageDesc) {
    super();
    this.messageCode = messageCode;
    this.messageDesc = messageDesc;
  }

  public String getMessageCode() {
    return messageCode;
  }

  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }

  public String getMessageDesc() {
    return messageDesc;
  }

  public void setMessageDesc(String messageDesc) {
    this.messageDesc = messageDesc;
  }

}
