package com.naojianghh.yijie2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var editText : EditText
    private lateinit var text1 : ImageView
    private lateinit var text2 : ImageView
    private lateinit var text3 : ImageView
    private lateinit var text4 : ImageView
    private lateinit var button2: Button
    private lateinit var image1 : ImageView
    private lateinit var image2 : ImageView
    private var dotCount = 1
    private lateinit var text2Handler: Handler
    private lateinit var text2Runnable: Runnable
    private lateinit var loadingText : TextView

    private val inputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText = findViewById(R.id.edit_text)
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)
        text3 = findViewById(R.id.text3)
        text4 = findViewById(R.id.text4)
        button2 = findViewById(R.id.button2)
        image1 = findViewById(R.id.image_launch)
        image2 = findViewById(R.id.image_more)
        loadingText = findViewById(R.id.loading_text)

        text1.visibility = View.VISIBLE
        text2.visibility = View.GONE
        text3.visibility = View.GONE
        text4.visibility = View.GONE
        image1.visibility = View.GONE
        loadingText.visibility = View.GONE


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                if (text.isNotEmpty()){
                    image1.visibility = View.VISIBLE
                    image2.visibility = View.GONE
                } else {
                    image1.visibility = View.GONE
                    image2.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        text2Handler = Handler(Looper.getMainLooper())
        text2Runnable = object : Runnable {
            override fun run() {
                when (dotCount) {
                    1 -> loadingText.setText("正在思考中.")
                    2 -> loadingText.setText("正在思考中..")
                    3 -> loadingText.setText("正在思考中...")
                }
                dotCount = (dotCount % 3) + 1
                text2Handler.postDelayed(this, 500) // 每500毫秒切换一次
            }
        }
        text2Handler.post(text2Runnable)

        text4.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {
                var intent : Intent
                intent = Intent(this@MainActivity, PictureDetailActivity::class.java)
                startActivity(intent)
            }

        })
        button2.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                editText.text.clear()
                hideKeyboard()
                showWithAnimation(text3, R.anim.anim_enter_right)
                loadingText.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    loadingText.visibility = View.GONE
                    text2.visibility = View.VISIBLE
                }, 3000)

                Glide.with(this@MainActivity)
                    .asGif()
                    .load(R.drawable.loading)
                    .into(findViewById(R.id.text4))
                text4.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    text4.setImageResource(R.drawable.text4)
                }, 7000)
            }

        })

    }

    private fun showWithAnimation(view: View, animRes: Int, startDelay: Long = 0) {
        view.visibility = View.VISIBLE
        val animation : Animation = AnimationUtils.loadAnimation(this, animRes)
        animation.startOffset = startDelay
        view.startAnimation(animation)
    }

    private fun hideKeyboard() {
        val currentFocus = currentFocus ?: return
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

}