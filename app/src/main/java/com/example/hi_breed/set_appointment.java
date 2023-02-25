package com.example.hi_breed;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hi_breed.classesFile.BaseActivity;
import com.example.hi_breed.classesFile.service_class;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class set_appointment extends BaseActivity {

    TextView check_out_name,
            checkout_number,
            checkout_address,
            checkout_zip;
    TextView dateTextView,
    itemSlot;
    Button saveButton;
    RelativeLayout toolbarID,timeLayout,dateLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_appointment);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        // find the picker
        saveButton = findViewById(R.id.saveButton);
        timeLayout = findViewById(R.id.timeLayout);
        dateLayout = findViewById(R.id.dateLayout);
        dateTextView = findViewById(R.id.dateTextView);
        itemSlot = findViewById(R.id.itemSlot);
        check_out_name = findViewById(R.id.check_out_name);
        checkout_number = findViewById(R.id.checkout_number);
        checkout_address = findViewById(R.id.checkout_address);
        checkout_zip = findViewById(R.id.checkout_zip);
        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot s) {
                        check_out_name.setText(s.getString("firstName") + " " + s.getString("middleName") + " " + s.getString("lastName"));
                        checkout_address.setText(s.getString("address"));
                        checkout_zip.setText(s.getString("zipCode"));
                        checkout_number.setText("09318924806");
                    }
                });

        toolbarID = findViewById(R.id.toolbarID);
        toolbarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_appointment.this.onBackPressed();
                finish();
            }
        });


        Intent intent = getIntent();
        service_class service = (service_class) intent.getSerializableExtra("model");

// Step 1: Retrieve the value of the availability field from Firestore
        // Step 1: Retrieve the value of the availability field from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Services").document(service.getId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    List<String> availability = (List<String>) documentSnapshot.get("availability");
// Step 1: Get the available days from Firestore
                    List<String> availableDays = (List<String>) documentSnapshot.get("schedule");

                    timeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getTime(availability);
                        }
                    });
                    dateLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getDate(availableDays);
                        }
                    });
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput(service);
            }
        });
    }

    private void checkInput(service_class service) {
        String date = dateTextView.getText().toString();
        String time = itemSlot.getText().toString();

        if(date.equals("Date")){
            Toast.makeText(this, "Please set a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(time.equals("Time")){
            Toast.makeText(this, "Please set a time", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,Object> trans = new HashMap<>();
        trans.put("id","");

/*
        FirebaseFirestore.getInstance().collection("Transaction")
                .add()*/

    }


    String pmAMHolder,minutesHolder,hoursHolder;
    int count=0;
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void getTime(List<String> availability) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.m_service_time_dialog,null);
        MaterialButton done,cancel;
        TextView service_title_dialog_title,service_message,from;

        from = view.findViewById(R.id.from);
        from.setVisibility(View.GONE);
        service_title_dialog_title = view.findViewById(R.id.service_title_dialog_title);
        service_title_dialog_title.setText("Set time appointment");
        service_title_dialog_title.setTextSize(15);
        service_message = view.findViewById(R.id.service_message);
        service_message.setText("Please provide your available time.");

        done = view.findViewById(R.id.gender_dialog_btn_done);
        done.setText("Proceed");
        cancel = view.findViewById(R.id.gender_dialog_btn_cancel);
        //pm am
        NumberPicker pm_am_numberPicker = view.findViewById(R.id.pm_am_numberPicker);
        String[] pmAM = {"AM","PM"};
        pm_am_numberPicker.setMinValue(0);
        pm_am_numberPicker.setMaxValue(pmAM.length-1);
        pm_am_numberPicker.setDisplayedValues(pmAM);
        pm_am_numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pm_am_numberPicker.setWrapSelectorWheel(false);
        pm_am_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                pmAMHolder = String.valueOf(newVal);
            }
        });
        //minutes
        NumberPicker minutes_numberPicker = view.findViewById(R.id.minutes_numberPicker);
        minutes_numberPicker.setMinValue(0);
        minutes_numberPicker.setMaxValue(59);
        minutes_numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutes_numberPicker.setWrapSelectorWheel(false);
        minutes_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                minutesHolder = String.valueOf(newVal);
            }
        });



        String startTimeString = availability.get(0);
        String endTimeString = availability.get(1);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("h:mm a");

