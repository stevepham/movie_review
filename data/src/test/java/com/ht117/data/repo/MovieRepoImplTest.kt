package com.ht117.data.repo

import com.ht117.data.source.IMovieSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRepoImplTest {

    @MockK
    lateinit var pagingSource: MoviePagingSource
    @MockK
    lateinit var remote: IMovieSource
    lateinit var repo: IMovieRepo

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = MovieRepoImpl(remote, pagingSource)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun getNowPlaying() = runBlocking {
        coEvery { remote.requestNowPlaying() }.returns(emptyFlow())

        repo.getNowPlaying()

        coVerify { remote.requestNowPlaying() }
    }

    @Test
    fun getDetail() = runBlocking {
        coEvery { remote.requestDetail(123) }.returns(emptyFlow())

        repo.getDetail(123)

        coVerify { remote.requestDetail(123) }
    }
}