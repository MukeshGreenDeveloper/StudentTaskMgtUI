package mukesh.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import mukesh.login.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.changeNotificaiton.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        binding.clickChat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        binding.calMenu.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }
        binding.profileMenu.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}