package com.ademir.exotransitionin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ademir.exotransitionin.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun onStop() {
        super.onStop()
        PlayerSingleton.getInstance(applicationContext).stop(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerSingleton.getInstance(applicationContext).release()
    }

    fun navigate(destination: Fragment, sharedView: View? = null) {
        val tag = destination::class.java.canonicalName
        supportFragmentManager.beginTransaction().run {
            setReorderingAllowed(true)

            if (sharedView != null) {
                addSharedElement(sharedView, sharedView.transitionName)
            }

            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            add(R.id.main_container, destination, tag)
            supportFragmentManager.findFragmentById(R.id.main_fragment)?.let(::hide)
            addToBackStack(tag)
            commit()
        }
    }

}
