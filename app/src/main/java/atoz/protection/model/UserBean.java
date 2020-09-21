package atoz.protection.model;

public class UserBean {
    String mobile, profilePic, fcmToken, referBy;
    float walletAmount;
    boolean isSubscribe, isRefer;

    public float getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(float walletAmount) {
        this.walletAmount = walletAmount;
    }

    public boolean isRefer() {
        return isRefer;
    }

    public void setRefer(boolean refer) {
        isRefer = refer;
    }

    public String getReferBy() {
        return referBy;
    }

    public void setReferBy(String referBy) {
        this.referBy = referBy;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
