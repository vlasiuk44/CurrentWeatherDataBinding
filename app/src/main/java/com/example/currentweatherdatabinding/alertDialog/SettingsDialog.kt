package com.example.currentweatherdatabinding.alertDialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class SettingsDialog(private val ctx: Context): DialogFragment() {
    private val viewModel: DialogViewModel by activityViewModels()
    private val choices = listOf(DisplayingMode.INFORMATIVE, DisplayingMode.SIMPLE)
    private val choices_string = (choices.map { it.mode }).toTypedArray()
    private var choice: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        choice = if (viewModel.displayingMode.value == DisplayingMode.INFORMATIVE) 0 else 1

        return ctx.let {  it ->
            AlertDialog.Builder(it)
                .setSingleChoiceItems(choices_string, choice) { _, which ->
                    Log.d("DIALOG CLICKED", (choices[which]).toString())
                    choice = which
                }
                .setPositiveButton("Выбрать") { _, _ ->
                    Log.d("FINAL VALUE", choices[choice].mode)
                    viewModel.displayingMode.value = choices[choice]
                }
                .setNegativeButton("Отмена", null)
                .setTitle("Выбор режима отображения:")
                .create()
        }
    }


}