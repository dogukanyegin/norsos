package com.example.norsos.services

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Controls the camera flash for steady torch, rapid strobe, or SOS Morse code.
 * Safe for all devices: gracefully handles devices without a flash unit.
 */
class FlashlightController(private val context: Context) {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
    private var cameraId: String? = null
    private var strobeJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isTorchOn: Boolean = false
        private set

    @Volatile
    var isStrobing: Boolean = false
        private set

    init {
        findCameraWithFlash()
    }

    private fun findCameraWithFlash() {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return
        }
        try {
            val ids = cameraManager?.cameraIdList ?: emptyArray()
            for (id in ids) {
                val chars = cameraManager?.getCameraCharacteristics(id)
                val hasFlash = chars?.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
                val facing = chars?.get(CameraCharacteristics.LENS_FACING)
                if (hasFlash && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = id
                    break
                }
            }
            if (cameraId == null && ids.isNotEmpty()) {
                cameraId = ids.firstOrNull()
            }
        } catch (e: Exception) {
            Log.w("FlashlightController", "Failed to inspect camera characteristics", e)
        }
    }

    fun setTorch(enable: Boolean) {
        stopStrobe()
        val id = cameraId ?: return
        try {
            cameraManager?.setTorchMode(id, enable)
            isTorchOn = enable
        } catch (e: Exception) {
            Log.e("FlashlightController", "Torch mode error", e)
        }
    }

    fun startStrobe(intervalMs: Long = 120L) {
        stopStrobe()
        val id = cameraId ?: return
        isStrobing = true
        strobeJob = scope.launch {
            var state = false
            try {
                while (isActive && isStrobing) {
                    state = !state
                    try {
                        cameraManager?.setTorchMode(id, state)
                        isTorchOn = state
                    } catch (e: Exception) {
                        Log.w("FlashlightController", "Strobe toggle error", e)
                    }
                    delay(intervalMs)
                }
            } finally {
                try {
                    cameraManager?.setTorchMode(id, false)
                } catch (_: Exception) {}
                isTorchOn = false
                isStrobing = false
            }
        }
    }

    fun stopStrobe() {
        isStrobing = false
        strobeJob?.cancel()
        strobeJob = null
        val id = cameraId ?: return
        try {
            cameraManager?.setTorchMode(id, false)
        } catch (_: Exception) {}
        isTorchOn = false
    }

    fun turnOffAll() {
        stopStrobe()
    }
}
