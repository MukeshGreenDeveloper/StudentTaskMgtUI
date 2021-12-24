package mukesh.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import mukesh.login.databinding.DialogGdprBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<ImageView>(R.id.enableInputType).setOnClickListener {
            Log.d("keytsss", "==>" + findViewById<EditText>(R.id.editTextPwd).inputType)
            if (findViewById<EditText>(R.id.editTextPwd).inputType == 129)
                findViewById<EditText>(R.id.editTextPwd).inputType =
                    InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            else if (findViewById<EditText>(R.id.editTextPwd).inputType == 112
                || findViewById<EditText>(R.id.editTextPwd).inputType == 128
            )
                findViewById<EditText>(R.id.editTextPwd).inputType = 129
        }
        findViewById<ImageView>(R.id.layoutball1).startAnimation(
            AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.anim_bals
            )
        )
        findViewById<ImageView>(R.id.layoutball2).startAnimation(
            AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.anim_bals
            )
        )
        findViewById<ImageView>(R.id.layoutball3).startAnimation(
            AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.anim_bals
            )
        )
        findViewById<View>(R.id.btnLogin).setOnClickListener {
            showBottomSheet()
        }


    }

    lateinit var dialogGdprBinding: DialogGdprBinding
    lateinit var dialog: BottomSheetDialog
    private fun showBottomSheet() {
        dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_gdpr, null)
        dialogGdprBinding = DialogGdprBinding.inflate(layoutInflater)
        dialog.setContentView(dialogGdprBinding.getRoot())
//        dialogGdprBinding.selectedOption = 1
        dialogGdprBinding.txtPush.setOnClickListener {
            dialogGdprBinding.selectedOption = 1
            doYourAnimation(R.anim.bounce, dialogGdprBinding.phoneImg)
            hideYourSelection(1)
        }
        dialogGdprBinding.txtEmail.setOnClickListener {
            dialogGdprBinding.selectedOption = 2
            doYourAnimation(R.anim.bounce, dialogGdprBinding.emailIMG)
            hideYourSelection(2)
        }
        dialogGdprBinding.txtSMS.setOnClickListener {
            dialogGdprBinding.selectedOption = 3
            doYourAnimation(R.anim.bounce, dialogGdprBinding.smsIMG)
            hideYourSelection(3)
        }
        dialogGdprBinding.btnsaveSettings.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
//            finish()
        }
        dialog.show()
    }

    private fun hideYourSelection(selectedOption: Int) {
        dialogGdprBinding.bgBall1.lyoutOFBalls.visibility =
            if (selectedOption == 1) View.VISIBLE else View.GONE
        dialogGdprBinding.bgBall2.lyoutOFBalls.visibility =
            if (selectedOption == 2) View.VISIBLE else View.GONE
        dialogGdprBinding.bgBall3.lyoutOFBalls.visibility =
            if (selectedOption == 3) View.VISIBLE else View.GONE
    }

    fun doYourAnimation(resoutID: Int, v: View) {
        v.apply {
            startAnimation(
                AnimationUtils.loadAnimation(
                    getApplicationContext(),
                    resoutID
                )
            )
        }
    }
}