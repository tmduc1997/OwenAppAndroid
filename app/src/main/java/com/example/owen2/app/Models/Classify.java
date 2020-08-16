package com.example.owen2.app.Models;

public class Classify {
    public int Classify_ID;
    public String Name;
    public int Display;

    public Classify(int classify_ID, String name, int display) {
        Classify_ID = classify_ID;
        Name = name;
        Display = display;
    }

    public int getClassify_ID() {
        return Classify_ID;
    }

    public void setClassify_ID(int classify_ID) {
        Classify_ID = classify_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getDisplay() {
        return Display;
    }

    public void setDisplay(int display) {
        Display = display;
    }
}
