package atoz.protection.model;

public class WalletHistory {
    String walletmobile,mobile,status,created;
    int amount;

    public String getWalletmobile() {
        return walletmobile;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setWalletmobile(String walletmobile) {
        this.walletmobile = walletmobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
