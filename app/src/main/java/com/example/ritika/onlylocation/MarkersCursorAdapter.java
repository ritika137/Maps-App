package com.example.ritika.onlylocation;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by RITIKA on 24-09-2017.
 */

public class MarkersCursorAdapter extends CursorAdapter {

    TextView MarkersTitle;
    TextView MarkersContent;


    public MarkersCursorAdapter(Context context, Cursor c)
    {
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ///Inflates/creates a new list item object from our xml file. Returns a lank list item.
        return LayoutInflater.from(context).inflate(R.layout.marker_edit,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        /** Takes the data from the cursor and assigns it to the the list item
         * returned by the newView mwthod. It displays the information
         * in the list and binds the cursor to the list item
         */
        MarkersTitle = (TextView) view.findViewById(R.id.nameofPlace);
        MarkersContent = (TextView) view.findViewById(R.id.descriptionText);

        int titleIndex = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_TITLE);
        int contentIndex = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_DESCRIPTION);

        String title = cursor.getString(titleIndex);
        String content = cursor.getString(contentIndex);

        //Log.i(String.valueOf(tag),"THE CONTENT IS BEING SET AS : " + content);

        MarkersTitle.setText(title);
        MarkersContent.setText(content);
    }
}
