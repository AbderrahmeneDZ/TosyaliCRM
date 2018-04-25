package app.crm.tosyali.tosyalicrm.crm_dashboard;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.crm.tosyali.tosyalicrm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    public MapFragment() {

    }

    private GoogleMap mMap;
    private TextInputLayout inputLatitude, inputLongitude;
    private EditText editLatitude, editLongitude;
    private Button saveLocation;

    public static double Longitude, Latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews(view);

        uiEvents();

        return view;
    }

    private void initViews(View view) {

        inputLatitude = (TextInputLayout) view.findViewById(R.id.layout_input_map_frag_lan);
        inputLongitude = (TextInputLayout) view.findViewById(R.id.layout_input_map_frag_long);

        editLatitude = (EditText) view.findViewById(R.id.edit_map_frag_lan);
        editLongitude = (EditText) view.findViewById(R.id.edit_map_frag_long);

        Latitude = 35.692174;
        Longitude = -0.630289;

        editLatitude.setText(Latitude+"");
        editLongitude.setText(Longitude+"");

        saveLocation = (Button) view.findViewById(R.id.button_select_location);

    }

    private void uiEvents() {

        editLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    double latitude = Double.parseDouble(charSequence.toString());
                    if (latitude < -85 || latitude > 85)
                        throw new Exception("Invalid Value");

                    inputLatitude.setErrorEnabled(false);
                }catch (Exception e){
                    inputLatitude.setError(getString(R.string.invalid));
                    inputLatitude.setErrorEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    double latitude = Double.parseDouble(editLatitude.getText().toString());
                    if (latitude < -85 || latitude > 85)
                        throw new Exception("Invalid Value");

                    inputLatitude.setErrorEnabled(false);
                    Latitude = latitude;
                    moveToNewLocation();
                }catch (Exception e){
                    inputLatitude.setError(getString(R.string.invalid));
                    inputLatitude.setErrorEnabled(true);
                }
            }
        });

        editLongitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    double longitude = Double.parseDouble(charSequence.toString());
                    if (longitude < -180 || longitude > 180)
                        throw new Exception("Invalid Value");

                    inputLongitude.setErrorEnabled(false);
                }catch (Exception e){
                    inputLongitude.setError(getString(R.string.invalid));
                    inputLongitude.setErrorEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    double longitude = Double.parseDouble(editLongitude.getText().toString());
                    if (longitude < -180 || longitude > 180)
                        throw new Exception("Invalid Value");

                    inputLongitude.setErrorEnabled(false);
                    Longitude = longitude;
                    moveToNewLocation();
                }catch (Exception e){
                    inputLongitude.setError(getString(R.string.invalid));
                    inputLongitude.setErrorEnabled(true);
                }
            }
        });

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void moveToNewLocation(LatLng latLng) {
        // ToDO: move to the new location
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void moveToNewLocation() {
        LatLng latLng = new LatLng(Latitude, Longitude);
        moveToNewLocation(latLng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        moveToNewLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Latitude = latLng.latitude;
                Longitude = latLng.longitude;
                editLatitude.setText(Latitude+"");
                editLongitude.setText(Longitude+"");
                moveToNewLocation(latLng);
            }
        });
    }
}
