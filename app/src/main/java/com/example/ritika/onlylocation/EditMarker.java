package com.example.ritika.onlylocation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.attr.tag;

public class EditMarker extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    ArrayList<String> AddressOfMarker = new ArrayList<>(MapsActivity.ListOfMarkers.size());
    ArrayList <String> DescriptionTextOfMarker = new ArrayList<>(MapsActivity.ListOfMarkers.size());

    double latitude, longitude;

    //edit_marker layout ones
    Button back;
    Button SaveEditChanges;
    //Button deleteMarker;

    EditText displayAddress;
    EditText description;
    Marker CurrentMarker;// = MapsActivity.ListOfMarkers.get(MapsActivity.markerIndex);

    private static final int EXISTING_MARKER_LOADER = 0;

    String markerTiltle;
    String markerDescription;

    private Uri mCurrentMarkerUri;
    Long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_edit);

        //setTheme(android.R.style.Theme_Holo_Light);

        //getActionBar().show();

        setTitle("My Markers");

        //back = (Button) findViewById(R.id.Back);
        //SaveEditChanges = (Button) findViewById(R.id.SaveEditting);
        //deleteMarker = (Button) findViewById(R.id.delete);
        displayAddress = (EditText) findViewById(R.id.nameofPlace);
        description = (EditText) findViewById(R.id.descriptionText);

        Intent intent = getIntent();
        mCurrentMarkerUri = intent.getData();

        id = ContentUris.parseId(mCurrentMarkerUri);


        CurrentMarker = MapsActivity.markerMap.get(id);


        //changedescription( CurrentMarker);

        //back.setOnClickListener(this);
        //SaveEditChanges.setOnClickListener(this);
        //deleteMarker.setOnClickListener(this);
        //description.setOnClickListener(this);
        //displayAddress.setOnClickListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportLoaderManager().initLoader(EXISTING_MARKER_LOADER, null, this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        LatLng CurrentMarkerlt = /*new LatLng(latitude, longitude);*/CurrentMarker.getPosition();
        //CurrentMarkerlt.latitude = latitude;

        Marker newM = mMap.addMarker(new MarkerOptions().position(CurrentMarkerlt).title("Marker"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentMarkerlt));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentMarkerlt, 10));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit_marker_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home :
                //Toast.makeText(this, "All changes made to note were discarded", Toast.LENGTH_SHORT).show();
                //will of back to the specified parent activity. You can mention that separateky in Manifest file.
                //the parent activity for each individual activity
                Toast.makeText(this, "Changes discarded", Toast.LENGTH_SHORT).show();
                int value = 0;

                Intent intent = getIntent();
                intent.putExtra("key", value);
                setResult(RESULT_CANCELED, intent);

                finish();
                return true;

            case R.id.save_marker :
                //Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                saveChanges();

                return true;
            case R.id.discard_marker :
                showDeleteConfirmationDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
        //return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete the marker");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteMarker();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    private void deleteMarker() {

       // MapsActivity.toDeleteMarker = 1;

        // Only perform the delete if this is an existing pet.
        if (mCurrentMarkerUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentMarkerUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Delete Failed!",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                //Toast.makeText(this,"Deleted!",
                  //      Toast.LENGTH_SHORT).show();
            }

        }
