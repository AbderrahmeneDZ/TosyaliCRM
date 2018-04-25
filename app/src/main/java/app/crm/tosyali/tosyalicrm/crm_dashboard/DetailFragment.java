package app.crm.tosyali.tosyalicrm.crm_dashboard;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.crm.tosyali.tosyalicrm.R;
import app.crm.tosyali.tosyalicrm.models.Interview;

public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }

    private Interview interview;
    @SuppressLint("ValidFragment")
    public DetailFragment(Interview interview) {
        this.interview = interview;
    }

    private TextView textName, textAddress,textDetailTitle, textDetail, textTitleProducts,
            textProduct_1, textProduct_2, textProduct_3, textDate;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                LatLng latLng = new LatLng(interview.getLatitude(), interview.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        textName = (TextView) view.findViewById(R.id.text_detail_name);
        textAddress = (TextView) view.findViewById(R.id.text_detail_address);
        textDetail = (TextView) view.findViewById(R.id.text_detail_details);
        textDetailTitle = (TextView) view.findViewById(R.id.text_detail_title_detail);
        textTitleProducts = (TextView) view.findViewById(R.id.text_detail_products_titles);
        textProduct_1 = (TextView) view.findViewById(R.id.text_detail_product_1);
        textProduct_2 = (TextView) view.findViewById(R.id.text_detail_product_2);
        textProduct_3 = (TextView) view.findViewById(R.id.text_detail_product_3);
        textDate = (TextView) view.findViewById(R.id.text_detail_date);

        textName.setText(interview.getName());
        textAddress.setText(interview.getAddress());
        textDetail.setText(interview.getDetails());
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, hh:mm");
        textDate.setText(formatter.format(new Date(interview.getDate())));
        if (interview.getDetails().isEmpty()){
            textDetailTitle.setVisibility(View.GONE);
            textDetail.setVisibility(View.GONE);
        }

        switch (interview.getProducts().size()){
            case 0:
                textTitleProducts.setVisibility(View.GONE);
                textProduct_1.setVisibility(View.GONE);
                textProduct_2.setVisibility(View.GONE);
                textProduct_3.setVisibility(View.GONE);
                break;
            case 1:
                textProduct_2.setVisibility(View.GONE);
                textProduct_3.setVisibility(View.GONE);
                textProduct_1.setText(interview.getProducts().get(0).getDescription());
                break;
            case 2:
                textProduct_3.setVisibility(View.GONE);
                textProduct_1.setText(interview.getProducts().get(0).getDescription());
                textProduct_2.setText(interview.getProducts().get(1).getDescription());
                break;
            case 3:
                textProduct_1.setText(interview.getProducts().get(0).getDescription());
                textProduct_2.setText(interview.getProducts().get(1).getDescription());
                textProduct_3.setText(interview.getProducts().get(2).getDescription());
                break;
        }

        return view;
    }

}
