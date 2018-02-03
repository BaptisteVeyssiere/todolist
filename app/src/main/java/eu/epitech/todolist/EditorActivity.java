package eu.epitech.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText btnTitle = null;
    private EditText btnDescription = null;
    private ImageButton btnDatePicker = null;
    private ImageButton btnTimePicker = null;
    private TextView txtDate = null;
    private TextView txtTime = null;
    private EditText btnCommentary = null;
    private RadioGroup group = null;
    private Task task = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_creator);

        Intent data = getIntent();
        task = data.getParcelableExtra("TASK");

        btnTitle = findViewById(R.id.title);
        btnTitle.setText(task.getTitle());
        btnDescription = findViewById(R.id.description);
        btnDescription.setText(task.getDescription());
        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.in_date);
        txtDate.setText(extract_date(task.getDate(), false));
        txtTime = findViewById(R.id.in_time);
        txtTime.setText(extract_date(task.getDate(), true));
        btnCommentary = findViewById(R.id.commentary);
        btnCommentary.setText(task.getCommentary());
        group = findViewById(R.id.status);
        switch (task.getStatus()) {
            case "TO DO":
                group.check(R.id.todo);
                break;
            case "URGENT":
                group.check(R.id.urgent);
                break;
            case "DONE":
                group.check(R.id.done);
                break;
        }

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        Button cancelButton = findViewById(R.id.cancel_action);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });

        Button saveButton = findViewById(R.id.save_action);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.todo:
                        task.setStatus("TO DO");
                        break;
                    case  R.id.urgent:
                        task.setStatus("URGENT");
                        break;
                    case  R.id.done:
                        task.setStatus("DONE");
                        break;
                    default:
                        task.setStatus("TO DO");
                        break;
                }
                String dateString = txtDate.getText().toString() + "T" + txtTime.getText().toString();
                task.setDate(dateString);
                checkValues(result);
            }
        });
    }

    private void checkValues(Intent result) {
        if (btnTitle.getText().toString().equals("")) {
            Toast.makeText(EditorActivity.this, "The title is missing", Toast.LENGTH_SHORT).show();
        } else if (txtDate.getText().toString().equals("") || txtTime.getText().toString().equals("")) {
            Toast.makeText(EditorActivity.this, "The date / time is missing", Toast.LENGTH_SHORT).show();
        } else {
            task.setTitle(btnTitle.getText().toString());
            task.setDescription(btnDescription.getText().toString());
            task.setCommentary(btnCommentary.getText().toString());
            result.putExtra("TASK", task);
            setResult(RESULT_OK, result);
            finish();
        }
    }

    private String extract_date(String dateString, boolean time) {
        if (time) {
            return dateString.substring(14);
        } else {
            return (dateString.substring(6, 10) + "-" + dateString.substring(3, 5) + "-" + dateString.substring(0, 2));
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(String.format("%04d", year) + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth));

                        }
                    }, year, month, day);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.timepicker,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                        }
                    }, hour, minute, false);
            timePickerDialog.show();
        }
    }
}
