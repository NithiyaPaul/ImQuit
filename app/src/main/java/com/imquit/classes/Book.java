package com.imquit.classes;

import com.orm.SugarRecord;

/**
 * Created by admininstrator on 9/1/18.
 */

public class Book extends SugarRecord {

    public String packageName;
    public String time;

    public Book() {
    }

    public Book(String packageName, String time) {
        this.packageName = packageName;
        this.time = time;
    }
}
