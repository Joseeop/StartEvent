package com.example.startevent



import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
/**

Actividad para procesar pagos a través de Stripe utilizando el Payment Sheet.

Se conecta con el backend a través de una solicitud HTTP POST para obtener el secreto del cliente.

Permite al usuario pagar utilizando el Payment Sheet y maneja los resultados de la transacción.

@property paymentIntentClientSecret el secreto del cliente proporcionado por el backend para procesar el pago

@property paymentSheet el objeto PaymentSheet que se utiliza para presentar la pantalla de pago

@property payButton el botón que el usuario debe presionar para iniciar el proceso de pago

@constructor crea la actividad e inicializa los elementos de la interfaz de usuario y el backend
 */
class CheckoutActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "CheckoutActivity"
        private const val BACKEND_URL = "http://10.0.2.2:3001"
    }

    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentSheet: PaymentSheet

    private lateinit var payButton: Button







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Hook up the pay button
        payButton = findViewById(R.id.pay_button)
        payButton.setOnClickListener(::onPayClicked)
        payButton.isEnabled = false
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        fetchPaymentIntent()


    }
    /**

    Realiza una solicitud HTTP POST al backend para obtener el secreto del cliente necesario para procesar el pago.

    El secreto se almacena en la variable paymentIntentClientSecret y se habilita el botón de pago.
     */
    private fun fetchPaymentIntent() {
        val url = "$BACKEND_URL/create-payment-intent"

        val shoppingCartContent = """
            {
                "items": [
                    {"id":"xl-tshirt"}
                ]
            }
        """

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val body = shoppingCartContent.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient()
            .newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    showAlert("Failed to load data", "Error: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        showAlert("Failed to load page", "Error: $response")
                    } else {
                        val responseData = response.body?.string()
                        val responseJson = responseData?.let { JSONObject(it) } ?: JSONObject()
                        paymentIntentClientSecret = responseJson.getString("clientSecret")
                        runOnUiThread { payButton.isEnabled = true }
                        //Log.i(TAG, "Retrieved PaymentIntent")
                    }
                }
            })
    }
    /**

    Muestra una alerta al usuario con el título y el mensaje proporcionados.
    @param title el título de la alerta
    @param message el mensaje de la alerta
     */
    private fun showAlert(title: String, message: String? = null) {
        if (!isFinishing) {
            runOnUiThread {
                val builder = AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                builder.setPositiveButton("Ok", null)
                builder.create().show()
            }
        }
    }
    /**

    Displays a toast message with the provided message string.
    @param message the message string to be displayed
     */
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
        }
    }

    private fun onPayClicked(view: View) {
        val configuration = PaymentSheet.Configuration("Example, Inc.")

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }
    /**
     *

    Maneja el resultado de la hoja de pago mostrando un mensaje Toast si el pago se completó correctamente,
    mostrando un cuadro de diálogo de alerta si hubo un error, o no haciendo nada si el pago fue cancelado.
    @param paymentResult el resultado de la hoja de pago
     */
    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                showToast(R.string.pagoOK.toString())
            }
            is PaymentSheetResult.Canceled -> {
                //Log.i(TAG, "Payment canceled!")
            }
            is PaymentSheetResult.Failed -> {
                showAlert("Payment failed", paymentResult.error.localizedMessage)
            }
        }
    }


}