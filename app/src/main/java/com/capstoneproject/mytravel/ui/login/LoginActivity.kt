package com.capstoneproject.mytravel.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.mytravel.*
import com.capstoneproject.mytravel.databinding.ActivityLoginBinding
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
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

class LoginActivity : AppCompatActivity() {
    
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.connectionFailed.observe(this){
            alertShow(it)
        }

        loginViewModel.isLoading.observe(this){
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

                val name = account.displayName.toString()
                val email = account.email.toString()
                val photoUrl = account.photoUrl.toString()
                loginViewModel.loginProcess(account.email.toString())
                var isToken = false
                loginViewModel.token.observe(this){
                    if(!isToken) {
                        if (it != null) {
                            Log.d(TAG, getString(R.string.firebase_auth_with_google) + account.id)
                            firebaseAuthWithGoogle(account.idToken!!)
                            val tokenBearer = "Bearer $it"
                            loginViewModel.getProfile(tokenBearer, email, photoUrl)
                        } else {
                            val data =
                                UserModel(0, photoUrl, name, email, getString(R.string.location), 0, "", false, "")
                            val intentToDetail =
                                Intent(this@LoginActivity, FirstSetupActivity::class.java)
                            intentToDetail.putExtra("DATA", data)
                            startActivity(intentToDetail)
                            finish()
                        }
                    isToken = true
                    }
                }
            } catch (e: ApiException) {
                Log.w(TAG, getString(R.string.google_signin_failed), e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, getString(R.string.signin_with_credential_success))
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, getString(R.string.signin_with_credential_failed), task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
    }
    private fun alertShow(alertShow: Boolean) {
        if (alertShow)
            AlertDialog.Builder(this@LoginActivity).apply {
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
    }
}