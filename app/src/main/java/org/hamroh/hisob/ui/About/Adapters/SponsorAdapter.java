package org.hamroh.hisob.ui.About.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.hamroh.hisob.Classes.Sponsor;
import org.hamroh.hisob.R;

import java.util.List;

@SuppressLint({"InflateParams", "ViewHolder"})
public class SponsorAdapter extends BaseAdapter {

    private Activity activity;
    private List<Sponsor> data;
    private LayoutInflater inflater;

    public SponsorAdapter(Activity activity, List<Sponsor> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = inflater.inflate(R.layout.row_sponsor, null);

        ImageView imgSponsor = root.findViewById(R.id.imgSponsor);
        TextView txtNameSponsor = root.findViewById(R.id.txtNameSponsor);
        TextView txtDescSponsor = root.findViewById(R.id.txtDescSponsor);

        imgSponsor.setImageResource(data.get(position).getImageResource());
        txtNameSponsor.setText(data.get(position).getName());
        txtDescSponsor.setText(data.get(position).getDesc());

        root.setOnClickListener(l -> {
            String url = data.get(position).getLink();
            if (!data.get(position).getLink().startsWith("http")) {
                url = "http://" + url;
            }
            Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(link);
        });

        return root;
    }
}
