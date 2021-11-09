package no.javazone.scheduler.api

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream

class AssetConferenceSessionTest {
    private lateinit var api: ConferenceSessionApi

    @Before
    fun setUp() {
        api = AssetConferenceSession.getInstance(FileInputStream("src/test/res/sessions.json"))
    }

    @Test
    fun test() {
        val result = api.fetch()
        assertThat(result.sessions).isNotEmpty()
    }
}