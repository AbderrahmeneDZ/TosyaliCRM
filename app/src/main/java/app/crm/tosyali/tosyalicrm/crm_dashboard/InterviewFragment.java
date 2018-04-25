package app.crm.tosyali.tosyalicrm.crm_dashboard;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.crm.tosyali.tosyalicrm.R;
import app.crm.tosyali.tosyalicrm.models.Interview;
import app.crm.tosyali.tosyalicrm.models.Product;
import app.crm.tosyali.tosyalicrm.tools.DialogProgress;

public class InterviewFragment extends Fragment {


    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    private static final String LongitudeKey = "LONG";
    private static final String LatitudeKey = "LAT";

    public InterviewFragment() {
    }

    private TextInputLayout inputName, inputAddress, inputDetails;
    private EditText editName, editAddress, editDetails;
    private CardView cardOpenMap, cardSelectDate, cardSelectTime;
    private CheckBox checkBoxProduct_1, checkBoxProduct_2, checkBoxProduct_3;
    private static TextView textDate, textTime;
    private Button saveInterview;

    private Interview interview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interview, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("interviews/"+firebaseAuth.getCurrentUser().getUid());


        initViews(view);
        uiEvents();

        interview = new Interview();
        interview.setProducts(new ArrayList<Product>());

        return view;
    }

    private void initViews(View view) {
        inputName = (TextInputLayout) view.findViewById(R.id.layout_input_interview_frag_client_name);
        inputAddress = (TextInputLayout) view.findViewById(R.id.layout_input_interview_frag_client_address);
        inputDetails = (TextInputLayout) view.findViewById(R.id.layout_input_interview_frag_details);

        editName = (EditText) view.findViewById(R.id.edit_interview_frag_client_name);
        editAddress = (EditText) view.findViewById(R.id.edit_interview_frag_client_address);
        editDetails = (EditText) view.findViewById(R.id.edit_interview_frag_details);

        cardOpenMap = (CardView) view.findViewById(R.id.card_map_selector);
        cardSelectDate = (CardView) view.findViewById(R.id.card_date_selector);
        cardSelectTime = (CardView) view.findViewById(R.id.card_time_selector);

        checkBoxProduct_1 = (CheckBox) view.findViewById(R.id.checkbox_product_1);
        checkBoxProduct_2 = (CheckBox) view.findViewById(R.id.checkbox_product_2);
        checkBoxProduct_3 = (CheckBox) view.findViewById(R.id.checkbox_product_3);

        textDate = (TextView) view.findViewById(R.id.text_interview_frag_date);
        textTime = (TextView) view.findViewById(R.id.text_interview_frag_time);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(new Date());
        textTime.setText(time);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        time = sdf.format(new Date());
        textDate.setText(time);

        saveInterview = (Button) view.findViewById(R.id.button_upload_interview);
    }

    private void uiEvents(){

        saveInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (formValidation())
                    initInterview();

            }
        });

        checkBoxEvents(checkBoxProduct_1,1);
        checkBoxEvents(checkBoxProduct_2,2);
        checkBoxEvents(checkBoxProduct_3,3);

        cardSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        cardSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        cardOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MapFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,fragment);
                fragmentTransaction.addToBackStack(MapFragment.class.getSimpleName());
                fragmentTransaction.commit();
            }
        });

    }

    private void initInterview() {

        final DialogProgress dialogProgress = new DialogProgress(getContext());
        dialogProgress.initDialog();

        interview.setName(editName.getText().toString());
        interview.setAddress(editAddress.getText().toString());
        interview.setDetails(editDetails.getText().toString());
        interview.setLatitude(MapFragment.Latitude);
        interview.setLongitude(MapFragment.Longitude);
        interview.setId(mDatabase.push().getKey());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(textDate.getText().toString()+" "+textTime.getText().toString());
            interview.setDate(convertedDate.getTime());
        } catch (ParseException e) {
            Log.i("ParseDate","Cannot Parse Date");
        }

        mDatabase.child(interview.getId()).setValue(interview)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            getFragmentManager().popBackStack();
                        }else {
                            Log.i("Firebase",task.getException().getMessage());
                            Toast.makeText(getContext(),"Something wrong happened, please check you connection and try again",Toast.LENGTH_LONG).show();
                        }
                        dialogProgress.dismissDialog();
                    }
                });

    }

    private void checkBoxEvents(final CheckBox checkBox, final int id){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    interview.getProducts().add(new Product(id,checkBox.getText().toString()));
                }else {
                    interview.getProducts().remove(new Product(id,checkBox.getText().toString()));
                }
            }
        });
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String currentH = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
            String currentM = minute < 10 ? "0"+minute : ""+minute;
            textTime.setText(currentH+":"+currentM);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String currentDay = day < 10 ? "0"+day : ""+day;
            String currentMonth = month < 10 ? "0"+(month + 1) : ""+(month + 1);
            textDate.setText(currentDay +"/"+ currentMonth +"/"+ year);
        }
    }

    private boolean formValidation(){

        boolean valid = true;

        if (editName.getText().toString().equals("")){
            inputName.setError(getString(R.string.invalid_name));
            inputName.setErrorEnabled(true);
            valid = false;
        }

        if (editAddress.getText().toString().equals("")){
            inputAddress.setError(getString(R.string.invalid_address));
            inputAddress.setErrorEnabled(true);
            valid = false;
        }

        return valid;
    }

}
