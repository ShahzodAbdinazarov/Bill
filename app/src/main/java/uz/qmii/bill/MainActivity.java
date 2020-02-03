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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint({"SetTextI18n", "NewApi"})
public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ListView history;
    private TextView current, income, outcome, from, to, setText;
    private int year, month, day;
    private DBHelper db;
    private Controller cr;
    private long toTime, fromTime, selectTime = Calendar.getInstance().getTimeInMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        ImageView btnFilter = findViewById(R.id.filter);
        current = findViewById(R.id.current);
        income = findViewById(R.id.income);
        outcome = findViewById(R.id.outcome);
        history = findViewById(R.id.history);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        db = new DBHelper(this);
        cr = new Controller(this);
        toTime = Calendar.getInstance().getTimeInMillis();
        fromTime = cr.getFromTime();
        String time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(fromTime));
        from.setText(time);
        if (cr.getFilter()) btnFilter.setImageResource(R.drawable.ic_loan_all);

        refresh();

        fab.setOnClickListener(l -> dialog());

        from.setOnClickListener(l -> {
            setTime();
            setSetText(from);
        });
        to.setOnClickListener(l -> {
            setTime();
            setSetText(to);
        });
        btnFilter.setOnClickListener(l -> {
            if (cr.getFilter()) {
                btnFilter.setImageResource(R.drawable.ic_bill_all);
                cr.setFilter(false);
            } else {
                btnFilter.setImageResource(R.drawable.ic_loan_all);
                cr.setFilter(true);
            }
            refresh();
        });

    }

    private void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_type);

        TextView txtOutgoing = dialog.findViewById(R.id.txtOutgoing);
        TextView txtIncome = dialog.findViewById(R.id.txtIncome);
        TextView txtBorrow = dialog.findViewById(R.id.txtBorrow);
        TextView txtBorrowBack = dialog.findViewById(R.id.txtBorrowBack);
        TextView txtLend = dialog.findViewById(R.id.txtLend);
        TextView txtLendBack = dialog.findViewById(R.id.txtLendBack);

        txtOutgoing.setOnClickListener(l -> showInputDialog(0));
        txtIncome.setOnClickListener(l -> showInputDialog(1));
        txtBorrow.setOnClickListener(l -> showInputDialog(2));
        txtBorrowBack.setOnClickListener(l -> showInputDialog(3));
        txtLend.setOnClickListener(l -> showInputDialog(4));
        txtLendBack.setOnClickListener(l -> showInputDialog(5));

        dialog.show();
    }

    public void setSetText(TextView setText) {
        this.setText = setText;
    }

    public void showInputDialog(int type) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        ImageView icon = dialog.findViewById(R.id.dialogImage);
        TextView title = dialog.findViewById(R.id.dialogTitle);
        switch (type) {
            case 0:
                icon.setImageResource(R.drawable.down);
                title.setText(R.string.outgoing);
                break;
            case 1:
                icon.setImageResource(R.drawable.up);
                title.setText(R.string.income);
                break;
            case 2:
                icon.setImageResource(R.drawable.ic_borrow);
                title.setText(R.string.borrow);
                break;
            case 3:
                icon.setImageResource(R.drawable.ic_borrow_back);
                title.setText(R.string.borrow_back);
                break;
            case 4:
                icon.setImageResource(R.drawable.ic_lend);
                title.setText(R.string.lend);
                break;
            case 5:
                icon.setImageResource(R.drawable.ic_lend_back);
                title.setText(R.string.lend_back);
                break;
            default:
                break;
        }
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
            if (dialogMoney.getText().toString().length() > 0) {
                double money = Double.parseDouble(dialogMoney.getText().toString());
                String info = dialogInfo.getText().toString();
                History history = new History(1, money, selectTime, info, type);
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
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, this, hour, minute, true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hourOfDay, minute);
        selectTime = calendar.getTimeInMillis();
        String time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(selectTime));
        setText.setText(time);
        if (setText == from) {
            fromTime = selectTime;
            cr.setFromTime(fromTime);
        } else if (setText == to) {
            toTime = selectTime;
        }
        refresh();
    }

}