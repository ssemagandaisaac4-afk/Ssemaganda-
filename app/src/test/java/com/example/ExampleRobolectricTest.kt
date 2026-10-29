package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ArcheryRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
    assertEquals("Archery AI Hub", appName)
  }

  @Test
  fun `archery repository initializes default user and plans`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = ArcheryRepository(context)
    
    assertEquals(5, repo.packagePlans.size)
    assertNotNull(repo.users.value["+256770000000"])
    val defaultUser = repo.users.value["+256770000000"]!!
    assertEquals(2000.0, defaultUser.balance, 0.001)
  }
}
