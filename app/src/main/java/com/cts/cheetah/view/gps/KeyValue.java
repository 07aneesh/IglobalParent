package com.cts.cheetah.view.gps;

/**
 * Created by dell on 9/24/2017.
 */

public class KeyValue {
    private int Id;
    private String Value;

    public KeyValue(){
        this.Id = 0;
        this.Value = "";
    }

    public void setId(int id){
        this.Id = id;
    }

    public int getId(){
        return this.Id;
    }

    public void setValue(String name){
        this.Value = name;
    }

    public String getValue(){
        return this.Value;
    }
}
