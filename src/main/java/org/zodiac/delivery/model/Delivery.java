package org.zodiac.delivery.model;

public class Delivery {
    public static int CHECKING = 0;
    public static int YES = 1;
    public static int DONE = 2;
    public static int CANCELED = 2;

    public static int PREPAY = 0;
    public static int POSTPAY = 1;

    private int no;
    private String describe;
    private String senderName;
    private String senderPhone;
    private String recipientName;
    private String recipientPhone;
    private String recipientEmail;
    private String recipientAddress;
    private int status;
    private int method;
    
    public Delivery (int no, String describe, String senderName, String senderPhone, String recipientName, String recipientPhone, String recipientEmail, String recipientAddress, int status, int method) {
        this.no = no;
        this.describe = describe;
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.recipientEmail = recipientEmail;
        this.recipientAddress = recipientAddress;
        this.status = status;
    }

    /**
     * @return the no
     */
    public int getNo() {
        return no;
    }

    /**
     * @return the describe
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * @return the sender
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @return the senderPhone
     */
    public String getSenderPhone() {
        return senderPhone;
    }

    /**
     * @return the recipientName
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * @return the recipientPhone
     */
    public String getRecipientPhone() {
        return recipientPhone;
    }

    /**
     * @return the recipientEmail
     */
    public String getRecipientEmail() {
        return recipientEmail;
    }

    /**
     * @return the recipientAddress
     */
    public String getRecipientAddress() {
        return recipientAddress;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the method
     */
    public int getMethod() {
        return method;
    }

    /**
     * @param describe the describe to set
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * @param recipientAddress the recipientAddress to set
     */
    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
}

