package com.gamerguide.android.starterapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import com.gamerguide.android.starterapp.databinding.ActivityMainBinding
import com.gamerguide.android.starterapp.databinding.ActivitySecondBinding
import com.gamerguide.android.starterapp.helpers.BlurHelper
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@SuppressLint("SetTextI18n")
class SecondActivity : AppCompatActivity() {

    private var background: ImageView? = null
    private var blurHelper: BlurHelper? = null
    private lateinit var binding: ActivitySecondBinding

    private var gameScore = 0;

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    //Restore any data saved between configuration changes
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        gameScore = savedInstanceState.getInt(GAME_SCORE_ID)
        binding.score.text = "GAME SCORE: ${gameScore}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Use ViewPump to hook into default fonts
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/sourcesanspro.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        //Get the data passed from parent activity through Intent
        gameScore = intent.extras?.get(GAME_SCORE_ID) as Int;

        //Create View Binding instance for current Activity
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        blurHelper = BlurHelper()
        blurHelper!!.setupImageBlurBackground(this, binding.background!!)


    }

    //Do all UI related work in onResume when app recieved focus
    override fun onResume() {
        super.onResume()

        binding.title.text = "SECOND ACTIVITY"
        binding.score.text = "GAME SCORE: $gameScore"
        binding.reload.setOnClickListener {
            blurHelper!!.setupImageBlurBackground(
                this,
                binding.background, true
            )
        }

        //Increase score when user press increase button
        binding.increase.setOnClickListener {
            gameScore++
            binding.score.text = "GAME SCORE: $gameScore"
        }

        //Decrease score when user press decrease button
        binding.decrease.setOnClickListener {
            gameScore--
            binding.score.text = "GAME SCORE: $gameScore"
        }

        //Finish the activity when user presses finish button and send data to parent activity through a intent and set status code
        binding.finish.setOnClickListener {
            val intent: Intent = Intent().apply {
                putExtra(GAME_SCORE_ID, gameScore)
                setResult(RESULT_OK, this);
            };
            finish();
        }
    }

    //Save data here to maintain state between configuration changes
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(GAME_SCORE_ID, gameScore);
        super.onSaveInstanceState(outState)
    }
}