package com.example.myapplication.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("1710218     姜奕兵\n1711332     蒋朝良\n1711335 卡哈尔·卡得尔");
    }

    public LiveData<String> getText() {
        return mText;
    }
}