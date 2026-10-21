package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.language.SrishtiStrings
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Srishti Institute", appName)
    }

    @Test
    fun `verify exact institute name constant`() {
        assertEquals("Srishti Computer Institute", SrishtiStrings.INSTITUTE_NAME)
    }
}
