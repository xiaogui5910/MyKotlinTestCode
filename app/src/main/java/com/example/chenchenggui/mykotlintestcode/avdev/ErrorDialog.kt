package com.example.chenchenggui.mykotlintestcode.avdev

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

/**
 * description ：
 * author : chenchenggui
 * creation date: 2019/5/28
 */
class ErrorDialog : DialogFragment() {

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