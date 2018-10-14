package tstapp.manee.com.tstapp.utils;

import java.util.ArrayList;

/**
 * Created by manee on 17/08/17.
 */

public class User {

    public static ArrayList<String> _noteID = new ArrayList<>();
    public static ArrayList<String> name = new ArrayList<>();
    public static ArrayList<String> username = new ArrayList<>();

    public User(ArrayList<String> userid, ArrayList<String> name, ArrayList<String> username) {
        this._noteID = userid;
        this.name = name;
        this.username = username;
    }
}
