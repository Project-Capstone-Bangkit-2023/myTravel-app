package com.capstoneproject.mytravel.ui.register

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.mytravel.HomeActivity
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.databinding.ActivityFirstSetupCategoryBinding
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class FirstSetupCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstSetupCategoryBinding
    private lateinit var firstSetupViewModel: FirstSetupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstSetupCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val data = intent.getParcelableExtra<UserModel>("DATA")
        println(data)
        val name = data?.name.toString()
        val email = data?.email.toString()
        val photoUrl = data?.photo_url.toString()
        val sendLoc = data?.location.toString()
        val ageStr = data?.age.toString()
        val age = ageStr.toInt()

        val blue = Color.BLUE
        val white = Color.WHITE
        val borderWidth = 20
        val cornerRadius = 20f

        val blueBorder = GradientDrawable()
        blueBorder.shape = GradientDrawable.RECTANGLE
        blueBorder.setStroke(borderWidth, blue)
        blueBorder.cornerRadii = floatArrayOf(cornerRadius,cornerRadius,cornerRadius,cornerRadius,cornerRadius,cornerRadius,cornerRadius,cornerRadius)

        val whiteBorder = GradientDrawable()
        whiteBorder.shape = GradientDrawable.RECTANGLE
        whiteBorder.setStroke(borderWidth, white)

        var isChecked1 = false
        binding.checkboxImage1.setOnClickListener {
            isChecked1 = !isChecked1
            if (isChecked1) {
                binding.llCheckbox1.background = blueBorder
            } else {
                binding.llCheckbox1.background = whiteBorder
            }
        }

        var isChecked2 = false
        binding.checkboxImage2.setOnClickListener {
            isChecked2 = !isChecked2
            if (isChecked2) {
                binding.llCheckbox2.background = blueBorder
            } else {
                binding.llCheckbox2.background = whiteBorder
            }
        }

        var isChecked3 = false
        binding.checkboxImage3.setOnClickListener {
            isChecked3 = !isChecked3
            if (isChecked3) {
                binding.llCheckbox3.background = blueBorder
            } else {
                binding.llCheckbox3.background = whiteBorder
            }
        }

        var isChecked4 = false
        binding.checkboxImage4.setOnClickListener {
            isChecked4 = !isChecked4
            if (isChecked4) {
                binding.llCheckbox4.background = blueBorder
            } else {
                binding.llCheckbox4.background = whiteBorder
            }
        }

        var isChecked5 = false
        binding.checkboxImage5.setOnClickListener {
            isChecked5 = !isChecked5
            if (isChecked5) {
                binding.llCheckbox5.background = blueBorder
            } else {
                binding.llCheckbox5.background = whiteBorder
            }
        }

        var isChecked6 = false
        binding.checkboxImage6.setOnClickListener {
            isChecked6 = !isChecked6
            if (isChecked6) {
                binding.llCheckbox6.background = blueBorder
            } else {
                binding.llCheckbox6.background = whiteBorder
            }
        }

        firstSetupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[FirstSetupViewModel::class.java]

        firstSetupViewModel.isLoading.observe(this){
            showLoading(it)
        }
        firstSetupViewModel.isRegisterSuccess.observe(this){
            alertShow(it)
        }

        binding.btnFinish.setOnClickListener{
            val catPref = categoryCondition(isChecked1,isChecked2,isChecked3,isChecked4,isChecked5,isChecked6)
            if(catPref.length > 5){
                firstSetupViewModel.register(photoUrl,name,email,sendLoc,age,catPref)
            }else{
                Toast.makeText(
                    this,
                    getString(R.string.update_failed_category_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun alertShow(alertShow: Boolean) {
        if (alertShow) {
            AlertDialog.Builder(this@FirstSetupCategoryActivity).apply {
                setTitle(getString(R.string.register_success))
                setMessage(getString(R.string.register_success_text))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this@FirstSetupCategoryActivity).apply {
                setTitle(getString(R.string.register_failed))
                setMessage(getString(R.string.register_failed_text))
                setPositiveButton(getString(R.string.close)) { _, _ ->

                }
                create()
                show()
            }
        }
    }
    private fun categoryCondition(
        checked1: Boolean,
        checked2: Boolean,
        checked3: Boolean,
        checked4: Boolean,
        checked5: Boolean,
        checked6: Boolean
    ): String {
        val checkedList = mutableListOf<String>()

        if (checked1) {
            checkedList.add(getString(R.string.bahari))
        }

        if (checked2) {
            checkedList.add(getString(R.string.budaya))
        }

        if (checked3) {
            checkedList.add(getString(R.string.cagar_alam))
        }

        if (checked4) {
            checkedList.add(getString(R.string.taman_hiburan))
        }

        if (checked5) {
            checkedList.add(getString(R.string.tempat_ibadah))
        }

        if (checked6) {
            checkedList.add(getString(R.string.pusat_perbelanjaan))
        }

        return checkedList.joinToString(",")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}