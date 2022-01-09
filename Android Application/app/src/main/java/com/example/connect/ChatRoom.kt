package com.example.connect

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.media.MediaRecorder
import com.google.firebase.storage.StorageReference
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import android.graphics.Color
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.example.iterconnect.Room
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class ChatRoom : AppCompatActivity(), MessageAdapter.OnRoomListener {
    private var currentRoom: Room? = null
    private var roomId: String? = null
    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var chatArrayList: ArrayList<Message?>? = null
    private var permissionToRecordAccepted = false
    private var permissionToStore = false
    private var recorder: MediaRecorder? = null
    private var fileName: String? = null
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var storage: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setLayoutSize()
        val bundle = intent.extras
        currentRoom = bundle!!["room"] as Room?
        roomId = currentRoom?.id
        val chatRoomName = findViewById<View>(R.id.tv_chatroom_name) as TextView
        chatRoomName.text = currentRoom?.roomName
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_PERMISSION_CODE
            )
        }

        setRecyclerViewData()
        setMessageBtn()
        setRecordBtn()
    }

    private fun setRecordBtn() {
        val recordBtn = findViewById<View>(R.id.iv_mic_record_btn) as ImageView
        recordBtn.setColorFilter(Color.GREEN)
        recordBtn.setOnTouchListener { view, motionEvent ->
            val chatRoomName = findViewById<View>(R.id.tv_chatroom_name) as TextView
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(applicationContext, "holding", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onTouch: holding rupel")
                recordBtn.setColorFilter(Color.RED)
                chatRoomName.text = "RECORDING..."
                onHoldRecord()
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                Toast.makeText(applicationContext, "relased", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onTouch: released rupel")
                recordBtn.setColorFilter(Color.GREEN)
                chatRoomName.text = currentRoom?.roomName
                onReleaseRecord()
            }
            true
        }
    }

    private fun onHoldRecord() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        }
        var dtf: DateTimeFormatter? = null
        var datetime = "lowversionp"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
            val now = LocalDateTime.now()
            println(dtf.format(now))
            datetime = "audio-" + dtf.format(now)
        }
        fileName = externalCacheDir!!.absolutePath
        fileName += "/$datetime.3gp"
        onRecord(true)
    }

    private fun onReleaseRecord() {
        onRecord(false)
    }

    //    audio recording
    //    permission to record audio
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> permissionToRecordAccepted =
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            EXTERNAL_STORAGE_PERMISSION_CODE -> permissionToStore =
                grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!permissionToRecordAccepted) finish()
        if (!permissionToStore) finish()
    }

    private fun onRecord(start: Boolean) {
        if (start) {
            startRecording()
        } else {
            stopRecording()
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        Log.d(TAG, "startRecording: $fileName")
        recorder!!.setOutputFile(fileName)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }
        recorder!!.start()
    }

    private fun stopRecording() {
        recorder!!.stop()
        recorder!!.release()
        recorder = null
        uploadAudio()
    }

    private fun uploadAudio() {
        val nameFile = fileName!!.split("/").toTypedArray()
        val name = nameFile[nameFile.size - 1]
        storage = FirebaseStorage.getInstance().reference
        val storageRef = storage!!.child("Audio/$name")
        Log.d(TAG, "uploadAudio: name file : $name")
        val uri = Uri.fromFile(File(fileName))
        storageRef.putFile(uri).addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "succefully uploaded in firebase storage",
                Toast.LENGTH_SHORT
            ).show()
            sendMessageFb(name)
        }
    }

    //    setting the layout size
    private fun setLayoutSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        val rv = findViewById<RecyclerView>(R.id.rv_chat)
        val rv_lp = rv.layoutParams
        rv_lp.height = height - 380
        rv.layoutParams = rv_lp
    }

    // setting the message Button
    private fun setMessageBtn() {
        val et_msg = findViewById<EditText>(R.id.et_msg_ml)
        val btn = findViewById<ImageView>(R.id.iv_btn_send_msg)
        btn.isEnabled = false
//        btn.setImageResource(R.mipmap.send_msg_arrow_deactivated_round)
        et_msg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (et_msg.text.toString() == "") {
                    btn.isEnabled = false
//                    btn.setImageResource(R.mipmap.send_msg_arrow_deactivated_round)
                } else {
                    btn.isEnabled = true
//                    btn.setImageResource(R.mipmap.send_msg_arrow_activated_round)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        btn.setOnClickListener {
            val et_msg = findViewById<EditText>(R.id.et_msg_ml)
            val message = et_msg.text.toString()
            sendMessageFb(message)
            et_msg.setText("")
        }
    }

    private fun sendMessageFb(message: String) {

//        date time
        var dtf: DateTimeFormatter? = null
        var datetime = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
            val now = LocalDateTime.now()
            println(dtf.format(now))
            datetime = dtf.format(now)
        }
        val date = datetime.split(" ").toTypedArray()[0]
        println(date)
        val time = datetime.split(" ").toTypedArray()[1]
        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        var userId: String? = ""
        var userName: String? = ""
        if (acct != null) {
            userId = acct.id
            userName = acct.displayName
        }
        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.getReference("rooms/$roomId/messages")
        val messageId = databaseReference!!.push().key
        val msg = Message(messageId, roomId, userId, userName, message, date, time)
        databaseReference!!.child(messageId!!).setValue(msg)
    }

    override fun onRoomClick(position: Int) {}
    fun setRecyclerViewData() {
        val al = ArrayList<Message?>()
        val databaseChat = FirebaseDatabase.getInstance()
        val refChat = databaseChat.getReference("rooms/$roomId/messages")
        refChat.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                al.clear()
                for (postSnapshot in dataSnapshot.children) {
//                    Log.d(TAG, "onDataChange: " +postSnapshot.getValue(Message.class).getMessageId().toString());
                    al.add(postSnapshot.getValue(Message::class.java))
                }
                chatArrayList = al
                setRecyclerView(al)
                //                refChat.removeEventListener(this);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    fun setRecyclerView(al: ArrayList<Message?>) {
        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        var userId = ""
        if (acct != null) {
            userId = acct.id
        }
        val rv = findViewById<RecyclerView>(R.id.rv_chat)
        val l = LinearLayoutManager(this)
        l.stackFromEnd = true
        rv.layoutManager = l
        rv.adapter = MessageAdapter(al, this, userId)
        val space = SpaceMessagesRv(20)
        if (rv.itemDecorationCount > 0) {
            rv.removeItemDecorationAt(0)
        }
        rv.addItemDecoration(space)
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 100
        private const val EXTERNAL_STORAGE_PERMISSION_CODE = 110
        const val TAG = "chatRoom"
        private const val LOG_TAG = "audioRecord logs cr"
    }
}