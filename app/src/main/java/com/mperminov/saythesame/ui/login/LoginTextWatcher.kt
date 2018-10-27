package com.mperminov.saythesame.ui.login

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher

class LoginTextWatcher(
  private val inputLayout: TextInputLayout,
  private val errorMessage: String,
  val predicate: (CharSequence?) -> Boolean
) : TextWatcher {

  override fun afterTextChanged(p0: Editable?) {
  }

  override fun beforeTextChanged(
    p0: CharSequence?,
    p1: Int,
    p2: Int,
    p3: Int
  ) {
  }

  override fun onTextChanged(
    cs: CharSequence?,
    p1: Int,
    p2: Int,
    p3: Int
  ) {
    if (predicate(cs)) inputLayout.error = errorMessage else
      inputLayout.error = null
  }
}