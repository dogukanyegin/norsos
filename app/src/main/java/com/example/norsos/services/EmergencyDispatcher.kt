package com.example.norsos.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast

object EmergencyDispatcher {

    /**
     * Dials an emergency phone number using ACTION_DIAL.
     * Complies 100% with Google Play policy by not requiring CALL_PHONE runtime permission.
     */
    fun dialNumber(context: Context, rawNumber: String) {
        val cleanNumber = rawNumber.filter { it.isDigit() || it == '+' }
        if (cleanNumber.isEmpty()) {
            Toast.makeText(context, "Geçersiz telefon numarası", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$cleanNumber")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("EmergencyDispatcher", "Error launching dial intent", e)
            Toast.makeText(context, "Arama başlatılamadı: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Opens SMS app prefilled with recipient and emergency text.
     * Complies 100% with Google Play policy by using ACTION_SENDTO instead of restricted SEND_SMS permission.
     */
    fun sendSms(context: Context, rawNumber: String, messageText: String) {
        val cleanNumber = rawNumber.filter { it.isDigit() || it == '+' }
        try {
            val uri = if (cleanNumber.isNotEmpty()) {
                Uri.parse("smsto:$cleanNumber")
            } else {
                Uri.parse("smsto:")
            }
            val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
                putExtra("sms_body", messageText)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("EmergencyDispatcher", "Error launching SMS intent", e)
            // Fallback to general text share
            shareText(context, "ACİL DURUM SMS", messageText)
        }
    }

    /**
     * Shares the emergency alert and location via the standard Android Sharesheet (WhatsApp, SMS, etc.).
     */
    fun shareText(context: Context, title: String, text: String) {
        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_SUBJECT, title)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, title).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            Log.e("EmergencyDispatcher", "Error launching share intent", e)
            Toast.makeText(context, "Paylaşım açılamadı", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Triggers SOS Morse haptic vibration: ... --- ...
     */
    fun triggerSosVibration(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator?.hasVibrator() == true) {
                // Timing in ms: . . . - - - . . .
                // 150ms dot, 100ms pause, 400ms dash
                val timings = longArrayOf(
                    0, 150, 100, 150, 100, 150, 250,
                    400, 100, 400, 100, 400, 250,
                    150, 100, 150, 100, 150
                )
                val amplitudes = intArrayOf(
                    0, 255, 0, 255, 0, 255, 0,
                    255, 0, 255, 0, 255, 0,
                    255, 0, 255, 0, 255
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(timings, -1)
                }
            }
        } catch (e: Exception) {
            Log.w("EmergencyDispatcher", "Error vibrating device", e)
        }
    }
}
