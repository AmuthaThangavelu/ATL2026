package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GamePreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("WonderPlay", appName)
  }

  @Test
  fun `game preferences star tracking`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = GamePreferences.getInstance(context)
    val initialStars = prefs.totalStars.value
    prefs.addStars(10)
    assertEquals(initialStars + 10, prefs.totalStars.value)
  }
}

