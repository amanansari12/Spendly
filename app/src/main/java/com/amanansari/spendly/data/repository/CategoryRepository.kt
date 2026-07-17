package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.CategoryDao
import com.amanansari.spendly.data.local.defaults.DefaultCategories
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    private val seedMutex = Mutex()
    private val _isSeeded = MutableStateFlow(false)
    val isSeeded: StateFlow<Boolean> = _isSeeded.asStateFlow()

    suspend fun ensureSeeded() {
        if (_isSeeded.value) return          // fast path after first call
        seedMutex.withLock {
            if (_isSeeded.value) return       // re-check inside the lock
            if (categoryDao.getCount() == 0) {
                categoryDao.insertAll(DefaultCategories.categories)
            }
            _isSeeded.value = true
        }
    }
}