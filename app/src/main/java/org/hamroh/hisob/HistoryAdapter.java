package org.hamroh.hisob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.hamroh.hisob.Classes.History;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends BaseAdapter {

    private final Activity activity;
    private final List<History> data;
    private final LayoutInflater inflater;
    private final DBHelper db;

    HistoryAdapter(Activity activity, List<History> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DBHelper(activity);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = inflater.inflate(R.layout.row, null);

        ImageView rowImage = root.findViewById(R.id.rowImage);
        TextView txtMoney = root.findViewById(R.id.txtMoney);
        TextView txtTime = root.findViewById(R.id.txtTime);
        TextView txtComment = root.findViewById(R.id.txtComment);
        switch (data.get(position).getType()) {
            case 0 -> rowImage.setImageResource(R.drawable.down);
            case 1 -> rowImage.setImageResource(R.drawable.up);
            case 2 -> rowImage.setImageResource(R.drawable.ic_borrow);
            case 3 -> rowImage.setImageResource(R.drawable.ic_borrow_back);
            case 4 -> rowImage.setImageResource(R.drawable.ic_lend);
            case 5 -> rowImage.setImageResource(R.drawable.ic_lend_back);
            default -> {
            }
        }
        Double number = data.get(position).getMoney();
        NumberFormat decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String money = decimalFormat.format(number);

        txtMoney.setText(money);
        txtTime.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                .format(new Date(data.get(position).getTime())));

        txtComment.setText(data.get(position).getInfo());
        txtComment.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        final boolean[] is = {true};
        root.setOnClickListener(l -> {
            if (is[0]) {
                txtComment.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                txtComment.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            }
            is[0] = !is[0];
        });


        root.setOnLongClickListener(l -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle(money);
            switch (data.get(position).getType()) {
                case 0 -> alert.setIcon(R.drawable.down);
                case 1 -> alert.setIcon(R.drawable.up);
                case 2 -> alert.setIcon(R.drawable.ic_borrow);
                case 3 -> alert.setIcon(R.drawable.ic_borrow_back);
                case 4 -> alert.setIcon(R.drawable.ic_lend);
                case 5 -> alert.setIcon(R.drawable.ic_lend_back);
                default -> {
                }
            }
            alert.setMessage(R.string.delete);
            alert.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                db.delete(data.get(position).getId());
                activity.recreate();
                dialog.dismiss();
            });
            alert.setNegativeButton(android.R.string.no, (dialog, whichButton) -> dialog.dismiss());
            alert.show();
            return true;
        });

        return root;
    }
}
