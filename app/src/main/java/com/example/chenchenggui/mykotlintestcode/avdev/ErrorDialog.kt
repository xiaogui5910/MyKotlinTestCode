package com.example.chenchenggui.mykotlintestcode.avdev

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog

/**
 * description ：
 * author : chenchenggui
 * creation date: 2019/5/28
 */
class ErrorDialog : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity!!)
                    .setMessage(arguments!!.getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok) { _, _ -> activity!!.finish() }
                    .create()

    companion object {

        @JvmStatic private val ARG_MESSAGE = "message"

        @JvmStatic fun newInstance(message: String): ErrorDialog = ErrorDialog().apply {
            arguments = Bundle().apply { putString(ARG_MESSAGE, message) }
        }
    }

}