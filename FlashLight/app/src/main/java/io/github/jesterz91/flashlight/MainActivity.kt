package io.github.jesterz91.flashlight

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val torch = Torch(this)

        flashSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                startService(intentFor<TorchService>().setAction("on"))
                //torch.flashOn()
            } else {
                startService(intentFor<TorchService>().setAction("off"))
                //torch.flashOff()
            }
        }

    }
}
