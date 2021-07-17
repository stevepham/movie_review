package com.ht117.data.source

import com.ht117.data.model.EMPTY
import com.ht117.data.model.Movie
import com.ht117.data.model.State
import com.ht117.data.model.UNKNOWN
import io.ktor.http.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRemoteTest {

    @Test
    fun requestNowPlaying_success() = runBlocking {
        val api = provideMockClient(mapOf(NOW_PLAYING_REQ to RESPONSE_SUCCESS_NOW_PLAYING))
        val remote = MovieRemote(api)
        val flow = remote.requestNowPlaying()
        val result = mutableListOf<State<List<Movie>>>()
        flow.take(2).toCollection(result)
        assert(result[0] == State.Loading)
        assert(result[1] is State.Success)
    }

    @Test
    fun requestNowPlaying_empty() = runBlocking {
        val api = provideMockClient(mapOf(NOW_PLAYING_REQ to RESPONSE_EMPTY_NOW_PLAYING))
        val remote = MovieRemote(api)
        val flow = remote.requestNowPlaying()
        val result = mutableListOf<State<List<Movie>>>()
        flow.take(2).toCollection(result)
        assert(result[0] == State.Loading)
        assert(result[1] == State.Failed(EMPTY))
    }

    @Test
    fun requestNowPlaying_unknown() = runBlocking {
        val api = provideMockClient(mapOf(NOW_PLAYING_REQ to ""))
        val remote = MovieRemote(api)
        val flow = remote.requestNowPlaying()
        val result = mutableListOf<State<List<Movie>>>()
        flow.take(2).toCollection(result)
        assert(result[0] == State.Loading)
        assert(result[1] == State.Failed(UNKNOWN))
    }

    @Test
    fun requestPopular_success() = runBlocking {
        val api = provideMockClient(mapOf(POPULAR_REQ to RESPONSE_SUCCESS_POPULAR, DETAIL_REQ to RESPONSE_SUCCESS_DETAIL))
        val remote = MovieRemote(api)
        val state = remote.requestPopular(1)
        assert(state is State.Success)
    }

    @Test
    fun requestPopular_empty() = runBlocking {
        val api = provideMockClient(mapOf(POPULAR_REQ to RESPONSE_EMPTY_POPULAR))
        val remote = MovieRemote(api)
        val state = remote.requestPopular(1)
        assert(state == State.Failed(EMPTY))
    }

    @Test
    fun requestPopular_unknown() = runBlocking {
        val api = provideMockClient(mapOf(POPULAR_REQ to ""))
        val remote = MovieRemote(api)
        val state = remote.requestPopular(1)
        assert(state == State.Failed(UNKNOWN))
    }

    @Test
    fun reqestDetail_success() = runBlocking {
        val api = provideMockClient(mapOf(DETAIL_REQ to RESPONSE_SUCCESS_DETAIL))
        val remote = MovieRemote(api)
        val flow = remote.requestDetail(movieId)
        val result = mutableListOf<State<Movie>>()
        flow.take(2).toCollection(result)
        assert(result[0] == State.Loading)
        assert(result[1] is State.Success)
    }

    @Test
    fun requestDetail_unknown() = runBlocking {
        val api = provideMockClient(mapOf(DETAIL_REQ to RESPONSE_UNKNOWN_DETAIL))
        val remote = MovieRemote(api)
        val flow = remote.requestDetail(movieId)
        val result = mutableListOf<State<Movie>>()
        flow.take(2).toCollection(result)
        assert(result[0] == State.Loading)
        assert(result[1] == State.Failed(UNKNOWN))
    }

    companion object {
        private const val movieId = 578701L
        private const val NOW_PLAYING_REQ = "${RemoteConfig.BaseHost}/movie/now_playing?language=en-US"
        private const val POPULAR_REQ = "${RemoteConfig.BaseHost}/movie/popular?language=en-US&page=1"
        private const val DETAIL_REQ = "${RemoteConfig.BaseHost}/movie/${movieId}?language=en-US"

        private val RESPONSE_SUCCESS_NOW_PLAYING = """
            {
              "dates": {
                "maximum": "2021-06-24",
                "minimum": "2021-05-07"
              },
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/8ChCpCYxh9YXusmHwcE9YzP0TSG.jpg",
                  "genre_ids": [
                    35,
                    80
                  ],
                  "id": 337404,
                  "original_language": "en",
                  "original_title": "Cruella",
                  "overview": "In 1970s London amidst the punk rock revolution, a young grifter named Estella is determined to make a name for herself with her designs. She befriends a pair of young thieves who appreciate her appetite for mischief, and together they are able to build a life for themselves on the London streets. One day, Estella’s flair for fashion catches the eye of the Baroness von Hellman, a fashion legend who is devastatingly chic and terrifyingly haute. But their relationship sets in motion a course of events and revelations that will cause Estella to embrace her wicked side and become the raucous, fashionable and revenge-bent Cruella.",
                  "popularity": 3842.377,
                  "poster_path": "/rTh4K5uw9HypmpGslcKd4QfHl93.jpg",
                  "release_date": "2021-05-26",
                  "title": "Cruella",
                  "video": false,
                  "vote_average": 8.6,
                  "vote_count": 2892
                }
              ],
              "total_pages": 50,
              "total_results": 986
            }
        """.trimIndent()

        private val RESPONSE_EMPTY_NOW_PLAYING = """
            {
              "dates": {
                "maximum": "2021-06-24",
                "minimum": "2021-05-07"
              },
              "page": 1,
              "results": [],
              "total_pages": 0,
              "total_results": 0
            }
        """.trimIndent()

        private val RESPONSE_SUCCESS_POPULAR = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/ouOojiypBE6CD1aqcHPVq7cJf2R.jpg",
                  "genre_ids": [
                    53,
                    18,
                    28,
                    9648
                  ],
                  "id": 578701,
                  "original_language": "en",
                  "original_title": "Those Who Wish Me Dead",
                  "overview": "A young boy finds himself pursued by two assassins in the Montana wilderness with a survival expert determined to protecting him - and a forest fire threatening to consume them all.  Download : https://hd.movietv.biz/movie/578701/those-who-wish-me-dead.html",
                  "popularity": 1130.103,
                  "poster_path": "/xCEg6KowNISWvMh8GvPSxtdf9TO.jpg",
                  "release_date": "2021-05-05",
                  "title": "Those Who Wish Me Dead",
                  "video": false,
                  "vote_average": 7,
                  "vote_count": 508
                }
              ],
              "total_pages": 500,
              "total_results": 10000
            }
        """.trimIndent()

        private val RESPONSE_EMPTY_POPULAR = """
            {
              "page": 1,
              "results": [],
              "total_pages": 500,
              "total_results": 10000
            }
        """.trimIndent()

        private val RESPONSE_SUCCESS_DETAIL = """
            {
              "adult": false,
              "backdrop_path": "/wjQXZTlFM3PVEUmKf1sUajjygqT.jpg",
              "belongs_to_collection": null,
              "budget": 0,
              "genres": [
                {
                  "id": 878,
                  "name": "Science Fiction"
                },
                {
                  "id": 28,
                  "name": "Action"
                },
                {
                  "id": 53,
                  "name": "Thriller"
                }
              ],
              "homepage": "https://www.paramountplus.com/movies/infinite/gkYk2Ju73QiIYX8TrooFblbsaUfPugRz/",
              "id": 581726,
              "imdb_id": "tt6654210",
              "original_language": "en",
              "original_title": "Infinite",
              "overview": "Evan McCauley has skills he never learned and memories of places he has never visited. Self-medicated and on the brink of a mental breakdown, a secret group that call themselves “Infinites” come to his rescue, revealing that his memories are real.",
              "popularity": 1676.732,
              "poster_path": "/niw2AKHz6XmwiRMLWaoyAOAti0G.jpg",
              "production_companies": [
                {
                  "id": 435,
                  "logo_path": "/AjzK0s2w1GtLfR4hqCjVSYi0Sr8.png",
                  "name": "Di Bonaventura Pictures",
                  "origin_country": "US"
                },
                {
                  "id": 4,
                  "logo_path": "/fycMZt242LVjagMByZOLUGbCvv3.png",
                  "name": "Paramount",
                  "origin_country": "US"
                },
                {
                  "id": 8537,
                  "logo_path": null,
                  "name": "Closest to the Hole Productions",
                  "origin_country": "US"
                },
                {
                  "id": 114732,
                  "logo_path": "/tNCbisMxO5mX2X2bOQxHHQZVYnT.png",
                  "name": "New Republic Pictures",
                  "origin_country": "US"
                },
                {
                  "id": 8151,
                  "logo_path": null,
                  "name": "Fuqua Films",
                  "origin_country": "US"
                }
              ],
              "production_countries": [
                {
                  "iso_3166_1": "US",
                  "name": "United States of America"
                }
              ],
              "release_date": "2021-09-08",
              "revenue": 0,
              "runtime": 106,
              "spoken_languages": [
                {
                  "english_name": "English",
                  "iso_639_1": "en",
                  "name": "English"
                }
              ],
              "status": "Released",
              "tagline": "Grief. Hallucinations. Pride. Visions & Reminiscence",
              "title": "Infinite",
              "video": false,
              "vote_average": 0,
              "vote_count": 0
            }
        """.trimIndent()

        private val RESPONSE_UNKNOWN_DETAIL = """
            {}
        """.trimIndent()
    }
}