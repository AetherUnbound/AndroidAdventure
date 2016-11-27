package com.example.liet_kynes.homework2;

/**
 * Created by Liet-Kynes on 11/2/2016.
 */

public class Contacts {
    //Actual resources
    private int cNameID;

    private int cImageID;

    private int cDiffID;

    private int cMapID;

    private String cWeakness;


    //Getters and setters
    public int getcNameID() {
        return cNameID;
    }

    public void setcNameID(int cNameID) {
        this.cNameID = cNameID;
    }

    public int getcDiffID() {
        return cDiffID;
    }

    public void setcDiffID(int cDiffID) {
        this.cDiffID = cDiffID;
    }

    public int getcImageID() {
        return cImageID;
    }

    public void setcImageID(int cImageID) {
        this.cImageID = cImageID;
    }

    public String getcWeakness() {
        return cWeakness;
    }

    public void setcWeakness(String cWeakness) {
        this.cWeakness = cWeakness;
    }

    public int getcMapID() {
        return cMapID;
    }

    public void setcMapID(int cMapID) {
        this.cMapID = cMapID;
    }


    public Contacts(int nameResID, int imageResID, int diffResID) {
        cNameID = nameResID;
        cImageID = imageResID;
        cDiffID = diffResID;
        cMapID = 0;
    }


}

