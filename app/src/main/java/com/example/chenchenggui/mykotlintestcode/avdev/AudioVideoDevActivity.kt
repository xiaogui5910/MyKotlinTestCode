package com.example.chenchenggui.mykotlintestcode.avdev

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.media.*
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.SurfaceHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.BaseActivity
import com.example.chenchenggui.mykotlintestcode.avdev.AudioConfig.Companion.AUDIO_FORMAT
import com.example.chenchenggui.mykotlintestcode.avdev.AudioConfig.Companion.CHANNEL_CONFIG
import com.example.chenchenggui.mykotlintestcode.log
import kotlinx.android.synthetic.main.activity_audio_video_dev.*
import java.io.*
import java.lang.Exception

class AudioVideoDevActivity : BaseActivity() {
    private var isRecording = false
    private var audioRecord: AudioRecord? = null
    private var audioTrack: AudioTrack? = null

    override fun getLayoutId(): Int = R.layout.activity_audio_video_dev

    override fun initView() {
        super.initView()

        surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (holder == null) {
                    return
                }

                val paint = Paint().apply {
                    isAntiAlias = true
                    style = Paint.Style.STROKE
                }

                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.city)

                val canvas = holder.lockCanvas()//锁定当前画布
                canvas.drawBitmap(bitmap, 0f, 0f, paint)//绘制
                holder.unlockCanvasAndPost(canvas)//解除锁定并显示在界面上
            }
        })

        //开始或停止录音
        btn_start.setOnClickListener {
            if (btn_start.text.toString() == getString(R.string.av_dev_start_record)) {
                btn_start.text = getString(R.string.av_dev_stop_record)
                checkPermissions()
            } else {
                btn_start.text = getString(R.string.av_dev_start_record)
                stopRecord()
            }
        }
        //录音文件转换格式
        btn_convert.setOnClickListener {
            val pcmToWavUtil = PcmToWavUtil(AudioConfig.SAMPLE_RATE_IN_HZ, AudioConfig.CHANNEL_CONFIG, AudioConfig.AUDIO_FORMAT)
            val pcmFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm")
            val wavFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.wav")
            if (!wavFile.mkdirs()) {
                log("wavFile Directory not created")
            }
            if (wavFile.exists()) {
                wavFile.delete()
            }
            pcmToWavUtil.pcmToWav(pcmFile.absolutePath, wavFile.absolutePath)
            log("convert pcm to wav ... ")
        }
        //开始或停止播放录音
        btn_play.setOnClickListener {
            if (btn_play.text.toString() == getString(R.string.av_dev_start_play)) {
                btn_play.text = getString(R.string.av_dev_stop_play)
                startPlay()
            } else {
                btn_play.text = getString(R.string.av_dev_start_play)
                stopPlay()
            }
        }
    }

    private fun startPlay() {
        val minBufferSize = AudioTrack.getMinBufferSize(AudioConfig.SAMPLE_RATE_IN_HZ, AudioFormat
                .CHANNEL_OUT_MONO, AudioConfig.AUDIO_FORMAT)
        audioTrack =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AudioTrack(
                            AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build(),
                            AudioFormat.Builder()
                                    .setSampleRate(AudioConfig.SAMPLE_RATE_IN_HZ)
                                    .setEncoding(AudioConfig.AUDIO_FORMAT)
                                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                    .build(),
                            minBufferSize,
                            AudioTrack.MODE_STREAM,
                            AudioManager.AUDIO_SESSION_ID_GENERATE)
                } else {
                    AudioTrack(AudioManager.STREAM_MUSIC, AudioConfig.SAMPLE_RATE_IN_HZ,
                            AudioFormat.CHANNEL_OUT_MONO, AudioConfig.AUDIO_FORMAT, minBufferSize, AudioTrack.MODE_STREAM)
                }
        audioTrack?.play()

        val pcmFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm")
        try {
            val fis = FileInputStream(pcmFile)
            Thread(Runnable {
                try {
                    val buffer = ByteArray(minBufferSize)
                    while (fis.available() > 0) {
                        val readCount = fis.read(buffer)
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack
                                        .ERROR_BAD_VALUE) {
                            continue
                        }
                        if (readCount != 0 && readCount != -1) {
                            if (audioTrack?.state != AudioTrack.PLAYSTATE_STOPPED) {
                                audioTrack?.write(buffer, 0, readCount)
                            }
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun stopPlay() {
        if (audioTrack?.state != AudioTrack.STATE_UNINITIALIZED) {
            if (audioTrack?.state != AudioTrack.PLAYSTATE_STOPPED) {
                try {
                    log("AudioTrack stop ...")
                    audioTrack?.stop()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            log("AudioTrack release ...")
            audioTrack?.release()
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
        val permissionList = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach {
                if (ContextCompat.checkSelfPermission(applicationContext, it) != PackageManager
                                .PERMISSION_GRANTED) {
                    permissionList.add(it)
                }
            }
            permissionList.toArray()
            if (permissionList.isNotEmpty()) {
                ActivityCompat.requestPermissions(AudioVideoDevActivity@ this,
                        permissionList.toArray(Array(permissionList.size) {
                            "$it"
                        }), 100)
            } else {
                startRecord()
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var isChecked = true
        if (requestCode == 100) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    log(permissions[i] + " 权限被用户禁止！")
                    isChecked = false
                }
            }

            log("get permission success == $isChecked")

            if (isChecked) {
                startRecord()
            }
        }
    }

    private fun startRecord() {
        val minBufferSize = AudioRecord.getMinBufferSize(AudioConfig.SAMPLE_RATE_IN_HZ, AudioConfig
                .CHANNEL_CONFIG, AudioConfig.AUDIO_FORMAT)
        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, AudioConfig.SAMPLE_RATE_IN_HZ,
                AudioConfig.CHANNEL_CONFIG, AudioConfig.AUDIO_FORMAT, minBufferSize)

        audioRecord?.startRecording()
        isRecording = true

        val data = ByteArray(minBufferSize)

        val recordFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm")

        //是否创建成功
        if (!recordFile.mkdirs()) {
            log("Directory not created !")
        }

        //已存在，则先删除
        if (recordFile.exists()) {
            recordFile.delete()
        }

        log("start recording ...")
        //子线程
        Thread(Runnable {
            var fos: FileOutputStream? = null

            try {
                fos = FileOutputStream(recordFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (fos == null) {
                return@Runnable
            }

            while (isRecording) {
                val read = audioRecord?.read(data, 0, minBufferSize)
                if (read != AudioRecord.ERROR_INVALID_OPERATION) {
                    try {
                        fos.write(data)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            try {
                log("run: close file output stream !")
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()

    }

    private fun stopRecord() {
        log("stop record ...")
        isRecording = false

        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
}
