package com.example.printermobile.ui.Views

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager.widget.ViewPager
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityMainBinding
import com.example.printermobile.ui.Views.onboarding.OnBoardingPageAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var onBoardingPageAdapter: OnBoardingPageAdapter
    private var dots: List<TextView> = listOf()
    private var currentItem = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        screenSplash.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.view.height.toFloat()
            )
            val isFirstTime =
                getSharedPreferences("onBoarding", MODE_PRIVATE).getString("tour", "a")
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L
            slideUp.doOnEnd {
                if (isFirstTime == "END") {
                    val listPrinterIntent = Intent(this, ListPrintersActivity::class.java)
                    startActivity(listPrinterIntent)
                } else {
                    splashScreenView.remove()
                }
            }
            if (isFirstTime == "END") {
                val listPrinterIntent = Intent(this, ListPrintersActivity::class.java)
                startActivity(listPrinterIntent)
            } else {
                slideUp.start()
            }
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initUI()
        initListeners()
        screenSplash.setKeepOnScreenCondition { false }
    }

    private fun initUI() {
        onBoardingPageAdapter = OnBoardingPageAdapter(applicationContext)

        setDots(0)
        binding.vpSliderLayout.adapter = onBoardingPageAdapter
    }

    private fun setDots(position:Int){
        dots = listOf()
        binding.clDotContainer.removeAllViews()
        for (i in 1..onBoardingPageAdapter.count) {
            val dot = TextView(this)
            dot.text = Html.fromHtml("&#8226;")
            dot.textSize = 35F
            dots = dots.plus(dot)
            binding.clDotContainer.addView(dot)
        }
        dots[position].setTextColor(ContextCompat.getColor(this,R.color.primary))
    }

    private fun initListeners() {
        binding.vpSliderLayout.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                setDots(position)
                currentItem = position
                when (position) {
                    0 -> {
                        val animation =
                            AnimationUtils.loadAnimation(applicationContext, R.anim.button_fide_out)
                        val fadeIn =
                            AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
                        binding.btnFinishOnboarding.animation = animation
                        binding.btnOnBoardingNext.animation = fadeIn
                        binding.btnFinishOnboarding.visibility = View.GONE
                        binding.btnOnBoardingNext.visibility = View.VISIBLE
                    }

                    1 -> {
                        val animation =
                            AnimationUtils.loadAnimation(applicationContext, R.anim.button_fide_in)
                        val fadeOut =
                            AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
                        binding.btnOnBoardingNext.animation = fadeOut
                        binding.btnFinishOnboarding.animation = animation
                        binding.btnFinishOnboarding.visibility = View.VISIBLE
                        binding.btnOnBoardingNext.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        binding.btnFinishOnboarding.setOnClickListener {
            getSharedPreferences("onBoarding", MODE_PRIVATE).edit().putString("tour", "END").apply()
            val printerListIntent = Intent(this, ListPrintersActivity::class.java)
            startActivity(printerListIntent)
        }
        binding.btnOnBoardingNext.setOnClickListener {
            Thread.sleep(200)
            currentItem += 1
            binding.vpSliderLayout.currentItem = currentItem
        }
        binding.btnSkipOnBoarding.setOnClickListener {
            getSharedPreferences("onBoarding", MODE_PRIVATE).edit().putString("tour", "END").apply()
            val printerListIntent = Intent(this, ListPrintersActivity::class.java)
            startActivity(printerListIntent)
        }
    }
}