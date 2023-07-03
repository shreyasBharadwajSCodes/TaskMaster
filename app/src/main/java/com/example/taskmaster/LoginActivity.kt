package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var _binding:ActivityLoginBinding

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finishAndRemoveTask();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = _binding.root

        setContentView(view)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        _binding.signInButton.setOnClickListener { startActivity(Intent(this@LoginActivity,MainCardPageActivity::class.java)) }

    }



}

/*

private val launcherSignInAFR = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        Toast.makeText(this,"" + result.resultCode + "" +  Activity.RESULT_OK,Toast.LENGTH_SHORT).show()

        if (result.resultCode == Activity.RESULT_OK) {

            Toast.makeText(this,"Here2",Toast.LENGTH_SHORT).show()

            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }}

 private fun signIn() {
        Toast.makeText(this,"Here sign in ",Toast.LENGTH_SHORT).show()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso!!);
        val signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent()
        launcherSignInAFR.launch(signInIntent)

    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = completedTask.getResult(ApiException::class.java)
                val personName = acct!!.displayName
                val personGivenName = acct!!.givenName
                val personFamilyName = acct!!.familyName
                val personEmail = acct!!.email
                val personId = acct!!.id
                val personPhoto: Uri? = acct!!.photoUrl
            Toast.makeText(this,personName,Toast.LENGTH_SHORT).show()
            // Signed in successfully, show authenticated UI

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }
 */