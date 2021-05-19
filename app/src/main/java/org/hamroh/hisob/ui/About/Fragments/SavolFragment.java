package org.hamroh.hisob.ui.About.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.hamroh.hisob.Classes.Savol;
import org.hamroh.hisob.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class SavolFragment extends Fragment {

    private final List<Savol> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_savol, null);

        ImageButton back = root.findViewById(R.id.back);
        TextView txtSavol = root.findViewById(R.id.txtSavol);
        TextView txtJavob = root.findViewById(R.id.txtJavob);
        String search = getArguments() != null ? getArguments().getString("savol", "") : "";
        setData();

        txtSavol.setText(search);

        String javob = "";

        for (int i = 0; i < data.size(); i++) {
            if (search.equals(data.get(i).getSavol())) {
                javob = data.get(i).getJavob();
                break;
            }
        }

        txtJavob.setText(javob);

        back.setOnClickListener(l -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.aboutContainer, new MainFragment()).commit();
            }
        });

        return root;
    }

    private void setData() {
        data.clear();
        Savol savol = new Savol();
        savol.setSavol("Bu o'zi qanday dastur?");
        savol.setJavob("    Bu dastur yordamida siz o'z mablag'ingizni nazort qilasiz. " +
                "Mablag'ingizni qayerga sarfladinggiz, kimga qarz berdingiz, kimdan qarz oldinggiz, " +
                "ma'um muddat ichidagi daromadinggiz va harajatingizni kuzata olasiz va " +
                "hisobingizdagi mablag'ingiz tahminan necha kunga yetishini ko'rib turishingiz mumkin.");
        data.add(savol);

        savol = new Savol();
        savol.setSavol("Dasturdan qanday foydalaniladi?");
        savol.setJavob("    Dasturdan birinchi marta foydalanayotgan bo'lsangiz hisobingizdagi jami " +
                "mablag'ingizni kirim qilib kitritib olishingiz kerak. Undan so'ng esa har bir kirim chiqimni," +
                " qarz oldi berdini kiritib borishingiz kerak bo'ladi.");
        data.add(savol);

        savol = new Savol();
        savol.setSavol("Kirim qanday kiritiladi?");
        savol.setJavob("    Kirim, chiqim, qarz olish, qaytarib berish, qarz berish va qaytarib olish " +
                "kabilarni pastki o'ng burchakdagi + tugmani bosish orqali kiritishingiz mumkin.");
        data.add(savol);

        savol = new Savol();
        savol.setSavol("Kiritilgan hisob qanday o'chiriladi?");
        savol.setJavob("    Kiritigan hisobingizni o'chirmoqchi bo'lsangiz uning ustiga bosib turing. " +
                "Shunda dastur sizdan hisobni o'chirish uchun ruxsat so'raydi.");
        data.add(savol);

        savol = new Savol();
        savol.setSavol("Hisob qanday o'zgartiriladi?");
        savol.setJavob("    Hisobni xato kiritigan bo'lsangiz uni o'chirib qaytadan kiritish lozim. " +
                "Yangilash funksiyasi hozircha mavjud emas.");
        data.add(savol);

        savol = new Savol();
        savol.setSavol("Izoh qayerdan ko'riladi?");
        savol.setJavob("    Kiritilgan hisobdagi izohlar dastlabki holatda yashirin bo'ladi. " +
                "Uni ko'rish uchun usha hisobni ustiga bir marta bosishingiz kerak. Shundan " +
                "so'ng izoh sizga ko'rinadi. Uni yana yashirish uchun usha hisob ustiga yana bir " +
                "marta bosishingiz kerak bo'ladi.");
        data.add(savol);

        savol = new Savol();
        savol.setSavol("Vaqt bo'yicha qanday filterlanadi?");
        savol.setJavob("    Vaqt bo'yicha filterlash uchun teppadagi ikkita vaqt ya'ni dastlabki va " +
                "oxirgi vaqtni kiritasiz. Shunda dastur sizga usha oraliqdagi hisoblarni usha " +
                "oraliqdagi farqni va jamiki natijalarni ko'rsatadi. Dastlabki vaqtni kiritganingizda " +
                "dastur uni eslab qoladi. Dasturdan chiqib kirganingizda yana qayta kiritihsingiz mumkin.");
        data.add(savol);

        savol = new Savol();
        savol.setSavol("Hisob turi bo'yicha qanday filterlanadi?");
        savol.setJavob("    Siz dastur yordamida faqat qarzingiz yoki chiqimingiz kabilarni ko'rmoqchi " +
                "bo'lsangiz. Pastga qaragan uchburchakni bosing. Shunda filterlash oynasi ochiladi. " +
                "Usha oynadan o'zingizga qulay usulda dasturni filterlashingiz mumkin bo'ladi. " +
                "Filter dasturdan chiqib qayta kirganingizda dastlabki holatga qaytadi.");

        savol = new Savol();
        savol.setSavol("Men qanday homiylik qilishim mumkin?");
        savol.setJavob("    Siz homiylik qilish uchun homiylar bo'limidagi dasturchini kontaktiga " +
                "telegram orqali murojaat qilishingiz mumkin.");
        data.add(savol);
    }
}
