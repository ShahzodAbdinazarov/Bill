package uz.qmii.bill;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ListView history;
    private TextView current, income, outcome, from, to, setText;
    private int year, month, day, hour, minute;
    private DBHelper db;
    private long selectTime = Calendar.getInstance().getTimeInMillis();
    private long fromTime = 0;
    private long toTime = Calendar.getInstance().getTimeInMillis() + Calendar.getInstance().getTimeInMillis();

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

        income.setOnClickListener(l -> showDialog(true));
        outcome.setOnClickListener(l -> showDialog(false));
        from.setOnClickListener(l -> {
            setTime();
            setSetText(from);
            fromTime = selectTime;
            refresh();
        });
        to.setOnClickListener(l -> {
            setTime();
            setSetText(to);
            toTime = selectTime;
            refresh();
        });

    }

    public void setSetText(TextView setText) {
        this.setText = setText;
    }

    public void showDialog(boolean is) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        ImageView icon = dialog.findViewById(R.id.dialogImage);
        if (is) icon.setImageResource(R.drawable.up);
        else icon.setImageResource(R.drawable.down);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        if (is) title.setText(R.string.msgIn);
        else title.setText(R.string.msgOut);

        EditText dialogMoney = dialog.findViewById(R.id.dialogMoney);

        TextView dialogTime = dialog.findViewById(R.id.dialogTime);
        dialogTime.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                DateFormat.SHORT).format(new Date(Calendar.getInstance().getTimeInMillis())));

        selectTime = Calendar.getInstance().getTimeInMillis();

        dialogTime.setOnClickListener(l -> {
            setTime();
            setSetText(dialogTime);
        });

        EditText dialogInfo = dialog.findViewById(R.id.dialogInfo);

        Button dialogButton = dialog.findViewById(R.id.dialogOK);
        dialogButton.setOnClickListener(v -> {
            double money = Double.parseDouble(dialogMoney.getText().toString());
            String info = dialogInfo.getText().toString();
            if (dialogMoney.getText().toString().length() > 0) {
                History history = new History(1, money, selectTime, info, is);
                db.add(history);
                refresh();
            }
            dialog.dismiss();
        });

        dialog.show();

    }

    private void refresh() {
        Double in = db.getIncome(fromTime, toTime, true);
        NumberFormat decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String txtMoney = decimalFormat.format(in);
        income.setText(txtMoney);

        Double out = db.getIncome(fromTime, toTime, false);
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        txtMoney = decimalFormat.format(out);
        outcome.setText(txtMoney);

        Double number = in - out;
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        txtMoney = decimalFormat.format(number);
        current.setText(txtMoney);

        HistoryAdapter adapter = new HistoryAdapter(this, db.getAll(fromTime, toTime));
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