package org.hamroh.hisob.ui.About.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.hamroh.hisob.Classes.Sponsor;
import org.hamroh.hisob.R;
import org.hamroh.hisob.ui.About.Adapters.SponsorAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class HomiyFragment extends Fragment {

    private ListView listSponsors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_homiy, null);

        listSponsors = root.findViewById(R.id.listSponsors);
        ImageButton back = root.findViewById(R.id.back);

        back.setOnClickListener(l -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.aboutContainer, new MainFragment()).commit();
            }
        });

        setData();

        return root;
    }

    private void setData() {
        List<Sponsor> data = new ArrayList<>();
        Sponsor sponsor = new Sponsor();
        sponsor.setImageResource(R.drawable.hamroh);
        sponsor.setName("Hamroh");
        sponsor.setLink("play.google.com/store/apps/dev?id=5472497613574924720");
        data.add(sponsor);

        sponsor = new Sponsor();
        sponsor.setImageResource(R.drawable.create_soft);
        sponsor.setName("CreateSoft LLC");
        sponsor.setLink("createsoft.uz");
        data.add(sponsor);

        sponsor = new Sponsor();
        sponsor.setImageResource(R.drawable.telegram);
        sponsor.setName("Dasturchi");
        sponsor.setDesc("@Shahzod_Abdinazarov");
        sponsor.setLink("t.me/Shahzod_Abdinazarov");
        data.add(sponsor);

        if (getActivity() != null) {
            SponsorAdapter adapter = new SponsorAdapter(getActivity(), data);
            listSponsors.setAdapter(adapter);
        }
    }
}
