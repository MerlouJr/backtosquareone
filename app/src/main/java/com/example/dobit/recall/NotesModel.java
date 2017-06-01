package com.example.dobit.recall;

/**
 * Created by dobit on 5/22/2017.
 */

public class NotesModel {
    public int pic;
    public String note;
    public String date;

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
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

    public NotesModel(String note, String date) {
        this.note = note;
        this.date = date;
    }

    public NotesModel() {
    }
}
