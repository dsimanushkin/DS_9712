package com.ds9712.ds_9712.ui.intro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.ActivityIntroBinding
import com.ds9712.ds_9712.ui.BaseActivity
import com.ds9712.ds_9712.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class IntroActivity: BaseActivity() {

    @Inject
    lateinit var sharedPrefsEditor: SharedPreferences.Editor

    private lateinit var binding: ActivityIntroBinding

    override fun inject() {
        (application as BaseApplication).introComponent().inject(this)
    }

    private lateinit var dots: Array<TextView>

    val introMainContent = intArrayOf(
        R.layout.view_intro_slide_1,
        R.layout.view_intro_slide_2,
        R.layout.view_intro_slide_3,
        R.layout.view_intro_slide_4,
        R.layout.view_intro_slide_5,
        R.layout.view_intro_slide_6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListeners()
        initViewPager()
        addBottomDots(0)
    }

    private fun setOnClickListeners() {
        binding.introPageCloseButton.setOnClickListener {
            updateShowIntro()
            navMainActivity()
        }
        binding.introPageNextButton.setOnClickListener {
            val current = getItem()
            if (current < introMainContent.size) {
                // move to next screen
                binding.intoPageViewpager.currentItem = current
            } else {
                updateShowIntro()
                navMainActivity()
            }
        }
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        (application as BaseApplication).releaseIntroComponent()
    }

    private fun updateShowIntro() {
        sharedPrefsEditor.putBoolean("showIntro", false)
        sharedPrefsEditor.apply()
    }

    private fun addBottomDots(currentPage: Int) {
        dots = Array(introMainContent.size){ TextView(this) }
        val colorsActive = getColor(R.color.intro_dot_active)
        val colorsInactive = getColor(R.color.intro_dot_inactive)
        binding.introPageDotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY)
            dots[i].textSize = 30F
            dots[i].setTextColor(colorsInactive)
            if (i != dots.size - 1) {
                dots[i].setPadding(0, 0, 30, 0)
            }
            binding.introPageDotsLayout.addView(dots[i])
        }
        if (dots.isNotEmpty()) dots[currentPage].setTextColor(colorsActive)
    }

    private fun getItem(): Int {
        return binding.intoPageViewpager.currentItem + 1
    }

    private fun initViewPager() {
        binding.intoPageViewpager.adapter = ViewPagerAdapter(this, introMainContent)
        binding.intoPageViewpager.addOnPageChangeListener(viewPagerPageChangeListener)
    }

    override fun isStoragePermissionGranted(): Boolean {
        return false
    }

    override fun displaySnackBarMessage(message: String) {}
    override fun showErrorMessage(message: String) {}
    override fun showProgressBar(isVisible: Boolean) {}

    //  viewpager change listener
    private val viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object :
        ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == introMainContent.size - 1) {
                // last page. make button text to GOT IT
                binding.introPageNextButtonText.text = getString(R.string.intro_page_finish_text_button)
                binding.introPageCloseButton.visibility = View.INVISIBLE
            } else {
                // still pages are left
                binding.introPageNextButtonText.text = getString(R.string.intro_page_next_text_button)
                binding.introPageCloseButton.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * View pager adapter
     */
    class ViewPagerAdapter(private val context: Context, private val itemList: IntArray) : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater!!.inflate(itemList[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return itemList.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view: View = `object` as View
            container.removeView(view)
        }
    }
}