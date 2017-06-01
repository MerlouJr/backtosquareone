package com.example.dobit.recall;

/**
 * Created by dobit on 5/31/2017.
 */

public class Notes {
    public String note;
    public String date;

    public Notes() {
    }

    public Notes(String note, String date) {
        this.note = note;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
