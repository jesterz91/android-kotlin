package io.github.jesterz91.qrcode

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_scan.*
import org.jetbrains.anko.toast

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        button.setOnClickListener {
            // QR 코드 스캔 인텐트
            IntentIntegrator(this)
                .setOrientationLocked(false)
                .initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 스캔 결과
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                toast("Cancelled")
            } else {
                toast("Scanned : ${result.contents}")
                textView.text = result.contents
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
