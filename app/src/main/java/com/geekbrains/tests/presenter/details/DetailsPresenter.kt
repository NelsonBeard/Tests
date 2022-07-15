package com.geekbrains.tests.presenter.details

import com.geekbrains.tests.view.ViewContract
import com.geekbrains.tests.view.details.ViewDetailsContract

internal class DetailsPresenter internal constructor(
    private val viewContract: ViewDetailsContract,
    var count: Int = 0
) : PresenterDetailsContract {

    var view: ViewContract? = null

    override fun onAttach(view: ViewContract) {
        this.view = view
    }

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        viewContract.setCount(count)
    }

    override fun onDecrement() {
        count--
        viewContract.setCount(count)
    }

    override fun onDetach(view: ViewContract) {
        this.view = null
    }
}
