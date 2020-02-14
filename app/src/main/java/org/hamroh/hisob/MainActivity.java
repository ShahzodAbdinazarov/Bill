package org.hamroh.hisob;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.hamroh.hisob.Classes.Filter;
import org.hamroh.hisob.Classes.History;
import org.hamroh.hisob.ui.About.AboutActivity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint({"SetTextI18n", "NewApi"})
public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    final boolean[] is = {true, true, true, true, true, true, true};
    private ListView history;
    private double money = 0;
    private TextView amount, current, income, outcome, from, to, setText;
    private int year, month, day;
    private DBHelper db;
    private Controller cr;
    private long toTime, fromTime, selectTime = Calendar.getInstance().getTimeInMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout expendLayout = findViewById(R.id.expendLayout);
        ImageView btnQuestion = findViewById(R.id.btnQuestion);
        ImageView expend = findViewById(R.id.expend);
        amount = findViewById(R.id.amount);
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
        btnQuestion.setOnClickListener(l -> startActivity(new Intent(this, AboutActivity.class)));
        expendLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        final boolean[] more = {true};
        expend.setOnClickListener(l -> {
            if (more[0]) {
                expend.setImageResource(R.drawable.ic_expand_less);
                expendLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                expend.setImageResource(R.drawable.ic_expand_more);
                expendLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            }
            more[0] = !more[0];
        });
        clickExpends();

    }

    private void clickExpends() {
        ImageView zeroFilter = findViewById(R.id.zeroFilter);
        ImageView oneFilter = findViewById(R.id.oneFilter);
        ImageView twoFilter = findViewById(R.id.twoFilter);
        ImageView threeFilter = findViewById(R.id.threeFilter);
        ImageView fourFilter = findViewById(R.id.fourFilter);
        ImageView fiveFilter = findViewById(R.id.fiveFilter);
        ImageView sixFilter = findViewById(R.id.sixFilter);

        zeroFilter.setOnClickListener(l -> {
            if (is[0]) {
                zeroFilter.setColorFilter(Color.parseColor("#888888"));
                oneFilter.setColorFilter(Color.parseColor("#888888"));
                twoFilter.setColorFilter(Color.parseColor("#888888"));
                threeFilter.setColorFilter(Color.parseColor("#888888"));
                fourFilter.setColorFilter(Color.parseColor("#888888"));
                fiveFilter.setColorFilter(Color.parseColor("#888888"));
                sixFilter.setColorFilter(Color.parseColor("#888888"));
                is[0] = false;
                is[1] = false;
                is[2] = false;
                is[3] = false;
                is[4] = false;
                is[5] = false;
                is[6] = false;
            } else {
                zeroFilter.setColorFilter(Color.parseColor("#ff00ff"));
                oneFilter.setColorFilter(Color.parseColor("#10C040"));
                twoFilter.setColorFilter(Color.parseColor("#C01010"));
                threeFilter.setColorFilter(Color.parseColor("#DDDD22"));
                fourFilter.setColorFilter(Color.parseColor("#DDDD22"));
                fiveFilter.setColorFilter(Color.parseColor("#002299"));
                sixFilter.setColorFilter(Color.parseColor("#002299"));
                is[0] = true;
                is[1] = true;
                is[2] = true;
                is[3] = true;
                is[4] = true;
                is[5] = true;
                is[6] = true;
            }
            refresh();
        });
        oneFilter.setOnClickListener(l -> {
            if (is[2]) {
                oneFilter.setColorFilter(Color.parseColor("#888888"));
                is[2] = false;
            } else {
                oneFilter.setColorFilter(Color.parseColor("#10C040"));
                is[2] = true;
            }
            refresh();
        });
        twoFilter.setOnClickListener(l -> {
            if (is[1]) {
                twoFilter.setColorFilter(Color.parseColor("#888888"));
                is[1] = false;
            } else {
                twoFilter.setColorFilter(Color.parseColor("#C01010"));
                is[1] = true;
            }
            refresh();
        });
        threeFilter.setOnClickListener(l -> {
            if (is[3]) {
                threeFilter.setColorFilter(Color.parseColor("#888888"));
                is[3] = false;
            } else {
                threeFilter.setColorFilter(Color.parseColor("#DDDD22"));
                is[3] = true;
            }
            refresh();
        });
        fourFilter.setOnClickListener(l -> {
            if (is[4]) {
                fourFilter.setColorFilter(Color.parseColor("#888888"));
                is[4] = false;
            } else {
                fourFilter.setColorFilter(Color.parseColor("#DDDD22"));
                is[4] = true;
            }
            refresh();
        });
        fiveFilter.setOnClickListener(l -> {
            if (is[5]) {
                fiveFilter.setColorFilter(Color.parseColor("#888888"));
                is[5] = false;
            } else {
                fiveFilter.setColorFilter(Color.parseColor("#002299"));
                is[5] = true;
            }
            refresh();
        });
        sixFilter.setOnClickListener(l -> {
            if (is[6]) {
                sixFilter.setColorFilter(Color.parseColor("#888888"));
                is[6] = false;
            } else {
                sixFilter.setColorFilter(Color.parseColor("#002299"));
                is[6] = true;
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

        txtOutgoing.setOnClickListener(l -> showInputDialog(0, dialog));
        txtIncome.setOnClickListener(l -> showInputDialog(1, dialog));
        txtBorrow.setOnClickListener(l -> showInputDialog(2, dialog));
        txtBorrowBack.setOnClickListener(l -> showInputDialog(3, dialog));
        txtLend.setOnClickListener(l -> showInputDialog(4, dialog));
        txtLendBack.setOnClickListener(l -> showInputDialog(5, dialog));

        dialog.show();
    }

    public void showInputDialog(int type, Dialog d) {
        d.cancel();
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
                double edtMoney = Double.parseDouble(dialogMoney.getText().toString());
                if (edtMoney < money || type == 1 || type == 2 || type == 5) {
                    String info = dialogInfo.getText().toString();
                    History history = new History(1, edtMoney, selectTime, info, type);
                    db.add(history);
                    dialog.dismiss();
                    recreate();
                } else {
                    dialogMoney.setError("Yetarli mablag' mavjud emas!");
                    dialogMoney.requestFocus();
                }
            } else {
                dialogMoney.setError("Mablag'ni kiriting!");
                dialogMoney.requestFocus();
            }
        });

        dialog.show();
    }

    private void refresh() {
        Filter filter = db.getIncome(fromTime, toTime);
        double in = 0;
        double out = 0;
        in += filter.getUp();
        out += filter.getDown();
        in += filter.getBorrow();
        out += filter.getBorrowBack();
        out += filter.getLend();
        in += filter.getLendBack();
        money = in - out;
        int day = (int) Math.floor(money / db.getDailyAmount());
        amount.setText("Tahminan " + day + " kunga yetadi.");

        in = 0;
        out = 0;

        if (is[2]) {
            in += filter.getUp();
        }

        if (is[1]) {
            out += filter.getDown();
        }

        if (is[3]) {
            in += filter.getBorrow();
        }

        if (is[4]) {
            out += filter.getBorrowBack();
        }

        if (is[5]) {
            out += filter.getLend();
        }

        if (is[6]) {
            in += filter.getLendBack();
        }

        NumberFormat decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String txtMoney = decimalFormat.format(in);
        income.setText(txtMoney);

        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        txtMoney = decimalFormat.format(out);
        outcome.setText(txtMoney);

        Double number = in - out;
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        txtMoney = decimalFormat.format(number);
        current.setText(txtMoney);

        HistoryAdapter adapter = new HistoryAdapter(this, db.getAll(fromTime, toTime, is));
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

    public void setSetText(TextView setText) {
        this.setText = setText;
    }

}