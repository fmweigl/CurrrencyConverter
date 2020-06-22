package com.example.ratesapp.data.repository

import com.example.ratesapp.data.datasource.IRatesDataSource
import com.example.ratesapp.data.response.RateResponse
import com.example.ratesapp.data.response.RatesResponse
import com.example.ratesapp.domain.model.Rate
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.math.BigDecimal

class RatesRepository(private val dataSource: IRatesDataSource) {

    fun getRates(baseCurrencyId: String): Single<List<Rate>> = dataSource.getRates(baseCurrencyId)
        .map { it.toRates() }
        .subscribeOn(Schedulers.io())

    private fun RatesResponse.toRates(): List<Rate> {
        val baseCurrencyRate = Rate(
            currencyId = baseCurrency,
            value = BigDecimal.valueOf(1) // base currency always has a rate of 1
        )
        return listOf(baseCurrencyRate).plus(currencyRates.map { it.toRate() })
    }

    private fun RateResponse.toRate() = Rate(currencyId, value)

}