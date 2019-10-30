package uz.qmii.bill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private List<History> data;
    private LayoutInflater inflater;
    private DBHelper db;

    HistoryAdapter(Context context, List<History> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DBHelper(context);
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

        if (data.get(position).isIncome) {
            rowImage.setImageResource(R.drawable.up);
        }

        Double number = data.get(position).getMoney();
        NumberFormat decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String money = decimalFormat.format(number);

        txtMoney.setText(money);
        txtTime.setText(data.get(position).getTime());

        root.setOnClickListener(l -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(money);
            if (data.get(position).isIncome) alert.setIcon(R.drawable.up);
            else alert.setIcon(R.drawable.down);
            alert.setMessage(data.get(position).getInfo());
            alert.setPositiveButton(android.R.string.ok, (dialog, whichButton) -> dialog.dismiss());
            alert.show();
        });

        root.setOnLongClickListener(l -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(money);
            if (data.get(position).isIncome) alert.setIcon(R.drawable.up);
            else alert.setIcon(R.drawable.down);
            alert.setMessage(R.string.delete);
            alert.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                db.delete(data.get(position).getId());
                dialog.dismiss();
            });
            alert.setNegativeButton(android.R.string.no, (dialog, whichButton) -> dialog.dismiss());
            alert.show();
            return true;
        });

        return root;
    }
}
