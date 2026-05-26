package com.example.hotelapp

import com.example.hotelapp.domain.model.Hotel
import com.example.hotelapp.domain.usecase.GetHotelsUseCase
import com.example.hotelapp.presentation.hotels.HotelsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class HotelsViewModelTest {

    private lateinit var getHotelsUseCase: GetHotelsUseCase
    private lateinit var viewModel: HotelsViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val fakeHotels = listOf(
        Hotel(1, "Grand Hotel Paris", "Paris", "France", "Описание", 80.0, 4.5, null),
        Hotel(2, "Berlin Plaza", "Berlin", "Germany", "Описание", 105.0, 4.2, null)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getHotelsUseCase = mock(GetHotelsUseCase::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `загрузка отелей возвращает список`() = runTest {
        `when`(getHotelsUseCase(null, null)).thenReturn(fakeHotels)
        viewModel = HotelsViewModel(getHotelsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, viewModel.state.value.hotels.size)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `ошибка загрузки отелей сохраняется в состоянии`() = runTest {
        `when`(getHotelsUseCase(null, null)).thenThrow(RuntimeException("Ошибка сети"))
        viewModel = HotelsViewModel(getHotelsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Ошибка сети", viewModel.state.value.error)
    }

    @Test
    fun `фильтрация по городу передаётся в useCase`() = runTest {
        val parisHotels = fakeHotels.filter { it.city == "Paris" }
        `when`(getHotelsUseCase("Paris", null)).thenReturn(parisHotels)
        viewModel = HotelsViewModel(getHotelsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadHotels(city = "Paris")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, viewModel.state.value.hotels.size)
    }
}