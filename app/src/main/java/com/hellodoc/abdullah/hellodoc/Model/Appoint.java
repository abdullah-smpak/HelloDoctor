package com.hellodoc.abdullah.hellodoc.Model;

public class Appoint {

    private String P_id;
    private String P_name;
    private String P_age;
    private String P_gender;
    private String P_phone;
    private String P_dayntime;

    public Appoint() {
    }

    public Appoint(String p_id, String p_name, String p_age, String p_gender, String p_phone, String p_dayntime) {
        P_id = p_id;
        P_name = p_name;
        P_age = p_age;
        P_gender = p_gender;
        P_phone = p_phone;
        P_dayntime = p_dayntime;
    }

    public String getP_id() {
        return P_id;
    }

    public void setP_id(String p_id) {
        P_id = p_id;
    }

    public String getP_name() {
        return P_name;
    }

    public void setP_name(String p_name) {
        P_name = p_name;
    }

    public String getP_age() {
        return P_age;
    }

    public void setP_age(String p_age) {
        P_age = p_age;
    }

    public String getP_gender() {
        return P_gender;
    }

    public void setP_gender(String p_gender) {
        P_gender = p_gender;
    }

    public String getP_phone() {
        return P_phone;
    }

    public void setP_phone(String p_phone) {
        P_phone = p_phone;
    }

    public String getP_dayntime() {
        return P_dayntime;
    }

    public void setP_dayntime(String p_dayntime) {
        P_dayntime = p_dayntime;
    }
}