// Use "h:m a" pattern instead for minutes less than 10
        if (startTimeString.contains(":0")) {
            format = new SimpleDateFormat("h:m a");
        }

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = format.parse(startTimeString);
            endDate = format.parse(endTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

        // Step 3: Set up the NumberPicker for hours


        //hours
        NumberPicker picker = view.findViewById(R.id.hours_numberPicker);
        picker.setMinValue(startHour);
        picker.setMaxValue(endHour);
        picker.setValue(startHour);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                boolean isAfternoon = newVal >= 12;
                picker.setDisplayedValues(null); // clear the previous values
                // Update the AM/PM marker
                if (isAfternoon) {
                    pm_am_numberPicker.setValue(1); // 1 = PM
                    pmAMHolder = "PM";
                } else {
                    pm_am_numberPicker.setValue(0); // 0 = AM
                    pmAMHolder = "AM";
                }
                hoursHolder = String.valueOf(newVal);
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        done.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(hoursHolder == ""|| hoursHolder == null){
                    hoursHolder = String.valueOf(startHour);
                }
                if(pmAMHolder == ""|| pmAMHolder == null){
                    pmAMHolder = "AM";
                }
                else{
                    pmAMHolder = "PM";
                }
                if(minutesHolder == "" || minutesHolder == null){
                    minutesHolder ="0";
                }
                int hour = Integer.parseInt(hoursHolder);
                int minute = Integer.parseInt(minutesHolder);

// Convert 24-hour format to 12-hour format
                String pmAMHolders = "AM";
                if (hour >= 12) {
                    pmAMHolders = "PM";
                    hour %= 12;
                }
                if (hour == 0) {
                    hour = 12;
                }

                itemSlot.setText(convertDate(hour) + ":" + convertDate(minute) + " " + pmAMHolders);
                dialog.dismiss();
                hoursHolder="";
                pmAMHolder="";
                pmAMHolders="";
                minutesHolder="";
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    @SuppressLint({"DefaultLocale", "UseCompatLoadingForDrawables", "SetTextI18n"})
    private void getDate(List<String> schedule){
        Toast.makeText(this, schedule.toString(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.product_add_expiration_dialog, null);
        DatePicker datePicker = view.findViewById(R.id.datePickers);
        AppCompatImageView appCompatImageView = view.findViewById(R.id.appCompatImageView);
        appCompatImageView.setImageDrawable(getDrawable(R.drawable.dialog_calendar_borders));
        TextView from = view.findViewById(R.id.from);
        from.setVisibility(View.GONE);
        TextView service_title_dialog_title = view.findViewById(R.id.service_title_dialog_title);
        service_title_dialog_title.setText("Set date");
        TextView service_message = view.findViewById(R.id.service_message);
        service_message.setText("Note: Please select the date you want to set an appointment");
        Calendar calendars = Calendar.getInstance();
        long minDate = calendars.getTimeInMillis();
        calendars.set(9999, 11, 31);
        long maxDate = calendars.getTimeInMillis();

        datePicker.setMinDate(minDate);
        datePicker.setMaxDate(maxDate);

        builder.setView(view);
        MaterialButton cancel, save;

        cancel = view.findViewById(R.id.product_dialog_btn_cancel);
        save = view.findViewById(R.id.product_dialog_btn_done);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                // Check if the day of the week is in the schedule array
                if (schedule.contains(getDayOfWeek(dayOfWeek))) {
                    // Display the selected date in the text view
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());
                    dateTextView.setText(formattedDate);
                    Toast.makeText(set_appointment.this, formattedDate, Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                } else {
                    // Display an error message
                    Toast.makeText(set_appointment.this, "Selected date is not available in the schedule", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "";
        }
    }

}