package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.example.audio.SoundEngine
import com.example.ui.games.HomeScreen
import com.example.ui.theme.WonderPlayTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val soundEngine = SoundEngine.getInstance(ApplicationProvider.getApplicationContext())
    composeTestRule.setContent {
      WonderPlayTheme {
        HomeScreen(
          soundEngine = soundEngine,
          stars = 25,
          onSelectGame = {},
          isSoundOn = true,
          isMusicOn = true,
          onToggleSound = {},
          onToggleMusic = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}

