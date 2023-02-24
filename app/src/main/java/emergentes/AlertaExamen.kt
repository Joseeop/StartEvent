package emergentes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.startevent.R
import com.example.startevent.databinding.ActivityAlertaExamenBinding

class AlertaExamen(context: Context) : AlertDialog(context) {

    private lateinit var binding: ActivityAlertaExamenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlertaExamenBinding.inflate(LayoutInflater.from(context))
        setView(binding.root)

        setTitle(context.resources.getString(R.string.menu_becamepremium))

        val supportTech = CheckBox(context)
        supportTech.text = context.resources.getString(R.string.examenSoporte)
        binding.linearLayout.addView(supportTech)

        val premiumExp = CheckBox(context)
        premiumExp.text = context.resources.getString(R.string.examenPremium)
        binding.linearLayout.addView(premiumExp)

        val companyCharity = CheckBox(context)
        companyCharity.text = context.resources.getString(R.string.examenCaridad)
        binding.linearLayout.addView(companyCharity)

        setButton(BUTTON_POSITIVE, context.resources.getString(R.string.examenComprar)) { _, _ ->
            val selected = getSelectedOption(supportTech, premiumExp, companyCharity)
            if (selected == null) {
                Toast.makeText(context, context.resources.getString(R.string.seleccion), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Ha seleccionado: $selected", Toast.LENGTH_SHORT).show()
            }
        }
        setButton(BUTTON_NEGATIVE, context.resources.getString(R.string.examenCancelar)) { dialog, _ -> dialog.dismiss() }
    }

    private fun getSelectedOption(
        supportTech: CheckBox,
        premiumExp: CheckBox,
        companyCharity: CheckBox
    ): String? {
        if (supportTech.isChecked && !premiumExp.isChecked && !companyCharity.isChecked) {
            return supportTech.text.toString()
        } else if (!supportTech.isChecked && premiumExp.isChecked && !companyCharity.isChecked) {
            return premiumExp.text.toString()
        } else if (!supportTech.isChecked && !premiumExp.isChecked && companyCharity.isChecked) {
            return companyCharity.text.toString()
        } else {
            return null
        }
    }

}
