package com.example.ritika.onlylocation;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by RITIKA on 23-09-2017.
 */

public class MarkersProvider extends ContentProvider {

    private MarkersDbHelper mdbHelper;
    /** Tag for the log messages */
    public static final String LOG_TAG = MarkersProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */
    private static final int Markers = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int Markers_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(MarkersContract.CONTENT_AUTHORITY, MarkersContract.PATH_MARKERS,Markers);
        sUriMatcher.addURI(MarkersContract.CONTENT_AUTHORITY, MarkersContract.PATH_MARKERS + "/#",Markers_ID);
    }

    @Override
    public boolean onCreate() {

        mdbHelper = new MarkersDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mdbHelper.getReadableDatabase();

        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match)
        {
            case Markers:
                ///selecting whole table in this case
                cursor = db.query(MarkersContract.MarkersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case Markers_ID:
                // For the NOTE_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.quickMarkers/Markers/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.

                selection = MarkersContract.MarkersEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(MarkersContract.MarkersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        if(getContext()!=null && cursor!=null)
            cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Markers:
                return MarkersContract.MarkersEntry.CONTENT_LIST_TYPE;
            case Markers_ID:
                return MarkersContract.MarkersEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    @Nullable
    @Override
    public Uri insert( Uri uri, ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case Markers :
                return  insertNewMarker(uri, values);
            default:
                throw new IllegalArgumentException("Insert is not supported for "+ uri);

        }

    }

    private Uri insertNewMarker(Uri uri,ContentValues values )
    {

        SQLiteDatabase db = mdbHelper.getWritableDatabase();

        long id = db.insert(MarkersContract.MarkersEntry.TABLE_NAME, null, values);

        if(id==-1)
        {
            Log.e(LOG_TAG, "Failed to insert row for : "+ uri);
            return null;
        }
        //Toast.makeText( getContext(),"INSERTION HAS BEEN MADE WITH TITLE : " + values.getAsString(MarkersEntry.COLUMN_TITLE),Toast.LENGTH_SHORT).show();
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mdbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted =0;
        switch (match) {
            case Markers:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(MarkersContract.MarkersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case Markers_ID:
                // Delete a single row given by the ID in the URI
                selection = MarkersContract.MarkersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MarkersContract.MarkersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            // If 1 or more rows were deleted, then notify all listeners that the data at the
            // given URI has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sUriMatcher.match(uri);

        switch (match)
        {
            case Markers:
                return updateMarker(uri, values, selection, selectionArgs);
            case Markers_ID:
                selection = MarkersContract.MarkersEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMarker(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }
    //TODO ADD NOTIFYCAHNGE THING TO NOTIFY THE CURSOR ADAPTER.

    private int updateMarker(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mdbHelper.getWritableDatabase();

        int updatedRows = db.update(MarkersContract.MarkersEntry.TABLE_NAME,values,selection,selectionArgs);

        if(updatedRows==0)
        {
            Log.e(LOG_TAG, "Failed to update row for : "+ uri);
            return 0;
        }
        else if (updatedRows != 0) {
            // If 1 or more rows were updated, then notify all listeners that the data at the
            // given URI has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //db.notifyChange();
        return updatedRows;

    }
}
