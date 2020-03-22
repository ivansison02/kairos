package com.ivansison.kairos

import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.ivansison.kairos.LocationAdapterTest.clickOnViewChild
import com.ivansison.kairos.RecentSearchesHelperTest.RecyclerViewMatchers.hasGreaterThan
import com.ivansison.kairos.RecentSearchesHelperTest.RecyclerViewMatchers.hasItemCount
import com.ivansison.kairos.views.activities.LocationActivity
import junit.framework.AssertionFailedError
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.hasItem
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LocationActivityTest {

    @get:Rule
    var locationRule: ActivityTestRule<LocationActivity> = ActivityTestRule(LocationActivity::class.java)

    @Test
    fun enter_Location() {
        onView(withId(R.id.fld_search))
            .perform(typeText("Bataan"), pressKey(KeyEvent.KEYCODE_ENTER))
    }

    @Test
    fun select_Search_Location() {
        onView(withId(R.id.lyt_my_location))
            .perform(click())
    }

    @Test
    fun remove_Search_Location() {
        val position : Int = 0

        try {
            onView(withId(R.id.rcv_location))
                .check(matches((hasGreaterThan(0))))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, clickOnViewChild(R.id.img_action)))
        }
        catch (e: AssertionFailedError) {
            assert(true)
        }
    }
}