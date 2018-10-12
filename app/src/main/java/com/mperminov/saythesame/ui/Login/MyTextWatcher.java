package com.mperminov.saythesame.ui.Login;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

class MyTextWatcher implements TextWatcher {
  private TextInputLayout inputLayout;
  private Boolean predicate;
  private class Predicate {
    private CharSequence cs;

  }

  public MyTextWatcher(TextInputLayout inputLayout, Boolean predicate, String errorMeassge) {
    this.inputLayout = inputLayout;
    this.predicate = predicate;
    this.errorMeassge = errorMeassge;
  }

  private String errorMeassge;

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (!predicate) {
      inputLayout.setError(errorMeassge);
    } else {
      inputLayout.setError(null);
    }
  }

  @Override public void afterTextChanged(Editable editable) {

  }
}
