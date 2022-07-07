package com.geekbrains.tests

import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.view.details.ViewDetailsContract
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter
    private var count = 0

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailsPresenter(viewContract, 0)
    }

    @Test
    fun onAttach_Test() {
        presenter.onAttach(viewContract)
        assertSame(viewContract, presenter.view)
    }

    @Test
    fun setCounter_Test() {
        val countNew = 2
        presenter.setCounter(countNew)
        assertEquals(countNew, presenter.count)
    }

    @Test
    fun onIncrement_Test() {
        presenter.onIncrement()
        verify(viewContract).setCount(count + 1)
    }

    @Test
    fun onDecrement_Test() {
        presenter.onDecrement()
        verify(viewContract).setCount(count - 1)
    }

    @Test
    fun onDetach_Test() {
        presenter.onDetach(viewContract)
        assertNull(presenter.view)
    }
}