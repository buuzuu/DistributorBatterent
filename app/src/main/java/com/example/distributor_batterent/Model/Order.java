package com.example.distributor_batterent.Model;

public class Order {

    private String userId,distributorId,userName,product,month,amount,address,orderId;

    public Order(){

    }


    public Order(String userId, String distributorId, String userName, String product, String month, String amount, String address, String orderId) {
        this.userId = userId;
        this.distributorId = distributorId;
        this.userName = userName;
        this.product = product;
        this.month = month;
        this.amount = amount;
        this.address = address;
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
