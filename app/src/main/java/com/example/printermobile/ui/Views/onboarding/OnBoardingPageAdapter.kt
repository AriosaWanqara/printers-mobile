package com.example.printermobile.ui.Views.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.printermobile.R

class OnBoardingPageAdapter(private val context: Context) : PagerAdapter() {

    private val titles = listOf<Int>(
        R.string.onboarding_title_1,
        R.string.onboarding_title_2
    )
    private val contents = listOf<Int>(
        R.string.onboarding_content_1,
        R.string.onboarding_content_2
    )
    private val icons = listOf<Int>(
        R.drawable.ic_android,
        R.drawable.ic_usb
    )

    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int = titles.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.onboarding_slide_layout, container, false)

        val title = view.findViewById<TextView>(R.id.tvOnBoardingTitle)
        val content = view.findViewById<TextView>(R.id.tvOnBoardingText)
        val icon = view.findViewById<ImageView>(R.id.imOnBoardingIcon)

        title.setText(titles[position])
        content.setText(contents[position])
        icon.setImageResource(icons[position])
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(`object` as ConstraintLayout)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

}