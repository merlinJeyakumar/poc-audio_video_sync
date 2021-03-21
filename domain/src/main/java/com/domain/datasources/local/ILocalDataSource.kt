package com.domain.datasources.local

import androidx.lifecycle.LiveData
import com.domain.entity.QuickTextEntity

interface ILocalDataSource {
    fun getLiveQuickText(): LiveData<List<QuickTextEntity>>
}