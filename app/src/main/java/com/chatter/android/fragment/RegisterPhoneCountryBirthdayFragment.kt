package com.chatter.android.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.chatter.android.R
import com.chatter.android.activity.RegisterActivity
import com.google.android.material.textfield.TextInputLayout
import java.util.*

@Suppress("NAME_SHADOWING", "DEPRECATION")
class RegisterPhoneCountryBirthdayFragment : Fragment() {

    private val items: List<String> =
        "Afghanistan\nAlbania\nAlgeria\nAndorra\nAngola\nAntigua and Barbuda\nArgentina\nArmenia\nAustralia\nAustria\nAzerbaijan\nThe Bahamas\nBahrain\nBangladesh\nBarbados\nBelarus\nBelgium\nBelize\nBenin\nBhutan\nBolivia\nBosnia and Herzegovina\nBotswana\nBrazil\nBrunei\nBulgaria\nBurkina Faso\nBurundi\nCabo Verde\nCambodia\nCameroon\nCanada\nCentral African Republic\nChad\nChile\nChina\nColombia\nComoros\nCongo, Democratic Republic of the\nCongo, Republic of the\nCosta Rica\nCôte d’Ivoire\nCroatia\nCuba\nCyprus\nCzech Republic\nDenmark\nDjibouti\nDominica\nDominican Republic\nEast Timor (Timor-Leste)\nEcuador\nEgypt\nEl Salvador\nEquatorial Guinea\nEritrea\nEstonia\nEswatini\nEthiopia\nFiji\nFinland\nFrance\nGabon\nThe Gambia\nGeorgia\nGermany\nGhana\nGreece\nGrenada\nGuatemala\nGuinea\nGuinea-Bissau\nGuyana\nHaiti\nHonduras\nHungary\nIceland\nIndia\nIndonesia\nIran\nIraq\nIreland\nIsrael\nItaly\nJamaica\nJapan\nJordan\nKazakhstan\nKenya\nKiribati\nKorea, North\nKorea, South\nKosovo\nKuwait\nKyrgyzstan\nLaos\nLatvia\nLebanon\nLesotho\nLiberia\nLibya\nLiechtenstein\nLithuania\nLuxembourg\nMadagascar\nMalawi\nMalaysia\nMaldives\nMali\nMalta\nMarshall Islands\nMauritania\nMauritius\nMexico\nMicronesia, Federated States of\nMoldova\nMonaco\nMongolia\nMontenegro\nMorocco\nMozambique\nMyanmar (Burma)\nNamibia\nNauru\nNepal\nNetherlands\nNew Zealand\nNicaragua\nNiger\nNigeria\nNorth Macedonia\nNorway\nOman\nPakistan\nPalau\nPanama\nPapua New Guinea\nParaguay\nPeru\nPhilippines\nPoland\nPortugal\nQatar\nRomania\nRussia\nRwanda\nSaint Kitts and Nevis\nSaint Lucia\nSaint Vincent and the Grenadines\nSamoa\nSan Marino\nSao Tome and Principe\nSaudi Arabia\nSenegal\nSerbia\nSeychelles\nSierra Leone\nSingapore\nSlovakia\nSlovenia\nSolomon Islands\nSomalia\nSouth Africa\nSpain\nSri Lanka\nSudan\nSudan, South\nSuriname\nSweden\nSwitzerland\nSyria\nTaiwan\nTajikistan\nTanzania\nThailand\nTogo\nTonga\nTrinidad and Tobago\nTunisia\nTurkey\nTurkmenistan\nTuvalu\nUganda\nUkraine\nUnited Arab Emirates\nUnited Kingdom\nUnited States\nUruguay\nUzbekistan\nVanuatu\nVatican City\nVenezuela\nVietnam\nYemen\nZambia\nZimbabwe".split(
            "\n"
        ).toList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var selectCountry: AutoCompleteTextView

    private lateinit var phoneField: TextInputLayout
    private lateinit var birthdayDate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_register_phone_country_birthday, container, false)

        init(view)
        onLoad(view)

        return view
    }

    private fun init(view: View) {
        selectCountry = view.findViewById(R.id.country_list)
        phoneField = view.findViewById(R.id.phoneField)
        birthdayDate = view.findViewById(R.id.birthdayField)
    }

    @SuppressLint("SetTextI18n")
    private fun onLoad(view: View) {
        phoneField.editText?.setText(RegisterActivity.user.phone)
        selectCountry.setText(RegisterActivity.user.country)
        birthdayDate.text =
            "${RegisterActivity.user.birthdayDate.year}-${RegisterActivity.user.birthdayDate.month + 1}-${RegisterActivity.user.birthdayDate.date}"

        arrayAdapter = ArrayAdapter(view.context, R.layout.list_item, items)
        selectCountry.setAdapter(arrayAdapter)
        selectCountry.setOnItemClickListener { parent, _, position, _ ->
            val item: String = parent.getItemAtPosition(position).toString()
            RegisterActivity.user.country = item
        }

        phoneField.editText?.addTextChangedListener {
            RegisterActivity.user.phone = RegisterActivity.readField(phoneField)
        }

        birthdayDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                view.context,
                { _, year, monthOfYear, dayOfMonth ->
                    birthdayDate.text = "${year}-${monthOfYear + 1}-${dayOfMonth}"
                    RegisterActivity.user.birthdayDate =
                        Date(year, monthOfYear, dayOfMonth)
                }, year, month, day
            ).show()
        }
    }
}