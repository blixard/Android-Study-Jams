package com.example.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iterconnect.Room
import com.example.iterconnect.Users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.*
import java.util.ArrayList
import android.content.SharedPreferences




class MainActivity : AppCompatActivity(), CustomAdapter.OnRoomListener {
    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var userId: String? = null
    var roomArrayList: ArrayList<Room?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getUserId()
        setNavBtns()

    }

    private fun setNavBtns() {
        findViewById<View>(R.id.iv_create_room_btn).setOnClickListener {
            val intent = Intent(applicationContext, MenuScreen::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.iv_setting_btn).setOnClickListener {
            val intent = Intent(applicationContext, SettingPage::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.iv_search_btn).setOnClickListener {
            val intent = Intent(applicationContext, searchRoom::class.java)
            startActivity(intent)
        }
    }

    private fun getUserId() {
        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (acct != null) {
            userId = acct.id
            roomsList
        }
    }

    //                databaseReference.removeEventListener(this);
    private val roomsList: Unit
        private get() {
            database = FirebaseDatabase.getInstance()
            databaseReference = database!!.getReference("users/$userId")
            databaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Users::class.java)
                    val rooms = user!!.rooms.toString()
                    setRecyclerViewData(rooms)
                    //                databaseReference.removeEventListener(this);
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

    fun setRecyclerViewData(rooms: String) {
        Log.d(TAG, "setRecyclerViewData: $rooms")
        val al = ArrayList<Room?>()
        val databaseRooms = FirebaseDatabase.getInstance()
        val refRooms = databaseRooms.getReference("rooms")
        refRooms.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                al.clear()
                for (postSnapshot in dataSnapshot.children) {
                    Log.d(
                        TAG, "onDataChange: " + postSnapshot.getValue(
                            Room::class.java
                        )?.id.toString()
                    )
                    if (rooms.contains(
                            postSnapshot.getValue(Room::class.java)?.id.toString()
                        )
                    ) {
                        al.add(postSnapshot.getValue(Room::class.java))
                    }
                }
                roomArrayList = al
                setRecyclerView(al)
                refRooms.removeEventListener(this)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    fun setRecyclerView(al: ArrayList<Room?>) {
        val rv = findViewById<RecyclerView>(R.id.rv_rooms)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = CustomAdapter(al, this)
        val space = SpaceMessagesRv(20)
        if (rv.itemDecorationCount > 0) {
            rv.removeItemDecorationAt(0)
        }
        rv.addItemDecoration(space)
    }

    override fun onRoomClick(position: Int) {
        val room = roomArrayList!![position]
        val intent = Intent(applicationContext, ChatRoom::class.java)
        val b = Bundle()
        b.putSerializable("room", room)
        intent.putExtras(b)
        Toast.makeText(applicationContext, room?.roomName, Toast.LENGTH_LONG).show()
        startActivity(intent)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}