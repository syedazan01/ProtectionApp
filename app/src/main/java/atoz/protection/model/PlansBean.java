package atoz.protection.model;

public class PlansBean {
    String planPrice, plan_name, plan_duration, plan_description;

    /*public PlansBean(String planPrice, String plan_name, String plan_duration, String plan_description) {
        this.planPrice = planPrice;
        this.plan_name = plan_name;
        this.plan_duration = plan_duration;
        this.plan_description = plan_description;
    }*/

    public String getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(String planPrice) {
        this.planPrice = planPrice;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_duration() {
        return plan_duration;
    }

    public void setPlan_duration(String plan_duration) {
        this.plan_duration = plan_duration;
    }

    public String getPlan_description() {
        return plan_description;
    }

    public void setPlan_description(String plan_description) {
        this.plan_description = plan_description;
    }
}
