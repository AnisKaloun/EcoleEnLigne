package com.example.ecoleenligne.Model;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    List<String> notifs=new ArrayList<String>();

    public Notification() {
    }

    public List<String> getNotifs() {
        return notifs;
    }

    public void setNotifs(List<String> notifs) {
        this.notifs = notifs;
    }

    public void AddNotif(String notif)
    {
        this.notifs.add(notif);
    }
}

