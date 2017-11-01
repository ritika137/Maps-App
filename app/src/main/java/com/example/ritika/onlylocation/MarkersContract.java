package com.example.ritika.onlylocation;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by RITIKA on 23-09-2017.
 */

public final class MarkersContract {

    private MarkersContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.ritika.onlylocation";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MARKERS = "Markers";


    /**
     * Inner class that defines constant values for the Notes database table.
     * Each entry in the table represents a single note.
     */

    public static final class MarkersEntry implements BaseColumns {


        /**
         +         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         +         */

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MARKERS;

        /**
         +         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         +         */public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MARKERS;

        /** Name of database table for Notes */
        public final static String TABLE_NAME = "Markers";

        /** The content URI to access the note data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MARKERS);

        /**
         * Unique ID number for the Note (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;
        /**
         * Title of the note.
         *
         * Type: TEXT
         */
        public final static String COLUMN_TITLE = "Title";
        /**
         * Content of the note.
         *
         * Type: TEXT
         */
        public final static String COLUMN_DESCRIPTION = "Description";

        public final static String COLUMN_LATITUDE = "latitude";

        public final static String COLUMN_LONGITUDE = "longitude";

        public final static String COLUMN_ADDRESS = "MarkerAddress";


    }
}
