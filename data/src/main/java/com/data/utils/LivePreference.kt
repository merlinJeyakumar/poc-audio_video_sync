package com.data.utils

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class LivePreference<T> constructor(private val updates: Observable<String>,
                                    private val preferences: SharedPreferences,
                                    private val key: String,
                                    private val defaultValue: T?) : MutableLiveData<T>() {

    private var disposable: Disposable? = null

    override fun onActive() {
        super.onActive()
        value = (preferences.all[key] as T) ?: defaultValue

        disposable = updates.filter { t -> t == key }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object: DisposableObserver<String>() {
                override fun onComplete() {

                }

                override fun onNext(t: String) {
                    postValue((preferences.all[t] as T) ?: defaultValue)
                }

                override fun onError(e: Throwable) {

                }
            })
    }

    override fun onInactive() {
        super.onInactive()
        disposable?.dispose()
    }
}