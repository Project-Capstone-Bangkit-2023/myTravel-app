package com.capstoneproject.mytravel.ui.setting

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.mytravel.HomeActivity
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.databinding.ActivityEditProfileBinding
import com.capstoneproject.mytravel.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        editProfileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[EditProfileViewModel::class.java]

        editProfileViewModel.isLoading.observe(this){
            showLoading(it)
        }
        editProfileViewModel.isUpdateSuccess.observe(this){
            alertShow(it)
        }

        editProfileViewModel.getUser().observe(this){
            val token = it.token
            val userId = it.userId
            val age = it.age.toString()
            val location = it.location
            binding.ageEditText.setText(age)
            binding.locationEditText.setText(location)
            binding.btnFinish.setOnClickListener{
                val updateAge = (binding.ageEditText.text.toString()).toInt()
                val updateLocation = binding.locationEditText.text.toString()
                val catPref = categoryCondition(isChecked1,isChecked2,isChecked3,isChecked4,isChecked5,isChecked6)
                editProfileViewModel.updateProfile(token, userId, updateLocation, updateAge, catPref)
            }
        }
    }

    private fun alertShow(alertShow: Boolean) {
        if (alertShow) {
            AlertDialog.Builder(this@EditProfileActivity).apply {
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
            AlertDialog.Builder(this@EditProfileActivity).apply {
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
            checkedList.add("Bahari")
        }

        if (checked2) {
            checkedList.add("Budaya")
        }

        if (checked3) {
            checkedList.add("Cagar Alam")
        }

        if (checked4) {
            checkedList.add("Taman Hiburan")
        }

        if (checked5) {
            checkedList.add("Tempat Ibadah")
        }

        if (checked6) {
            checkedList.add("Pusat Perbelanjaan")
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