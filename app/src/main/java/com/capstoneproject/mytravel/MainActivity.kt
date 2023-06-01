package com.capstoneproject.mytravel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.mytravel.databinding.ActivityMainBinding
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.User
import com.capstoneproject.mytravel.ui.register.FirstSetupActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
        binding.signinButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.connectionFailed.observe(this){
            alertShow(it)
        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                println(account.account)
                println(account.id)
                println(account.idToken)
                println(account.email)
                println(account.displayName)
                println(account.familyName)
                println(account.givenName)
                println(account.photoUrl)

                val name = account.displayName.toString()
                val email = account.email.toString()
                val photoUrl = account.photoUrl.toString()
                mainViewModel.loginProcess(account.email.toString())
                fun loginRegisterCondition(token: String?) {
                    if (token != null) {
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                        firebaseAuthWithGoogle(account.idToken!!)
                    }
                    else{
                        val data = UserModel(0, photoUrl, name, email, "location", 0, false, "")
                        val intentToDetail = Intent(this@MainActivity, FirstSetupActivity::class.java)
                        intentToDetail.putExtra("DATA", data)
                        startActivity(intentToDetail)
                        finish()
                    }
            }
                mainViewModel.token.observe(this){ loginRegisterCondition(it) }

            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish()
        }
    }
    private fun alertShow(alertShow: Boolean) {
        if (alertShow)
            AlertDialog.Builder(this@MainActivity).apply {
                setTitle(getString(R.string.login_failed))
                setMessage(getString(R.string.login_failed_text))
                setPositiveButton(getString(R.string.close)) { _, _ -> }
                create()
                show()
            }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        private const val TAG = "LoginActivity"
        var EXTRA_TOKEN = ""
    }
}