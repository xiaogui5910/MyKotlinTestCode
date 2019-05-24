package com.example.chenchenggui.mykotlintestcode.avdev

import android.media.AudioFormat

/**
 * description ：
 * author : chenchenggui
 * creation date: 2019/5/21
 */
class AudioConfig {
    companion object {
        /**
         * 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
         */
        const val SAMPLE_RATE_IN_HZ = 44100
        /**
         * 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
         */
        const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
        /**
         * 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
         */
        const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_16BIT
    }
}