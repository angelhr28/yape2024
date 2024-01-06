package com.angelhr28.data.datasource.remote.api

import com.angelhr28.domain.util.BadRequestException
import com.angelhr28.domain.util.GenericException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class UtilKtTest {

    @RelaxedMockK
    private lateinit var response: Response<Any>

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `when response is null should throw GenericException`() {
        try {
            val nullResponse: Response<Any>? = null
            nullResponse.getBody()
        } catch (throwable: Throwable) {
            assertTrue(throwable is GenericException)
        }
    }

    @Test
    fun `when response is isSuccessful should return body`() {
        val body = 5
        coEvery { response.isSuccessful } returns true
        coEvery { response.body() } returns body

        val result = response.getBody()

        assertEquals(result, body)
        coVerify(exactly = 1) { response.isSuccessful }
        coVerify(exactly = 1) { response.body() }
        coVerify(exactly = 0) { response.code() }
        coVerify(exactly = 0) { response.errorBody() }
    }

    @Test
    fun `when response is isSuccessful but body is null should throw GenericException`() {
        coEvery { response.isSuccessful } returns true
        coEvery { response.body() } returns null

        try {
            response.getBody()
        } catch (throwable: Throwable) {
            assertTrue(throwable is GenericException)
            coVerify(exactly = 1) { response.isSuccessful }
            coVerify(exactly = 1) { response.body() }
            coVerify(exactly = 0) { response.code() }
            coVerify(exactly = 0) { response.errorBody() }
        }
    }

    @Test
    fun `when response has a 400 code should throw BadRequestException`() {
        coEvery { response.isSuccessful } returns false
        coEvery { response.code() } returns 400

        try {
            response.getBody()
        } catch (throwable: Throwable) {
            assertTrue(throwable is BadRequestException)
            coVerify(exactly = 1) { response.isSuccessful }
            coVerify(exactly = 0) { response.body() }
            coVerify(exactly = 1) { response.code() }
            coVerify(exactly = 1) { response.errorBody() }
        }
    }

    @Test
    fun `when response has a code different than 400 code should throw GenericException`() {
        coEvery { response.isSuccessful } returns false
        coEvery { response.code() } returns 500

        try {
            response.getBody()
        } catch (throwable: Throwable) {
            assertTrue(throwable is GenericException)
            coVerify(exactly = 1) { response.isSuccessful }
            coVerify(exactly = 0) { response.body() }
            coVerify(exactly = 1) { response.code() }
            coVerify(exactly = 0) { response.errorBody() }
        }
    }
}
