package mukesh.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import mukesh.login.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var vm: ChatVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(ChatVM::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.apply {
            lifecycleOwner = this@ProfileActivity
            viewModel = vm
        }
        /*vm.selectedModeMonth.observe(this, Observer {
            when(it){
                0 -> {}
                1->{}
                2->{}
            }
        })*/
        binding.txtWeek.setOnClickListener {
            vm.selectedModeMonth.value = 0
        }
        binding.txtMonth.setOnClickListener {
            vm.selectedModeMonth.value = 1
        }
        binding.txtAllTime.setOnClickListener {
            vm.selectedModeMonth.value = 2
        }
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}