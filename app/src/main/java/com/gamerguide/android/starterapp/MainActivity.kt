package com.gamerguide.android.starterapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.gamerguide.android.starterapp.helpers.BlurHelper
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.gamerguide.android.starterapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.calligraphy3.CalligraphyConfig

const val GAME_SCORE_ID = "game_score"
const val MAINACTIVITY_CODE = 123;

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {
    private var background: ImageView? = null
    private var blurHelper: BlurHelper? = null
    private lateinit var binding: ActivityMainBinding

    private var gameScore = 0;

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    //Restore the data stored in Bundle during configuration changes and implement your custom logic
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        gameScore = savedInstanceState.getInt(GAME_SCORE_ID)
        binding.score.text = "MAIN ACTIVITY"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Build a ViewPump for hooking into font
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

        //Create View Binding instance for the current activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        blurHelper = BlurHelper()


    }

    // OnResume is called when the app recieve focus, Do all UI related work here
    override fun onResume() {
        super.onResume()

        blurHelper!!.setupImageBlurBackground(this, binding.background)
        binding.title.text = "MAIN ACTIVITY"
        binding.score.text = "GAME SCORE: $gameScore"

        //Reload a new background theme when use clicks the refresh button
        binding.reload.setOnClickListener {
            blurHelper!!.setupImageBlurBackground(
                this,
                binding.background,
                true
            )
        }
        //Increase the score when user presses the button
        binding.increase.setOnClickListener {
            gameScore++
            binding.score.text = "GAME SCORE: $gameScore"
        }

        //Decrease the score when user presses the button
        binding.decrease.setOnClickListener {
            gameScore--
            binding.score.text = "GAME SCORE: $gameScore"
        }

        //Start new activity and send the data through a Intent
        binding.start.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(GAME_SCORE_ID, gameScore)
                startActivityForResult(this, MAINACTIVITY_CODE)
            }

        }


    }

    // Store any data in this bundle when app configuration change occurs
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(GAME_SCORE_ID, gameScore);
        super.onSaveInstanceState(outState)
    }

    // Check the result obtained from activity and compare which activity sent it and if was succes, If yes, Continue with your logic
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            MAINACTIVITY_CODE ->
                if (resultCode == RESULT_OK) {
                    gameScore = intent!!.getIntExtra(GAME_SCORE_ID, 0)
                    binding.score.text = "GAME SCORE: $gameScore"
                }
        }
    }


    // Check if user presses the back button and save any important work before the activity is destroyed
    override fun onBackPressed() {


        Snackbar.make(
            binding.root,
            "Save important data here before the Activity is killed..",
            Snackbar.LENGTH_SHORT
        ).setAction("OK", { super.onBackPressed() })
            .show()
    }


}