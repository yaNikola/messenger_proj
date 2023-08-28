package com.example.telegram.ui.screens.settings

import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.ui.screens.base.BaseChangeFragment
import com.example.telegram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

/* Фрагмент для изменения username пользователя */

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    private lateinit var mNewUsername: String
    private lateinit var mTextView: TextView
    private lateinit var mEditText: EditText

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
        initFragmentComponents()
        changeTextView()
    }

    private fun initFragmentComponents() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        mTextView = (activity as MainActivity).findViewById(R.id.settings_username_exist)
        mEditText = (activity as MainActivity).findViewById(R.id.settings_input_username)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    private fun changeTextView() {
        mEditText.addTextChangedListener(AppTextWatcher {
            mTextView.setTextColor(Color.parseColor("#939393"))
            mTextView.text = "Проверка псевдонима..."

            REF_DATABASE_ROOT.child(
                NODE_USERNAMES
            )
                .addValueEventListener(AppValueEventListener {
                    if (mEditText.text.toString().length < 5) {
                        mTextView.setTextColor(Color.parseColor("#E31A5F"))
                        mTextView.text = "Псевдоним должен содержать хотя бы 5 символов."
                    } else if (it.hasChild(mEditText.text.toString()) && mEditText.text.toString() != USER.username) {
                        mTextView.setTextColor(Color.parseColor("#E31A5F"))
                        mTextView.text = "Этот псевдоним уже занят."
                    } else {
                        mTextView.setTextColor(Color.parseColor("#4FCF55"))
                        mTextView.text = mEditText.text.toString() + " доступный."
                    }
                })
        })
    }


    override fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(
                NODE_USERNAMES
            ).addListenerForSingleValueEvent(AppValueEventListener {
                if (!it.hasChild(mNewUsername)) {
                    changeUsername()
                } else if (mNewUsername == USER.username) {
                    fragmentManager?.popBackStack()
                }
            })
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(
            CURRENT_UID
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mNewUsername)
                }
            }
    }
}

