package io.github.jesterz91.qrcode

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_generator.*
import org.jetbrains.anko.toast

class GeneratorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)

        button.setOnClickListener {
            val text = editText.text.toString()

            if (text.isEmpty()) {

                toast("문자를 입력하세요")

            } else {

                // 입력된 문자를 이용하여 QR 코드 생성
                val multiFormatWriter = MultiFormatWriter()
                val bitMatrix: BitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

                // 생성된 QR 코드를 표시
                imageView.setImageBitmap(bitmap)
            }

        }
    }
}
