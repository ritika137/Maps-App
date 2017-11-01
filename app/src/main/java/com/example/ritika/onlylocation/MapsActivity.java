package com.example.ritika.onlylocation;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.R.attr.tag;
import static android.R.attr.windowClipToOutline;
import static com.example.ritika.onlylocation.R.drawable.marker;
import static java.sql.Types.INTEGER;
import static java.sql.Types.NULL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,LocationSource, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    LocationRequest mLocationRequest;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    ImageView newMarker;
    LatLng center = null;

   // LatLng latLng;
    /*public static*/ GoogleMap mGoogleMap;
    SupportMapFragment mFragment;

    AutoCompleteTextView getLocation;
    ImageButton Search;
    String address;
    public static FloatingActionButton ButtonToEditMarker;
    FloatingActionButton AddNewMarker;
    public static TextView displayMarkerAddress;

    LinearLayout selectLocation;

    String result;
    LatLng latLng;
    public static int markerIndex=0;
    //private int count = 0;
    public static ArrayList<Marker> ListOfMarkers = new ArrayList<>();
    private List<Address> Listaddress = null;
    private List<Address> LatlngAddress = null;
    private Address addresssClass;

    TextView onMarkerClickAdd;

    ///Animation of the text views when set to visisble. (Slide down and slide up)
    private Animation animShow, animHide;

    public static int toDeleteMarker=0;
    Marker cMarker;
    Long cId;


    //Marker mCurrLocation;
    public boolean m=false;
    TextView random;
    boolean onCameraChanged= false;

    boolean hidden= true;
    boolean hiddensingle = true;
    boolean addNewhidden = false;



    public static  final int MARKERS_LOADER = 0;
    MarkersCursorAdapter mCursorAdapter;
    MarkersDbHelper mDbHelper;

    long idOfClickedMarker=0;
    Uri CurrentMarkerUri = null;
    Marker currentMarker;

    public static HashMap<Long, Marker> markerMap = new HashMap< Long, Marker>();
    public static HashMap<Marker, String> markerMapAddresses = new HashMap<Marker, String>();


    //ArrayList<MarkerObject> markersToPlace = new ArrayList<>();
    //HashMap<Marker, float> markerMap;



    public void onClick(View view) {
        /*if(view == Search)
        {
            address = getLocation.getText().toString();


            if(address!= null || !(address.equals("")))
            {
                showAddConfirmationDialog();
            }
            else
            {
                Toast.makeText(this,"Please enter a valid address",Toast.LENGTH_SHORT).show();
            }
            //getLocation.setText("Search");

            getLocation.setCursorVisible(false);
        }
        else if (view == getLocation)
        {
            //getLocation.setText("");
            getLocation.setCursorVisible(true);

            Intent appInfo = new Intent(MapsActivity.this, MainActivity.class);

            //appInfo.setData(CurrentMarkerUri);
            //appInfo.putExtra("MARKER", currentMarker);

            startActivity(appInfo);
        }
        else*/ if ( view == ButtonToEditMarker)
        {
            startEditActivity();

        }
        else if( view == AddNewMarker)
        {
            if(hiddensingle==false)
            {
                onMarkerClickAdd.startAnimation( animHide );
                onMarkerClickAdd.setVisibility(View.INVISIBLE);
                hiddensingle = true;
            }

            if(addNewhidden==false)
            {
                AddNewMarker.startAnimation( animHide );
                AddNewMarker.setVisibility(View.INVISIBLE);
                addNewhidden = true;
            }

            if(m==false)
            {
                Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 90 milliseconds
                v.vibrate(120);
                newMarker.setVisibility(View.VISIBLE);
                onCameraChanged=true;
                displayMarkerAddress.setClickable(true);
                mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        if(onCameraChanged==true)
                        {
                            center = mGoogleMap.getCameraPosition().target;
                            if(hidden==true)
                            {
                                displayMarkerAddress.setVisibility(View.VISIBLE);
                                random.setVisibility(View.VISIBLE);
                                selectLocation.setVisibility(View.VISIBLE);
                                selectLocation.startAnimation( animShow );
                            }
                            hidden= false;
                            if(center!=null)
                                getAddressFromLocation(center,getApplicationContext(), new GeocoderHandler());
                        }

                    }
                });
                m=true;
            }
        }
        else if (view==selectLocation)
        {
           Toast.makeText(this,"Adding new Marker",Toast.LENGTH_SHORT).show();

           Marker markerInserted =  mGoogleMap.addMarker(new MarkerOptions().position(center).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            LatLng searchLocation = center;

            newMarker.setVisibility(view.INVISIBLE);
            random.setVisibility(view.INVISIBLE);
            selectLocation.setVisibility(View.INVISIBLE);
            displayMarkerAddress.setVisibility(View.INVISIBLE);
            onMarkerClickAdd.setVisibility(View.INVISIBLE);
            m=false; onCameraChanged=false;
            hidden = true;
            hiddensingle = true;

            Double lat = searchLocation.latitude;
            Double longi = searchLocation.longitude;

            String latitude = lat.toString();
            String longitude = longi.toString();

            if(address!=null && !(address.equals("New Marker")))
            markerMapAddresses.put(markerInserted, address);

            ContentValues values = new ContentValues();
            values.put(MarkersContract.MarkersEntry.COLUMN_TITLE, address);
            values.put(MarkersContract.MarkersEntry.COLUMN_DESCRIPTION, "");
            values.put(MarkersContract.MarkersEntry.COLUMN_LATITUDE, latitude);
            values.put(MarkersContract.MarkersEntry.COLUMN_LONGITUDE, longitude);
            values.put(MarkersContract.MarkersEntry.COLUMN_ADDRESS, address);

            Uri newUri = getContentResolver().insert(MarkersContract.MarkersEntry.CONTENT_URI, values);

            Long idcm = ContentUris.parseId(newUri);


            markerInserted.setTag(idcm);

            CurrentMarkerUri = ContentUris.withAppendedId(MarkersContract.MarkersEntry.CONTENT_URI, idcm);

            markerMap.put(idcm, markerInserted);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "FAILED!", Toast.LENGTH_SHORT).show();
            }
            String s = idcm.toString();
            markerInserted.setTitle(s);

            mGoogleMap.setOnMarkerClickListener(this);
            startEditActivity();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(searchLocation));
        }
    }










    void startEditActivity()
    {
        selectLocation.setVisibility(View.INVISIBLE);
        displayMarkerAddress.setVisibility(View.INVISIBLE);
        onMarkerClickAdd.setVisibility(View.INVISIBLE);
        random.setVisibility(View.INVISIBLE);
        ButtonToEditMarker.setVisibility(View.INVISIBLE);
        hidden = true;
        hiddensingle = true;

        Intent appInfo = new Intent(MapsActivity.this, EditMarker.class);

        appInfo.setData(CurrentMarkerUri);
        //appInfo.putExtra("MARKER", currentMarker);

        startActivityForResult(appInfo, toDeleteMarker);
    }

    private void showAddConfirmationDialog(final LatLng coordinates, final String name, final String addressOfLocation) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to?");
        builder.setNeutralButton("Zoom", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                //getLocationThroughAddress(address,0);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));

            }
        });
        builder.setPositiveButton("Add Marker", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                AddMArkerToLocation(coordinates,name,addressOfLocation);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                getLocation.setText("");

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    private void AddMArkerToLocation(LatLng coordinates, String name, String addressOfLocation) {

        Marker markerInserted = /*ListOfMarkers.add(*/mGoogleMap.addMarker(new MarkerOptions().position(coordinates).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));//);

        Double lat = coordinates.latitude;
        Double longi = coordinates.longitude;

        String latitude = lat.toString();
        String longitude = longi.toString();

        markerMapAddresses.put(markerInserted, addressOfLocation);

        ContentValues values = new ContentValues();
        values.put(MarkersContract.MarkersEntry.COLUMN_TITLE, name);
        values.put(MarkersContract.MarkersEntry.COLUMN_DESCRIPTION, "");
        values.put(MarkersContract.MarkersEntry.COLUMN_LATITUDE, latitude);
        values.put(MarkersContract.MarkersEntry.COLUMN_LONGITUDE, longitude);
        values.put(MarkersContract.MarkersEntry.COLUMN_ADDRESS, addressOfLocation);

        Uri newUri = getContentResolver().insert(MarkersContract.MarkersEntry.CONTENT_URI, values);

        Long idcm = ContentUris.parseId(newUri);

        // Toast.makeText(this, "ADD : "+address, Toast.LENGTH_SHORT).show();


        markerInserted.setTag(idcm);

        CurrentMarkerUri = ContentUris.withAppendedId(MarkersContract.MarkersEntry.CONTENT_URI, idcm);

        markerMap.put(idcm, markerInserted);

        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "FAILED!", Toast.LENGTH_SHORT).show();
        } else {
           //Toast.makeText(this, "SUCCESFULLY "+ markerInserted.getTag(), Toast.LENGTH_SHORT).show();
        }
        String s = idcm.toString();
        markerInserted.setTitle(s);

        mGoogleMap.setOnMarkerClickListener(this);
        startEditActivity();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(coordinates));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Toast.makeText(this, "Actvity Ended: ", Toast.LENGTH_LONG).show();
        // check if the request code is same as what is passed  here it is 1
        if (requestCode == toDeleteMarker) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                cMarker.remove();
                markerMap.remove(cId);
                markerMap.remove(cId);
                markerMapAddresses.remove(cMarker);
                //final String result = data.getStringExtra("Key");
                // Use the data - in this case display it in a Toast.
                //Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
            }
        }
    }

   /* public void newLayoutForEdittingTheMarker() {

        //setContentView(R.layout.edit_marker);
        // New Layout
        back = (Button) findViewById(R.id.Back);
        SaveEditChanges = (Button) findViewById(R.id.SaveEditting);
        deleteMarker = (Button) findViewById(R.id.delete);
        displayAddress = (EditText) findViewById(R.id.nameofPlace);
        description = (EditText) findViewById(R.id.descriptionText);

        back.setOnClickListener(this);
        SaveEditChanges.setOnClickListener(this);
        deleteMarker.setOnClickListener(this);
    }*/

    void getLocationThroughAddress(String add, int placeMarker)
    {
        Geocoder geocoder = new Geocoder(this);
        try {
            Listaddress =geocoder.getFromLocationName(add,1);
        } catch (IOException e) {
            e.printStackTrace();
           // Toast.makeText(this,"Error displaying Location",Toast.LENGTH_SHORT).show();
        }
        if(Listaddress==null)
            Toast.makeText(this,"Connect to Internet",Toast.LENGTH_SHORT).show();
        else {
            addresssClass = Listaddress.get(0);
            if (addresssClass != null || !(addresssClass.equals(""))) {
                LatLng searchLocation = new LatLng(addresssClass.getLatitude(), addresssClass.getLongitude());
                if (m == false) {
                    if(placeMarker==1) {
                        Marker markerInserted = /*ListOfMarkers.add(*/mGoogleMap.addMarker(new MarkerOptions().position(searchLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));//);

                        Double lat = searchLocation.latitude;
                        Double longi = searchLocation.longitude;

                        String latitude = lat.toString();
                        String longitude = longi.toString();

                        markerMapAddresses.put(markerInserted, address);

                        ContentValues values = new ContentValues();
                        values.put(MarkersContract.MarkersEntry.COLUMN_TITLE, address);
                        values.put(MarkersContract.MarkersEntry.COLUMN_DESCRIPTION, "");
                        values.put(MarkersContract.MarkersEntry.COLUMN_LATITUDE, latitude);
                        values.put(MarkersContract.MarkersEntry.COLUMN_LONGITUDE, longitude);
                        values.put(MarkersContract.MarkersEntry.COLUMN_ADDRESS, "ABCD");

                        Uri newUri = getContentResolver().insert(MarkersContract.MarkersEntry.CONTENT_URI, values);

                        Long idcm = ContentUris.parseId(newUri);

                       // Toast.makeText(this, "ADD : "+address, Toast.LENGTH_SHORT).show();


                        markerInserted.setTag(idcm);

                        CurrentMarkerUri = ContentUris.withAppendedId(MarkersContract.MarkersEntry.CONTENT_URI, idcm);

                        markerMap.put(idcm, markerInserted);

                        if (newUri == null) {
                            // If the new content URI is null, then there was an error with insertion.
                            Toast.makeText(this, "FAILED!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the insertion was successful and we can display a toast.

                             //Toast.makeText(this, "SUCCESFULLY "+ markerInserted.getTag(), Toast.LENGTH_SHORT).show();

                            //MarkerObject obj = new MarkerObject(id, lat, longi);

                            //markersToPlace.add(id, obj);


                        }
                        String s = idcm.toString();
                        markerInserted.setTitle(s);

                        mGoogleMap.setOnMarkerClickListener(this);
                        startEditActivity();
                    }
                }
                if(placeMarker==0)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation, 12));
                else
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(searchLocation));

            } else {
                Toast.makeText(this, "Please provide a valid Location", Toast.LENGTH_SHORT).show();
            }
        }

    }
    //Add Intent service instead to make this reverse geocoding process fast
    public void getAddressFromLocation(
            final LatLng location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                result = null;
                try {
                    LatlngAddress = geocoder.getFromLocation(
                            location.latitude, location.longitude, 1);
                    if (LatlngAddress != null && LatlngAddress.size() > 0) {
                        Address toDisplay = LatlngAddress.get(0);

                        if(toDisplay!=null || !(toDisplay.equals("")))
                        {
                            if(toDisplay.getLocality()!=null)
                                result = toDisplay.getAddressLine(0) + ", " + toDisplay.getLocality() +  ", " + toDisplay.getCountryName() ;
                            else if (toDisplay.getAddressLine(1)!=null)
                                result = toDisplay.getAddressLine(0) + ", " + toDisplay.getAddressLine(1)   ;
                            else
                                result = toDisplay.getAddressLine(0)  +  ", " + toDisplay.getCountryName() ;


                            Log.v("TAGRES", result);
                            //displayMarkerAddress.setText(result);
                        }
                        else
                        {
                            displayMarkerAddress.setText("");
                            //Toast.makeText(context,"Please provide a valid Location",Toast.LENGTH_SHORT).show();
                        }

                        // sending back first address line and locality
                        //result = toDisplay.getAddressLine(0) + ", " + toDisplay.getLocality();
                    }
                } catch (IOException e) {
                    final String TAG = "MyActivity";
                    e.printStackTrace();
                    Log.e(TAG, "Impossible to connect to Geocoder", e);
                    //Toast.makeText(context, "Impossible to connect to Geocoder",Toast.LENGTH_SHORT).show();

                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }



    /*public void getAddressFromLocation(LatLng point)
    {
        Geocoder geocoder = new Geocoder(this);
        try {
            LatlngAddress = geocoder.getFromLocation(point.latitude,point.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this,"Error displaying Location",Toast.LENGTH_SHORT).show();
        }
        Address toDisplay = LatlngAddress.get(0);
        if(toDisplay!=null || !(toDisplay.equals("")))
        {
            if(toDisplay.getLocality()!=null)
                result = toDisplay.getAddressLine(0) + ", " + toDisplay.getLocality() +  ", " + toDisplay.getCountryName() ;
            else if (toDisplay.getAddressLine(1)!=null)
                result = toDisplay.getAddressLine(0) + ", " + toDisplay.getAddressLine(1)   ;
            else
                result = toDisplay.getAddressLine(0)  +  ", " + toDisplay.getCountryName() ;


            displayMarkerAddress.setText(result);
        }
        else
        {
            displayMarkerAddress.setText("");
            Toast.makeText(this,"Please provide a valid Location",Toast.LENGTH_SHORT).show();
        }



    }*/

        private OnLocationChangedListener mListener;
        /**
         * Flag to keep track of the activity's lifecycle. This is not strictly necessary in this
         * case because onMapLongPress events don't occur while the activity containing the map is
         * paused but is included to demonstrate best practices (e.g., if a background service were
         * to be used).
         */
        private boolean mPaused;

        @Override
        public void activate(OnLocationChangedListener listener) {
            mListener = listener;
        }

        @Override
        public void deactivate() {
            mListener = null;
        }

        @Override
        public void onMapLongClick(LatLng point) {

            if(hiddensingle==false)
            {
                onMarkerClickAdd.startAnimation( animHide );
                onMarkerClickAdd.setVisibility(View.INVISIBLE);
                ButtonToEditMarker.setAnimation(animHide);
                ButtonToEditMarker.setVisibility(View.INVISIBLE);
                hiddensingle = true;
            }

            if(addNewhidden==false)
            {
                AddNewMarker.startAnimation( animHide );
                AddNewMarker.setVisibility(View.INVISIBLE);
                addNewhidden = true;
            }

        if(m==false)
        {
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 90 milliseconds
            v.vibrate(120);
            newMarker.setVisibility(View.VISIBLE);
            //view.startAnimation( animShow );

            onCameraChanged=true;
            displayMarkerAddress.setClickable(true);
            mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if(onCameraChanged==true)
                    {
                        center = mGoogleMap.getCameraPosition().target;
                        //displayMarkerAddress.setVisibility(View.VISIBLE);
                        //displayMarkerAddress.startAnimation( animShow );


//                        random.setVisibility(View.VISIBLE);
//                        random.startAnimation( animShow );



                        if(hidden==true)
                        {
                            displayMarkerAddress.setVisibility(View.VISIBLE);
                            random.setVisibility(View.VISIBLE);
                            selectLocation.setVisibility(View.VISIBLE);
                            selectLocation.startAnimation( animShow );


                        }

                        hidden= false;

                    if(center!=null)
                        getAddressFromLocation(center,getApplicationContext(), new GeocoderHandler());

                    }

                }
            });

           // String str = "lat " + center.latitude;
            //Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
            m=true;
        }
            //displayMarkerAddress.setOnClickListener(this);



               /*mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                       // Context con = getApplicationContext();
                        //Toast.makeText(con,"I'm running",Toast.LENGTH_SHORT).show();

                        LatLng newPoint = new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);
                        displayMarkerAddress.setVisibility(View.VISIBLE);
                        getAddressFromLocation(newPoint);

                    }
                });*/



           /* if ( !mPaused) {
                //Location location = new Location("LongPressLocationProvider");
                //location.setLatitude(point.latitude);
                //location.setLongitude(point.longitude);
                //location.setAccuracy(100);
                //mListener.onLocationChanged(location);
                ListOfMarkers.add(mGoogleMap.addMarker(new MarkerOptions().position(point).title("Marker")));
                //getLocation.setText(point.toString());
                    mGoogleMap.setOnMarkerClickListener(this);

                } /*else {
                    Context context = getApplicationContext();
                    CharSequence text = "Cannot add more than 5 markers.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                }*/

        }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String result;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    result = bundle.getString("address");
                    break;
                default:
                    result = "Invalid";
            }
            // replace by what you need to do
            if(result.equals("Invalid"))
                address = "New Marker";
          displayMarkerAddress.setText(result);
            onMarkerClickAdd.setText(result);
        }
    }
    @Override
    public void onBackPressed() {

        newMarker.setVisibility(View.INVISIBLE);
        if(cMarker!=null)
        cMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        if(hiddensingle==false)
        ButtonToEditMarker.startAnimation( animHide );
        ButtonToEditMarker.setVisibility(View.INVISIBLE);

        if(hidden==false)
           selectLocation.startAnimation(animHide);
            selectLocation.setVisibility(View.INVISIBLE);

        if(hiddensingle==false)
        {
            onMarkerClickAdd.startAnimation(animHide);
            onMarkerClickAdd.setVisibility(View.INVISIBLE);
        }
        if(addNewhidden==true)
        {

            AddNewMarker.startAnimation(animShow);
            AddNewMarker.setVisibility(View.VISIBLE);
            addNewhidden = false;
        }

            //random.startAnimation( animHide);
        //random.setVisibility(View.GONE);

//        if(hidden==false)
//        displayMarkerAddress.startAnimation( animHide );
//        displayMarkerAddress.setVisibility(View.GONE);

        //displayMarkerAddress.setClickable(false);
        hidden = true;
        hiddensingle = true;
        m=false;
        onCameraChanged=false;
    }


    @Override
    public void onMapClick(LatLng latLng) {


            if (cMarker != null)
                cMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            if (hiddensingle == false) {
                ButtonToEditMarker.startAnimation(animHide);

                ButtonToEditMarker.setVisibility(View.INVISIBLE);
            }


            if (m != true) {
                if (hidden == false) {
                    selectLocation.startAnimation(animHide);
                    selectLocation.setVisibility(View.INVISIBLE);
                    hidden = true;


                }
                if (hiddensingle == false) {
                    onMarkerClickAdd.startAnimation(animHide);
                    onMarkerClickAdd.setVisibility(View.INVISIBLE);
                    // hiddensingle=true;
                    hiddensingle = true;
                }

                if(addNewhidden==true)
                {
                    AddNewMarker.setVisibility(View.VISIBLE);
                    AddNewMarker.startAnimation(animShow);
                    addNewhidden = false;
                }


//            if(hidden==false)
//                selectLocation.setVisibility(View.VISIBLE);
//            selectLocation.startAnimation( animShow );
                // random.startAnimation( animHide );
                //random.setVisibility(View.GONE);
            }




    }





          @Override
      public boolean onMarkerClick(Marker marker) {

              if(newMarker.getVisibility()==View.INVISIBLE) {
                  if (cMarker != null && cMarker!=marker)
                      cMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                  cMarker = marker;


                  marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                  if(addNewhidden==false)
                  {
                      AddNewMarker.startAnimation(animHide);
                      AddNewMarker.setVisibility(View.INVISIBLE);
                      addNewhidden = true;
                  }
              /*ListOfMarkers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                      Uri CurrentNoteUri = ContentUris.withAppendedId(MarkersContract.MarkersEntry.CONTENT_URI,id);

                      Intent appInfo = new Intent(MapsActivity.this, EditMarker.class);

                      appInfo.setData(CurrentNoteUri);

                      startActivity(appInfo);
                  }});*/


                  //Context context = getApplicationContext();

                  // if(marker!=null)
//              idOfClickedMarker = (long) marker.getTag();

                  String title = marker.getTitle();

                  //Toast.makeText(this, "title : "+ title, Toast.LENGTH_SHORT).show();

                  //Toast.makeText(this, "ID OF MARKER CLICKED : "+ idOfClickedMarker, Toast.LENGTH_SHORT).show();

                  currentMarker = marker;
                  if (title != null) {
                      idOfClickedMarker = Long.parseLong(title);
                      cId = idOfClickedMarker;


                      CurrentMarkerUri = ContentUris.withAppendedId(MarkersContract.MarkersEntry.CONTENT_URI, idOfClickedMarker);

                      //String sizee = Integer.toString(ListOfMarkers.size());
                      //Toast.makeText(this,"H IS : "+hidden,Toast.LENGTH_SHORT).show();
                      if (hiddensingle == true) {
                          ButtonToEditMarker.setVisibility(View.VISIBLE);
                          ButtonToEditMarker.startAnimation(animShow);
                      }
                      //hidden = false;

//                 if(hidden==true){
//                     selectLocation.setVisibility(View.VISIBLE);
//                  selectLocation.startAnimation( animShow );
//                 displayMarkerAddress.setVisibility(View.VISIBLE);
//                  displayMarkerAddress.startAnimation( animShow );
//                    hidden = false;
//
//                 }
                      if (hiddensingle == true) {
                          onMarkerClickAdd.setVisibility(View.VISIBLE);
                          onMarkerClickAdd.startAnimation(animShow);
                          hiddensingle = false;
                      }
//
//                  random.setVisibility(View.INVISIBLE);


                      ///TODO CHECK THIS PART

                      String addressCM = markerMapAddresses.get(marker);

                      //displayMarkerAddress.setText(addressCM);


                      if (addressCM == null)
                      ///TODO REMOVE    WHEN ADDRESS COMES OUT TO BE SAME IN BOTH CASES CHECK THAT
                      {
                          LatLng newLatlng = marker.getPosition();

                          getAddressFromLocation(newLatlng, this, new GeocoderHandler());

                      } else {
                          //Toast.makeText(this, "DIRECT : D", Toast.LENGTH_SHORT).show();
                          onMarkerClickAdd.setText(addressCM);
                      }
                      // TODO FIND A BETTER METHOD TO DO THIS PLEASE ::: HASHMAP

//              for(int i=0; i<ListOfMarkers.size();i++)
//              {
//                  if(ListOfMarkers.get(i).equals(marker))
//                  {
//                      markerIndex=i;
//                      break;
//                  }
//              }*/
                  }
              }
             // mGoogleMap.

            return true;


       }

    //private LongPressLocationSource mLocationSource = new LongPressLocationSource();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onmarker_click);



        //Search = (ImageButton) findViewById(R.id.searchButton);
        //getLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ButtonToEditMarker = (FloatingActionButton) findViewById(R.id.editMarker);
        displayMarkerAddress = (TextView) findViewById(R.id.displayMarkerAddress);
        newMarker = (ImageView) findViewById(R.id.markerToinsert);
        random = (TextView)findViewById(R.id.randomTEXT);
        onMarkerClickAdd = (TextView) findViewById(R.id.displayMarkerAddressOnly);
        AddNewMarker = (FloatingActionButton) findViewById(R.id.add_new);

        addNewhidden = false;

        selectLocation = (LinearLayout) findViewById(R.id.select_location);

        selectLocation.setOnClickListener(this);

        markerMap.clear();




        //getLocation.setText("Search");
        //getLocation.setCursorVisible(false);

        //Search.setOnClickListener(this);
        //getLocation.setOnClickListener(this);
        random.setOnClickListener(this);
        AddNewMarker.setOnClickListener(this);

        ButtonToEditMarker.setOnClickListener(this);

        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);

        mDbHelper = new MarkersDbHelper(this);
        mCursorAdapter = new MarkersCursorAdapter(this, null);

        hidden = true;

        //kicking off the loader
        getSupportLoaderManager().initLoader(MARKERS_LOADER,null, this);

        animShow = AnimationUtils.loadAnimation( this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( this, R.anim.view_hide);


        ///AUTOCOMPLETE FRAGMENT!!!!!!!!!!
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName());

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();
                //txtPlaceDetails.setText(placeDetailsStr);

                LatLng placeThroughAC = place.getLatLng();
                String name = (String) place.getName();
                String addressPlace = (String) place.getAddress();
                showAddConfirmationDialog(placeThroughAC, name, addressPlace);
                //Toast.makeText(MapsActivity.this, placeDetailsStr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setPadding(0, 170, 0, 120);

        //###############what did we do here?


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        } else {
            Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show();
        }

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnMapClickListener(this);
//        Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                .width(5)
//                .color(Color.RED));
       // mGoogleMap.setLocationSource(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //activate(mListener);
        //mLocationSource.onResume();
        mPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        //mLocationSource.onPause();
        mPaused = true;
        //deactivate();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
       // Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Please provide permission for Location and Storage",Toast.LENGTH_SHORT).show();
        }
        else
        {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
           // mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            /*MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));*/
            // mCurrLocation = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        else
        {
           // Toast.makeText(this,"Please turn on Location",Toast.LENGTH_SHORT).show();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500000); //5 seconds
        mLocationRequest.setFastestInterval(300000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(1F); //1/10 meter

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else
        {
            Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show();
        }

    }}

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        //remove previous current location marker and add new one at current position
        /*if (mCurrLocation != null) {
            mCurrLocation.remove();
        }*/
        if(location!=null)
        { latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));*/
       // mCurrLocation = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //if(!(location.equals(mLastLocation)) )
        //Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }}

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ///makes app more responsove

        //Toast.makeText(this, "INITATED PROCESS", Toast.LENGTH_SHORT).show();
        String[] projection = {
                MarkersContract.MarkersEntry._ID, MarkersContract.MarkersEntry.COLUMN_LATITUDE, MarkersContract.MarkersEntry.COLUMN_LONGITUDE, MarkersContract.MarkersEntry.COLUMN_ADDRESS};
        return new CursorLoader(this, MarkersContract.MarkersEntry.CONTENT_URI, projection, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {



        if(cursor!=null && loader!=null)
        {

            int count = cursor.getCount();
            //Toast.makeText(this, "LOADING "+count, Toast.LENGTH_SHORT).show();
            cursor.moveToFirst();

            for(int i=0;i<count;i++){
        //if (cursor.moveToFirst()) {
                try    // Find the columns of pet attributes that we're interested in
                {
                    int titleColumnIndex = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_LATITUDE);
                    int contentColumnIndex = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_LONGITUDE);
                    int contentId = cursor.getColumnIndex(MarkersContract.MarkersEntry._ID);
                    int contentAdd = cursor.getColumnIndex(MarkersContract.MarkersEntry.COLUMN_ADDRESS);


                    // Extract out the value from the Cursor for the given column index
                    String lat = cursor.getString(titleColumnIndex);
                    String longi = cursor.getString(contentColumnIndex);
                    String Id = cursor.getString(contentId);
                    String add = cursor.getString(contentAdd);



                    Log.v("TAG", "LAT : "+ lat + " LONG : " + longi);

                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(longi);

                    LatLng searchLocation = new LatLng(latitude, longitude);

                    //Toast.makeText(this, "LOADING MARKERS "+ contentAdd, Toast.LENGTH_SHORT).show();

                        ///*ListOfMarkers.add(*/mGoogleMap.addMarker(new MarkerOptions().position(searchLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
                    Marker newM = mGoogleMap.addMarker(new MarkerOptions().position(searchLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    newM.setTitle(Id);
                    mGoogleMap.setOnMarkerClickListener(this);

                    markerMap.put(Long.parseLong(Id), newM);
                   // markerMapAddresses.put(newM, add);

                    cursor.moveToNext();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(String.valueOf(tag), "NOT FOUND!");
                }
            }
        }
        //mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }



}

