package io.github.jesterz91.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

/**
 *  안드로이드 6.0(API 23) 이상에서 제공하는 방법으로 플래시 사용
 */

class Torch(context : Context) {
    private var cameraId : String? = null // 카메라를 켜고 끌 때 카메라 ID가 필요
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    init {
        cameraId = getCameraId()
    }

    fun flashOn(){
        cameraManager.setTorchMode(cameraId, true)

    }
    fun flashOff(){
        cameraManager.setTorchMode(cameraId, false)

    }

    // 카메라의 id를 얻는 메서드
    private fun getCameraId(): String? {
        // CameraManager 는 기기가 가지고 있는 모든 카메라에 대한 정보 목록을 제공
        val cameraIds = cameraManager.cameraIdList

        for (id in cameraIds) {
            val info = cameraManager.getCameraCharacteristics(id)

            // 플래시 가능 여부와 렌즈의 방향
            val flashAvailable = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)

            if ( flashAvailable != null
                && flashAvailable
                && lensFacing != null
                && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                return id
            }

        }

        return null
    }
}