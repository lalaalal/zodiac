package org.zodiac.delivery.model;

public class User {
    private int no;
    private String id;
    private String pw;
    private String name;
    private String phone;
    private String email;
    private String address;

    public User(int no, String id, String pw, String name, String phone, String email, String address) {
        this.no = no;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getNo() {
        return no;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return Integer.toString(this.no);
    }
}