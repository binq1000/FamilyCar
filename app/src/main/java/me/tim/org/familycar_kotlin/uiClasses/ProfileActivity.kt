package me.tim.org.familycar_kotlin.uiClasses

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_profile.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_profile.*
import me.tim.org.familycar_kotlin.R
import me.tim.org.familycar_kotlin.data.Driver
import java.util.*


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        //Get driver from intent
        val driverString = intent.extras.get("Driver") as String
        val driver = Driver.fromJSON(driverString)
        this.title = driver.name

        setEcoScore()

        Picasso.with(this)
                .load("https://www.pekoda.com/images/default.png")
                .into(ivProfilePic)
    }

    fun setEcoScore() {
        val random = Random()

        val animation = CircleAngleAnimation(circle, random.nextInt(101))
        animation.duration = 1000
        circle.startAnimation(animation)
    }

}
