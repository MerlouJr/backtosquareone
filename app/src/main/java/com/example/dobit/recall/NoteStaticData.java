package com.example.dobit.recall;

import com.example.dobit.recall.NotesModel;
import com.example.dobit.recall.R;

import java.util.ArrayList;

/**
 * Created by dobit on 5/22/2017.
 */

public class NoteStaticData {

    public static ArrayList<NotesModel> getData(){
        ArrayList<NotesModel> data = new ArrayList<>();

        int[] pic = {R.drawable.microphone, R.drawable.microphone};
        String[] content =  {"I placed my wallet in my pocket", "I have an appointment with the doctor at 5 PM"};
        String[] time = {"5/26/17 9:00 AM", "5/26/17 8:00 AM"};

        for (int i = 0; i < content.length; i++){
            NotesModel current = new NotesModel();
            current.pic = pic[i];
            current.note = content[i];
            current.date = time[i];

            data.add(current);
        }
        return data;
    }
}
