package atoz.protection.model;

public class PayRequestBean {
    int amount;
    String accountName,upiId,registeredMobile,payrequestDateAndTime;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getRegisteredMobile() {
        return registeredMobile;
    }

    public void setRegisteredMobile(String registeredMobile) {
        this.registeredMobile = registeredMobile;
    }

    public String getPayrequestDateAndTime() {
        return payrequestDateAndTime;
    }

    public void setPayrequestDateAndTime(String payrequestDateAndTime) {
        this.payrequestDateAndTime = payrequestDateAndTime;
    }
}
