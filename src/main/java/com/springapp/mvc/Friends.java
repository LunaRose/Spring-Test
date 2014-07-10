package com.springapp.mvc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: willbyrne
 * Date: 07/12/2013
 * Time: 01:08
 * To change this template use File | Settings | File Templates.
 */
public class Friends implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, Person> friends = new HashMap<String, Person>();

    public Friends(Map<String, Person> friends)
    {
        this.friends = friends;
    }

    public Map<String, Person> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, Person> friends) {
        this.friends = friends;
    }
}
