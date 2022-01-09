package com.example.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.iterconnect.Room
import com.example.iterconnect.Users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SigninPage : AppCompatActivity() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_page)
        creatSigninRequest()
        //        checkLastSignin();


        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        //        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener { signIn() }
    }
    private fun creatSigninRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun checkLastSignin() {
        val acct = GoogleSignIn.getLastSignedInAccount(this)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            if (acct != null) {
                val personId = acct.id
                val intent = Intent(applicationContext, MainActivity::class.java)
                val database = FirebaseDatabase.getInstance()
                val userRef = database.getReference("users")
                userRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var inDatabase = false
                        for (postSnapshot in dataSnapshot.children) {
                            if (postSnapshot.getValue(Users::class.java)
                                    ?.personId == personId
                            ) {
                                inDatabase = true
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                        Log.d(TAG, "onDataChange: yololo$inDatabase")
                        if (inDatabase == false) {
                            val database = FirebaseDatabase.getInstance()
                            val userRef = database.getReference("users").child(personId)
                            var acct: GoogleSignInAccount? = null
                            try {
                                acct = completedTask.getResult(ApiException::class.java)
                            } catch (e: ApiException) {
                                e.printStackTrace()
                            }
                            // Signed in successfully, show authenticated UI.
                            if (acct != null) {
                                val personName = acct.displayName
                                val personGivenName = acct.givenName
                                val personFamilyName = acct.familyName
                                val personEmail = acct.email
                                val personId = acct.id
                                val personPhoto = acct.photoUrl
                                val photo: String
                                photo = personPhoto?.toString() ?: ""
                                val u = Users(
                                    personName,
                                    personGivenName,
                                    personFamilyName,
                                    personEmail,
                                    personId,
                                    photo
                                )
                                u.addPersonalRoom(personId)
                                //                                add personal room in database
                                addRoom(personId)
                                userRef.setValue(u)
                            }
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        userRef.removeEventListener(this)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                        // ...
                    }
                })
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun addRoom(id: String) {
        val dbRoom = FirebaseDatabase.getInstance()
        val refRoom = dbRoom.getReference("rooms")
        val room = Room(id, "Chat bot", id, id, "adminPass@123","chat bot" , "bot","none")
        refRoom.child(id).setValue(room)
    }

    companion object {
        const val RC_SIGN_IN = 100
        const val TAG = "signin page"
    }
}