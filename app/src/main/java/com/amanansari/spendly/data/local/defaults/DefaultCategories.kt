package com.amanansari.spendly.data.local.defaults

import com.amanansari.spendly.data.local.entity.CategoryEntity
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.model.allIncomeCategories

object DefaultCategories {

    val categories : List<CategoryEntity> =

        allExpenseCategories.mapIndexed { index, category ->
            CategoryEntity(
                categoryId = category.id,
                name = category.title,
                type = "EXPENSE",
                sortOrder = index
            )
        }+ allIncomeCategories.mapIndexed { index, category ->
                    CategoryEntity(
                        categoryId = category.id,
                        name = category.title,
                        type = "INCOME",
                        sortOrder = index
                    )
                }




}