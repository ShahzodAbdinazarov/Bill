package uz.qmii.bill;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int money;
    private ListView history;
    private TextView current, income, outcome, from, to, setText;
    private int year, month, day, hour, minute;
    private DBHelper db;
    private long selectTime = Calendar.getInstance().getTimeInMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current = findViewById(R.id.current);
        income = findViewById(R.id.income);
        outcome = findViewById(R.id.outcome);
        history = findViewById(R.id.history);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        db = new DBHelper(this);

        refresh();

        income.setOnClickListener(l -> dialog(true));
        outcome.setOnClickListener(l -> dialog(false));
        from.setOnClickListener(l -> {
            setTime();
            setSetText(from);
        });
        to.setOnClickListener(l -> {
            setTime();
            setSetText(to);
        });

    }

    public void setSetText(TextView setText) {
        this.setText = setText;
    }

    void dialog(boolean isIncome) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edt = new EditText(this);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (isIncome) alert.setIcon(R.drawable.up);
        else alert.setIcon(R.drawable.down);

        if (isIncome) alert.setTitle(R.string.msgIn);
        else alert.setTitle(R.string.msgOut);

        alert.setView(edt);

        alert.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
            String text = edt.getText().toString();
            if (text.length() > 0) {
                History history;
                if (isIncome) {
                    money += Integer.parseInt(text);
                    history = new History(1, Integer.parseInt(text), selectTime, "aachen auriga ankara knelling", true);
                } else {
                    money -= Integer.parseInt(text);
                    history = new History(1, Integer.parseInt(text), selectTime, "ian qingdao nosebag shrugging", false);
                }
                db.add(history);
                refresh();
            }
            dialog.dismiss();
        });

        alert.setNegativeButton(android.R.string.no, (dialog, whichButton) -> dialog.dismiss());

        alert.show();
    }

    private void refresh() {
        Double in = db.getIncome(true);
        NumberFormat decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String txtMoney = decimalFormat.format(in);
        income.setText(txtMoney);

        Double out = db.getIncome(false);
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        txtMoney = decimalFormat.format(out);
        outcome.setText(txtMoney);

        Double number = in - out;
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        txtMoney = decimalFormat.format(number);
        current.setText(txtMoney);

        HistoryAdapter adapter = new HistoryAdapter(this, db.getAll());
        history.setAdapter(adapter);
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        popTimePicker();
    }

    private void popTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, this, hour, minute, false).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        selectTime = getTimeFromPicker();
        String time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(selectTime));
        setText.setText(time);
    }

    private long getTimeFromPicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

}