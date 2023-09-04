package com.mod5.projecttwo;

import java.io.Serializable;

public class DataItem implements Serializable {
    private int id;
    private String label1;
    private String label2;
    private int inventory;

    // created a constructor for the DataItem class
    public DataItem(int id, String label1, String label2){
        this.id = id;
        this.label1 = label1;
        this.label2 = label2;
        this.inventory = inventory;
    }

    // getter for the id
    public int getId(){
        return id;
    }

    // getter for label 1
    public String getLabel1(){
        return label1;
    }

    // setter for label 1
    public void setLabel1(String label1){
        this.label1 = label1;
    }

    // getter for label 2
    public String getLabel2(){
        return label2;
    }

    // setter for label 2
    public void setLabel2(String label2){
        this.label2 = label2;
    }

    // getter for inventory
    public int getInventory(){
        return inventory;
    }

    // setter for inventory
    public void setInventory(int inventory){
        this.inventory = inventory;
    }
}
