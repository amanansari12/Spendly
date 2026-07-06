package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.CategoryDao
import com.amanansari.spendly.data.local.defaults.DefaultCategories

class CategoryRepository(
    private val categoryDao: CategoryDao
) {
    suspend fun seedCategories() {
        if (categoryDao.getCount() == 0) {
            categoryDao.insertAll(DefaultCategories.categories)
        }
    }
}