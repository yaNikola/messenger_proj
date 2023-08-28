package com.example.telegram.ui.screens.settings

import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.ui.screens.base.BaseChangeFragment
import kotlinx.android.synthetic.main.fragment_cnage_bio.*

/* Фрагмент для изменения информации о пользователе */

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    private lateinit var mEditText: EditText
    private var lineCounter = 1
    private var charactersLeft: Int = 120
    private val edTextIsNotNull = settings_input_bio != null

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)

        if (edTextIsNotNull) {
            charactersLeft = 120 - settings_input_bio.text.length
        } else settings_char_counter.text = charactersLeft.toString()

        followTheEdText()
    }

    private fun followTheEdText() {
        settings_input_bio.addTextChangedListener(AppTextWatcher {
            if (settings_input_bio.text.isNotEmpty()) {
                charactersLeft = 120 - settings_input_bio.text.length
                settings_char_counter.text = charactersLeft.toString()

                if (settings_input_bio.text.length == 38 * lineCounter - 1) {
                    settings_input_bio.text.append("\n")
                    lineCounter++
                } else if (settings_input_bio.text.length <= 38 * (lineCounter - 1) - 1){
                    lineCounter--
                }
            }
        })
    }

    override fun change() {
        super.change()
        val newBio = settings_input_bio.text.toString()
        setBioToDatabase(newBio)
    }

}

