package com.example.hotelapp

import com.example.hotelapp.domain.model.Booking
import com.example.hotelapp.domain.usecase.GetMyBookingsUseCase
import com.example.hotelapp.presentation.bookings.BookingsViewModel
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
class BookingsViewModelTest {

    private lateinit var getMyBookingsUseCase: GetMyBookingsUseCase
    private lateinit var viewModel: BookingsViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val fakeBookings = listOf(
        Booking(1, 1, 1, "Grand Hotel", "Paris", "2026-06-01T12:00:00", "2026-06-05T12:00:00", 320.0),
        Booking(2, 1, 2, "Berlin Plaza", "Berlin", "2026-07-01T12:00:00", "2026-07-03T12:00:00", 210.0)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getMyBookingsUseCase = mock(GetMyBookingsUseCase::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `загрузка бронирований возвращает список`() = runTest {
        `when`(getMyBookingsUseCase()).thenReturn(fakeBookings)
        viewModel = BookingsViewModel(getMyBookingsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, viewModel.state.value.bookings.size)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `пустой список бронирований обрабатывается корректно`() = runTest {
        `when`(getMyBookingsUseCase()).thenReturn(emptyList())
        viewModel = BookingsViewModel(getMyBookingsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(0, viewModel.state.value.bookings.size)
    }

    @Test
    fun `ошибка загрузки бронирований сохраняется в состоянии`() = runTest {
        `when`(getMyBookingsUseCase()).thenThrow(RuntimeException("Нет соединения"))
        viewModel = BookingsViewModel(getMyBookingsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Нет соединения", viewModel.state.value.error)
    }
}