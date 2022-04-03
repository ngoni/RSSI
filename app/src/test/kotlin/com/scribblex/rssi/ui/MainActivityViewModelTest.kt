package com.scribblex.rssi.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.scribblex.rssi.data.entities.Rssi
import com.scribblex.rssi.data.repository.RssiRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock

@DelicateCoroutinesApi
class MainActivityViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val viewStateObserver = mock<Observer<List<Rssi>>>()
    private var repository = mock<RssiRepository>()
    private lateinit var viewModel: MainActivityViewModel
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = MainActivityViewModel(repository)
        viewModel.getViewState().observeForever(viewStateObserver)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun testViewModelSetsDataToView(): Unit = runBlocking {
        launch(Dispatchers.Main) {  // Will be launched in the mainThreadSurrogate dispatcher
            //setup
            val data = mock<List<Rssi>>()
            //act
            viewModel.setViewState(data)
            //validate
            verify(viewStateObserver).onChanged(data)
        }
    }
}