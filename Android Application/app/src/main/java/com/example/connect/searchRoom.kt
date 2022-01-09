package com.example.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iterconnect.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class searchRoom : AppCompatActivity(), CustomAdapter2.OnRoomListener {
    var roomArrayList: ArrayList<Room?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_room)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val btn:Button = findViewById(R.id.btn_set_search)
        btn.setOnClickListener {
            val etTags = findViewById<View>(R.id.et_tags_sr) as EditText
            val etPos = findViewById<View>(R.id.et_positions_sr) as EditText
            val tags = etTags.text.toString()
            val positions = etPos.text.toString()
            setRecyclerViewData(tags, positions)
        }

    }

    private fun setRecyclerViewData(tags: String, positions: String) {
        val al = ArrayList<Room?>()
        val databaseRooms = FirebaseDatabase.getInstance()
        val refRooms = databaseRooms.getReference("rooms")

        Log.d("test rupel", "setRecyclerViewData: $tags $positions")
        refRooms.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                al.clear()
                for (postSnapshot in dataSnapshot.children) {
                    Log.d(
                        MainActivity.TAG, "onDataChange: " + postSnapshot.getValue(
                            Room::class.java
                        )?.id.toString()
                    )
                    val tagArr = tags.split(",")
                    val posArr = positions.split(",")
                    var flag1 = false
                    var flag2 = false
                    for(tag in tagArr){
                        Log.d("hello rupel", "onDataChange: $tag")
                        if (tag == "" || tag ==" "){continue}
                        if (postSnapshot.getValue(Room::class.java)?.tags.toString().contains(tag) and (postSnapshot.getValue(
                                Room::class.java
                            )?.roomId.toString() == "") and (postSnapshot.getValue(
                                Room::class.java
                            )?.password.toString() == "")){
                            flag1 = true
                            break;
                        }
                    }
                    for (pos in posArr){
                        if (pos == "" || pos ==" "){continue}
                        if (postSnapshot.getValue(Room::class.java)?.positions.toString().contains(pos) and (postSnapshot.getValue(
                                Room::class.java
                            )?.roomId.toString() == "") and (postSnapshot.getValue(Room::class.java)?.password.toString() == "")
                        ){

                            flag2 = true
                            break
                        }
                    }

                    if (flag1 || flag2){
                        al.add(postSnapshot.getValue(Room::class.java))
                    }
                }
                roomArrayList = al
                setRecyclerView(al)
                refRooms.removeEventListener(this)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(MainActivity.TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    fun setRecyclerView(al: ArrayList<Room?>) {
        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = CustomAdapter2(al,this)
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
}