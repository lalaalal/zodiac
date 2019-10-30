package org.zodiac.delivery.model;

public class Delivery {
    public static int CHECKING = 0;
    public static int DONE = 1;
    public static int CANCELED = 2;

    private int no;
    private String senderName;
    private String senderPhone;
    private String recipientName;
    private String recipientPhone;
    private String recipientEmail;
    private String recipientAddress;
    private int status;
    
    public Delivery (int no, String senderName, String senderPhone, String recipientName, String recipientPhone, String recipientEmail, String recipientAddress, int status) {
        this.no = no;
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