package com.ademir.exotransitionin

import android.content.Context
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer

class PlayerSingleton private constructor() {

    companion object {
        private var instance: SimpleExoPlayer? = null
        fun getInstance(context: Context): SimpleExoPlayer {
            val i = instance
            if (i != null) {
                return i
            }

            return synchronized(this) {
                val i2 = instance
                if (i2 != null) {
                    i2
                } else {
                    val created = ExoPlayerFactory.newSimpleInstance(context)
                    instance = created
                    created
                }
            }
        }

        fun destroy() {
            instance = null
        }

    }

}