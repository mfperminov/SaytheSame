package com.mperminov.saythesame.data.utils;

public class Answer {
    private String curAnswer;
    private ChangeListener listener;

    public interface ChangeListener {
        void onChange();
    }

    public String getCurAnswer() {
        return curAnswer;
    }

    public void setCurAnswer(String curAnswer) {
        this.curAnswer = curAnswer;
        if(listener!=null) listener.onChange();
    }


    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }
}
