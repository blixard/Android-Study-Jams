package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import android.widget.EditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.Button
import com.example.iterconnect.Room
import com.example.iterconnect.Users

class CreateRoom : AppCompatActivity() {
    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)
        val btnCreateRoom = findViewById<Button>(R.id.btn_create_room)
        btnCreateRoom.setOnClickListener {
            val etRoomName = findViewById<View>(R.id.et_roomname_cr) as EditText
            val etRoomId = findViewById<View>(R.id.et_roomid_cr) as EditText
            val etRoomPass = findViewById<View>(R.id.et_roompass_cr) as EditText
            val etRoomDes = findViewById<View>(R.id.et_description_cr) as EditText
            val etRoomTags = findViewById<View>(R.id.et_tags_cr) as EditText
            val etRoomPositions = findViewById<View>(R.id.et_positions_cr) as EditText
            val roomName = etRoomName.text.toString()
            val roomId = etRoomId.text.toString()
            val roomPass = etRoomPass.text.toString()
            val roomDescription = etRoomDes.text.toString()
            val roomTags = etRoomTags.text.toString()
            val roomPositions = etRoomPositions.text.toString()


            // TODO: 23-10-2021  check if room id is not there already in database
            setRoomFirebase(roomName, roomId, roomPass , roomDescription, roomTags, roomPositions)
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRoominUser(roomId: String?) {
        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        var userId = ""
        if (acct != null) {
            userId = acct.id
        }
        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.getReference("users/$userId")
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                user!!.rooms = user.rooms.toString() + ";" + roomId
                databaseReference!!.setValue(user)
                databaseReference!!.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // TODO: 22-10-2021 add user's id to users of Room Object while pushing Room class
    private fun setRoomFirebase(roomName: String, roomId: String, roomPass: String , roomDescription: String , roomTags : String , roomPositions:String ) {
        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.getReference("rooms")
        val id = databaseReference!!.push().key
        val idOfficial = id + "_official"
        val idUnofficial = id + "_un"
        val roomOfficial = Room(idOfficial, roomName + "_official", "users", roomId, roomPass, roomDescription,roomTags,roomPositions)
        databaseReference!!.child(idOfficial!!).setValue(roomOfficial)

        val roomUn = Room(idUnofficial, roomName + "_unofficial", "users", "", "",roomDescription,roomTags,roomPositions)
        databaseReference!!.child(idUnofficial!!).setValue(roomUn)

        setRoominUser("$idOfficial;$idUnofficial")

        Log.d("rrrTAG", "setRoomFirebase: official : " + idOfficial )
        Log.d("rrrTAG", "setRoomFirebase: unofficial : " + idUnofficial )
    }
}