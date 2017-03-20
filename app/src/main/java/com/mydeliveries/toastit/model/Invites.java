package com.mydeliveries.toastit.model;

/**
 * Created by Andrew on 04/09/2015.
 */

public class Invites {


    private String name;
    private String email;


    private boolean isChecked;

    public Invites(String name, String email){
        this.name = name;
        this.email = email;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


}