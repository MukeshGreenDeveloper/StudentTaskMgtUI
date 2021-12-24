package mukesh.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import mukesh.login.databinding.ActivityChatBinding


class ChatActivity : AppCompatActivity() {
    lateinit var vm: ChatVM
    lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat)
//        binding = ActivityChatBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this).get(ChatVM::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        binding.apply {
            lifecycleOwner = this@ChatActivity
            viewModel = vm
        }
//        binding.viewModel = vm
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.imgSend.setOnClickListener {
            hideKeyboard(applicationContext, it)
            findViewById<EditText>(R.id.editMsg).text.clear()
        }
        binding.layoutFrame.adapter = ChatPagerAdatper(this)
        binding.layoutFrame.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                vm.selectedOption.value = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        binding.layoutFrame.currentItem = 2
        binding.layoutInformation.setOnClickListener {
            vm.selectedOption.value = 0
            binding.layoutFrame.currentItem = 0
        }
        binding.layoutFiles.setOnClickListener {
            vm.selectedOption.value = 1
            binding.layoutFrame.currentItem = 1
        }
        binding.layoutGroupChat.setOnClickListener {
            vm.selectedOption.value = 2
            binding.layoutFrame.currentItem = 2
        }
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}