//        MapsActivity.markerMap.remove(id);
//        MapsActivity.markerMap.remove(id);
//        MapsActivity.markerMapAddresses.remove(CurrentMarker);
        CurrentMarker.remove();

        String value = "1";

        Intent intent = getIntent();
        intent.putExtra("key", value);
        setResult(RESULT_OK, intent);

        finish();

        //Toast.makeText(this, "TD VAL : "+MapsActivity.toDeleteMarker,Toast.LENGTH_SHORT).show();


    }

    void saveChanges()
    {
        //Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        //displayAddress.setText("");
        // displayAddress.setVisibility(View.GONE);
        MapsActivity.ButtonToEditMarker.setVisibility(View.GONE);
        MapsActivity.displayMarkerAddress.setVisibility(View.GONE);


        String addressContent = displayAddress.getText().toString();
        String desc = description.getText().toString();

       // Toast.makeText(this, "add : "+ addressContent, Toast.LENGTH_SHORT).show();

        ContentValues values = new ContentValues();
        values.put(MarkersContract.MarkersEntry.COLUMN_TITLE, addressContent);
        values.put(MarkersContract.MarkersEntry.COLUMN_DESCRIPTION, desc);

        int rowsAffected = getContentResolver().update(mCurrentMarkerUri, values, null, null);
        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, "COULDN'T UPDATE",Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, "Changes Saved!", Toast.LENGTH_SHORT).show();
        }

        int value = 0;

        Intent intent = getIntent();
        intent.putExtra("key", value);
        setResult(RESULT_CANCELED, intent);

        finish();


        //finish();
    }

    /*@Override
    public void onClick(View view) {
        *//*if (view == back) {

            Toast.makeText(this, "No Changes Saved", Toast.LENGTH_SHORT).show();
            MapsActivity.ButtonToEditMarker.setVisibility(View.GONE);
            MapsActivity.displayMarkerAddress.setVisibility(View.GONE);
            finish();
        } else if (view == SaveEditChanges) {





        } else*//* if (view == deleteMarker) {
            MapsActivity.ListOfMarkers.remove(MapsActivity.markerIndex).remove();
            Toast.makeText(this, "Marker Deleted", Toast.LENGTH_SHORT).show();
            //displayAddress.setText("");
            //displayAddress.setVisibility(View.GONE);
            MapsActivity.ButtonToEditMarker.setVisibility(View.GONE);
            MapsActivity.displayMarkerAddress.setVisibility(View.GONE);
            finish();

        } else if (view == displayAddress) {
            displayAddress.setText("");
            displayAddress.setCursorVisible(true);
        } else if (view == description) {
            //Toast.makeText(this,"you clicked description",Toast.LENGTH_SHORT).show();
            if(description.getText().toString().equals("description"))
            {
                description.setText("");
                description.setCursorVisible(true);
                //Toast.makeText(this,"Inside if",Toast.LENGTH_SHORT).show();
            }
            else
            {description.setCursorVisible(true);
                //Toast.makeText(this,"inside else",Toast.LENGTH_SHORT).show();
            }

        }
    }
*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all Markers attributes, define a projection that contains
        // all columns from the Markers table
        String[] projection = {
                MarkersContract.MarkersEntry._ID,
                MarkersContract.MarkersEntry.COLUMN_TITLE,
                MarkersContract.MarkersEntry.COLUMN_DESCRIPTION, MarkersContract.MarkersEntry.COLUMN_LATITUDE,
                MarkersContract.MarkersEntry.COLUMN_LONGITUDE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentMarkerUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor!=null && loader!=null)
        {
            if (cursor.moveToFirst()) {
                try    // Find the columns of pet attributes that we're interested in
                {
                    int titleColumnIndex = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_TITLE);
                    int contentColumnIndex = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_DESCRIPTION);
                    int latColumn= cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_LATITUDE);
                    int longColumn = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_LONGITUDE);


                    // Extract out the value from the Cursor for the given column index
                    String title = cursor.getString(titleColumnIndex);
                    String con = cursor.getString(contentColumnIndex);
                    latitude = Double.parseDouble(cursor.getString(latColumn));
                    longitude = Double.parseDouble(cursor.getString(longColumn));

                    displayAddress.setText(title);
                    description.setText(con);

                    //Toast.makeText(this, "BALUES ARE : "+ title + " "+ con, Toast.LENGTH_SHORT).show();
                    //lastEdited.setText("Last edited : "+ date);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(String.valueOf(tag), "NOT FOUND!");
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO WHAT TO DO HERE??
        displayAddress.setText("");
        description.setText("");


    }
}

