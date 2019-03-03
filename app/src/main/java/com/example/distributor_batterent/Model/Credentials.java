package com.example.distributor_batterent.Model;

public class Credentials {
    private String usedID,rcURL;

    public Credentials() {
    }

    public Credentials(String usedID, String rcURL) {
        this.usedID = usedID;
        this.rcURL = rcURL;
    }

    public String getUsedID() {
        return usedID;
    }

    public void setUsedID(String usedID) {
        this.usedID = usedID;
    }

    public String getRcURL() {
        return rcURL;
    }

    public void setRcURL(String rcURL) {
        this.rcURL = rcURL;
    }
}
