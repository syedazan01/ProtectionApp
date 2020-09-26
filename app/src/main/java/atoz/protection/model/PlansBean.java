package atoz.protection.model;

public class PlansBean {
    String plan_name, plan_description;
    int plan_duration,planPrice;

    public int getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(int planPrice) {
        this.planPrice = planPrice;
    }

    public int getPlan_duration() {
        return plan_duration;
    }

    public void setPlan_duration(int plan_duration) {
        this.plan_duration = plan_duration;
    }
    /*public PlansBean(String planPrice, String plan_name, String plan_duration, String plan_description) {
        this.planPrice = planPrice;
        this.plan_name = plan_name;
        this.plan_duration = plan_duration;
        this.plan_description = plan_description;
    }*/


    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }


    public String getPlan_description() {
        return plan_description;
    }

    public void setPlan_description(String plan_description) {
        this.plan_description = plan_description;
    }
}
