package com.example.hotelapp

import com.example.hotelapp.domain.model.Hotel
import com.example.hotelapp.domain.repository.HotelRepository
import com.example.hotelapp.domain.usecase.GetHotelsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetHotelsUseCaseTest {

    private lateinit var repository: HotelRepository
    private lateinit var useCase: GetHotelsUseCase

    private val fakeHotels = listOf(
        Hotel(1, "Grand Hotel Paris", "Paris", "France", "Описание", 80.0, 4.5, null),
        Hotel(2, "Berlin Plaza", "Berlin", "Germany", "Описание", 105.0, 4.2, null),
        Hotel(3, "Roma Luxury", "Rome", "Italy", "Описание", 130.0, 4.8, null)
    )

    @Before
    fun setup() {
        repository = mock(HotelRepository::class.java)
        useCase = GetHotelsUseCase(repository)
    }

    @Test
    fun `получение всех отелей возвращает список`() = runTest {
        `when`(repository.getHotels(null, null)).thenReturn(fakeHotels)
        val result = useCase()
        assertEquals(3, result.size)
    }

    @Test
    fun `фильтрация по городу возвращает отели`() = runTest {
        val parisHotels = fakeHotels.filter { it.city == "Paris" }
        `when`(repository.getHotels("Paris", null)).thenReturn(parisHotels)
        val result = useCase(city = "Paris")
        assertEquals(1, result.size)
        assertEquals("Paris", result[0].city)
    }

    @Test
    fun `пустой список возвращается когда отелей нет`() = runTest {
        `when`(repository.getHotels(null, null)).thenReturn(emptyList())
        val result = useCase()
        assertEquals(0, result.size)
    }

    @Test
    fun `фильтрация по цене возвращает правильные отели`() = runTest {
        val cheapHotels = fakeHotels.filter { it.pricePerNight <= 100.0 }
        `when`(repository.getHotels(null, 100.0)).thenReturn(cheapHotels)
        val result = useCase(maxPrice = 100.0)
        assertEquals(1, result.size)
        assertEquals("Grand Hotel Paris", result[0].name)
    }
}