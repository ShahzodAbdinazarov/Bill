package org.hamroh.hisob.ui.about.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.hamroh.hisob.R
import org.hamroh.hisob.data.Savol

@SuppressLint("InflateParams")
class SavolFragment : Fragment() {
    private val data: MutableList<Savol> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about_savol, null)
        val back = root.findViewById<ImageButton>(R.id.back)
        val txtSavol = root.findViewById<TextView>(R.id.txtSavol)
        val txtJavob = root.findViewById<TextView>(R.id.txtJavob)
        val search = if (arguments != null) requireArguments().getString("savol", "") else ""
        setData()
        txtSavol.text = search
        var javob = ""
        for (i in data.indices) {
            if (search == data[i].savol) {
                javob = data[i].javob
                break
            }
        }
        txtJavob.text = javob
        back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.aboutContainer, MainFragment()).commit()
        }
        return root
    }

    private fun setData() {
        data.clear()
        var savol = Savol()
        savol.savol = ("Bu o'zi qanday dastur?")
        savol.javob = (
                "    Bu dastur yordamida siz o'z mablag'ingizni nazort qilasiz. " +
                        "Mablag'ingizni qayerga sarfladinggiz, kimga qarz berdingiz, kimdan qarz oldinggiz, " +
                        "ma'um muddat ichidagi daromadinggiz va harajatingizni kuzata olasiz va " +
                        "hisobingizdagi mablag'ingiz tahminan necha kunga yetishini ko'rib turishingiz mumkin."
                )
        data.add(savol)
        savol = Savol()
        savol.savol = ("Dasturdan qanday foydalaniladi?")
        savol.javob = (
                "    Dasturdan birinchi marta foydalanayotgan bo'lsangiz hisobingizdagi jami " +
                        "mablag'ingizni kirim qilib kitritib olishingiz kerak. Undan so'ng esa har bir kirim chiqimni," +
                        " qarz oldi berdini kiritib borishingiz kerak bo'ladi."
                )
        data.add(savol)
        savol = Savol()
        savol.savol = ("Kirim qanday kiritiladi?")
        savol.javob = (
                "    Kirim, chiqim, qarz olish, qaytarib berish, qarz berish va qaytarib olish " +
                        "kabilarni pastki o'ng burchakdagi + tugmani bosish orqali kiritishingiz mumkin."
                )
        data.add(savol)
        savol = Savol()
        savol.savol = ("Kiritilgan hisob qanday o'chiriladi?")
        savol.javob = (
                "    Kiritigan hisobingizni o'chirmoqchi bo'lsangiz uning ustiga bosib turing. " +
                        "Shunda dastur sizdan hisobni o'chirish uchun ruxsat so'raydi."
                )
        data.add(savol)
        savol = Savol()
        savol.savol = ("Hisob qanday o'zgartiriladi?")
        savol.javob = (
                "    Hisobni xato kiritigan bo'lsangiz uni o'chirib qaytadan kiritish lozim. " +
                        "Yangilash funksiyasi hozircha mavjud emas."
                )
        data.add(savol)
        savol = Savol()
        savol.savol = ("Izoh qayerdan ko'riladi?")
        savol.javob = (
                "    Kiritilgan hisobdagi izohlar dastlabki holatda yashirin bo'ladi. " +
                        "Uni ko'rish uchun usha hisobni ustiga bir marta bosishingiz kerak. Shundan " +
                        "so'ng izoh sizga ko'rinadi. Uni yana yashirish uchun usha hisob ustiga yana bir " +
                        "marta bosishingiz kerak bo'ladi."
                )
        data.add(savol)
        savol = Savol()
        savol.savol = ("Vaqt bo'yicha qanday filterlanadi?")
        savol.javob = (
                "    Vaqt bo'yicha filterlash uchun teppadagi ikkita vaqt ya'ni dastlabki va " +
                        "oxirgi vaqtni kiritasiz. Shunda dastur sizga usha oraliqdagi hisoblarni usha " +
                        "oraliqdagi farqni va jamiki natijalarni ko'rsatadi. Dastlabki vaqtni kiritganingizda " +
                        "dastur uni eslab qoladi. Dasturdan chiqib kirganingizda yana qayta kiritihsingiz mumkin."
                )
        data.add(savol)
        savol = Savol()
        savol.savol = ("Hisob turi bo'yicha qanday filterlanadi?")
        savol.javob = (
                "    Siz dastur yordamida faqat qarzingiz yoki chiqimingiz kabilarni ko'rmoqchi " +
                        "bo'lsangiz. Pastga qaragan uchburchakni bosing. Shunda filterlash oynasi ochiladi. " +
                        "Usha oynadan o'zingizga qulay usulda dasturni filterlashingiz mumkin bo'ladi. " +
                        "Filter dasturdan chiqib qayta kirganingizda dastlabki holatga qaytadi."
                )
        savol = Savol()
        savol.savol = ("Men qanday homiylik qilishim mumkin?")
        savol.javob = (
                "    Siz homiylik qilish uchun homiylar bo'limidagi dasturchini kontaktiga " +
                        "telegram orqali murojaat qilishingiz mumkin."
                )
        data.add(savol)
    }
}
