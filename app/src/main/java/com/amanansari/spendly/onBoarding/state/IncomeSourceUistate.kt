package com.amanansari.spendly.onBoarding.state

import com.amanansari.spendly.model.ExpIncCategory

data class IncomeSourceUistate (
    val availableIncomeSource : List<ExpIncCategory.IncomeCategory>,
    val selectedIncomeSourceId : String = ""
)