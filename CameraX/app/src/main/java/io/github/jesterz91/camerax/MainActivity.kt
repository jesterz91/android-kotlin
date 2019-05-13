package io.github.jesterz91.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import org.jetbrains.anko.toast
import java.io.File


class MainActivity : AppCompatActivity(), AnkoLogger {

    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 카메라 권한체크
        if (allPermissionsGranted()) {
            viewFinder.post {
                startCamera()
            }
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // 레이아웃 변경 반영
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }

    private fun startCamera() {
        // 카메라 미리보기 설정
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(640, 640))
        }.build()

        // 프리뷰 객체 생성
        val preview = Preview(previewConfig)

        // 뷰 파인더가 업데이트 될 때마다 레이아웃을 다시 계산
        preview.setOnPreviewOutputUpdateListener {
            // SurfaceTexture 를 업데이트하려면 제거하고 다시 추가
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)
            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        // 이미지 캡처 설정
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setTargetAspectRatio(Rational(1, 1))
                // 가로 세로 비율과 요청 된 모드에 기반한 해상도
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            }.build()

        // 이미지 캡처 객체 생성
        val imageCapture = ImageCapture(imageCaptureConfig)

        // 카메라 버튼 클릭 리스너설정
        captureButton.setOnClickListener {
            val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")

            imageCapture.takePicture(file,
                object : ImageCapture.OnImageSavedListener {
                    override fun onError(error: ImageCapture.UseCaseError, message: String, exc: Throwable?) {
                        val msg = "사진 캡처 실패 : $message"
                        toast(msg)
                        error { msg }
                        exc?.printStackTrace()
                    }

                    override fun onImageSaved(file: File) {
                        val msg = "사진 저장 성공: ${file.absolutePath}"
                        toast(msg)
                        debug { msg }
                    }
                })
        }
        // 생명주기 바인딩
        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun updateTransform() {
        val matrix = Matrix()
        // 뷰 파인더의 중심 계산
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // 디스플레이 회전을 고려하여 미리보기 출력을 수정
        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        // TextureView 에 적용
        viewFinder.setTransform(matrix)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                toast("카메라 사용권한이 거부되었습니다.")
                finish()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
