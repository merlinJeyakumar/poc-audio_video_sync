package com.data.utils

import android.content.SharedPreferences
import com.support.shared_pref.BaseLivePreference
import com.support.shared_pref.BaseMultiPreference
import io.reactivex.rxjava3.subjects.PublishSubject

class LiveSharedPreferences constructor(private val preferences: SharedPreferences) {

    private val publisher = PublishSubject.create<String>()
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> publisher.onNext(key) }

    private val updates = publisher.doOnSubscribe {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }.doOnDispose {
        if (!publisher.hasObservers())
            preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun getPreferences(): SharedPreferences {
        return preferences
    }

    fun getString(key: String, defaultValue: String?): BaseLivePreference<String> {
        return BaseLivePreference(updates, preferences, key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): BaseLivePreference<Int> {
        return BaseLivePreference(updates, preferences, key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): BaseLivePreference<Boolean> {
        return BaseLivePreference(updates, preferences, key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): BaseLivePreference<Float> {
        return BaseLivePreference(updates, preferences, key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): BaseLivePreference<Long> {
        return BaseLivePreference(updates, preferences, key, defaultValue)
    }

    fun getStringSet(key: String, defaultValue: Set<String>): BaseLivePreference<Set<String>> {
        return BaseLivePreference(updates, preferences, key, defaultValue)
    }

    fun <T> listenMultiple(keys: List<String>, defaultValue: T): BaseMultiPreference<T> {
        return BaseMultiPreference(updates, preferences, keys, defaultValue)
    }
}