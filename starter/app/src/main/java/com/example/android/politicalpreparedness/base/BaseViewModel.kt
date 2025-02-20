package com.example.android.politicalpreparedness.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.utils.SingleLiveEvent

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBar: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarInt: SingleLiveEvent<Int> = SingleLiveEvent()
    val showToast: SingleLiveEvent<String> = SingleLiveEvent()
    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()

}