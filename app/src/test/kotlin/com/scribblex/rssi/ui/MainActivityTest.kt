package com.scribblex.rssi.ui

import androidx.appcompat.widget.AppCompatTextView
import com.google.common.truth.Truth.assertThat
import com.scribblex.rssi.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(manifest = Config.NONE)
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun checkGreetingMessageExistsInView() {
        val activity: MainActivity = Robolectric.setupActivity(MainActivity::class.java)
        val textView = activity.requireViewById<AppCompatTextView>(R.id.greeting_message)
        assertThat(textView.id).isEqualTo(R.id.greeting_message)
    }
}