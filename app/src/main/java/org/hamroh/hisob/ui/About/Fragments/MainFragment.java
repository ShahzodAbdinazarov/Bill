package org.hamroh.hisob.ui.About.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import org.hamroh.hisob.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("InflateParams")
public class MainFragment extends Fragment {

    private List<String> data = new ArrayList<>();
    private ListView listSavol;
    private TextInputEditText edtSearch;
    private List<String> searchData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_main, null);

        ImageView close = root.findViewById(R.id.close);
        ImageView search = root.findViewById(R.id.search);
        Button sponsors = root.findViewById(R.id.sponsors);
        listSavol = root.findViewById(R.id.listSavol);
        edtSearch = root.findViewById(R.id.edtSearch);

        close.setOnClickListener(l -> Objects.requireNonNull(getActivity()).finish());
        search.setOnClickListener(l -> searchSavol());
        sponsors.setOnClickListener(l -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.aboutContainer, new HomiyFragment()).commit();
            }
        });

        refresh();

        searchSavol();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSavol();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listSavol.setOnItemClickListener((parent, view, position, id) -> {
            Fragment fragment = new SavolFragment();
            Bundle bundle = new Bundle();
            bundle.putString("savol", searchData.get(position));
            fragment.setArguments(bundle);
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.aboutContainer, fragment).commit();
            }
        });

        return root;
    }

    private void refresh() {
        data.add("Bu o'zi qanday dastur?");
        data.add("Dasturdan qanday foydalaniladi?");
        data.add("Kirim qanday kiritiladi?");
        data.add("Kiritilgan hisob qanday o'chiriladi?");
        data.add("Hisob qanday o'zgartiriladi?");
        data.add("Izoh qayerdan ko'riladi?");
        data.add("Vaqt bo'yicha qanday filterlanadi?");
        data.add("Hisob turi bo'yicha qanday filterlanadi?");
        data.add("Men qanday homiylik qilishim mumkin?");

        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
            listSavol.setAdapter(adapter);
        }
    }

    private void searchSavol() {
        searchData = new ArrayList<>();
        String text = (edtSearch.getText() != null) ? edtSearch.getText().toString().toLowerCase() : "";
        for (int i = 0; i < data.size(); i++) {
            String search = data.get(i).toLowerCase();
            if (search.contains(text)) {
                searchData.add(data.get(i));
            }
        }

        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, searchData);
            listSavol.setAdapter(adapter);
        }
    }
}
