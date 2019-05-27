package com.example.chenchenggui.mykotlintestcode.avdev

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import com.example.chenchenggui.mykotlintestcode.R
import kotlinx.android.synthetic.main.frament_camera.*

/**
 * description ：
 * author : chenchenggui
 * creation date: 2019/5/24
 */
class CameraFragment : Fragment(), SurfaceHolder.Callback {
    var cameraMgr: CameraManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frament_camera, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        surfaceView.holder.addCallback(this)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            cameraMgr = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }
}