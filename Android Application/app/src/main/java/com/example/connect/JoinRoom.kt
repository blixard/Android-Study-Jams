package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.widget.EditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.util.Log
import android.view.*
import com.example.iterconnect.Room
import com.example.iterconnect.Users

class JoinRoom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_room)
        joinRoomfun()
    }

    private fun joinRoomfun() {
        val etRoomid = findViewById<EditText>(R.id.et_roomid_jr)
        val etRoomPass = findViewById<EditText>(R.id.et_roompass_jr)
        findViewById<View>(R.id.btn_joinroom).setOnClickListener {
            joinRoomFromDB(etRoomid.text.toString(), etRoomPass.text.toString())
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun joinRoomFromDB(roomId: String, roomPass: String) {
        val databaseRooms = FirebaseDatabase.getInstance()
        val refRooms = databaseRooms.getReference("rooms")
        refRooms.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    Log.d(
                        TAG, "onDataChange: " + postSnapshot.getValue(
                            Room::class.java
                        )?.id.toString()
                    )
                    if (postSnapshot.getValue(Room::class.java)
                            ?.roomId == roomId && postSnapshot.getValue(
                            Room::class.java
                        )?.password == roomPass
                    ) {
                        addRoomToUser(postSnapshot.getValue(Room::class.java)?.id)
                        break
                    }
                }
                refRooms.removeEventListener(this)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    private fun addRoomToUser(id: String?) {
        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        var userId = ""
        if (acct != null) {
            userId = acct.id
        }
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("users/$userId")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                if (!user!!.rooms.contains(id!!)) {
                    user.rooms = user.rooms.toString() + ";" + id
                    databaseReference.setValue(user)
                }
                databaseReference.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    companion object {
        const val TAG = "Join Room"
    }
}