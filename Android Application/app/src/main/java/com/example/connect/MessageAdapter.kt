package com.example.connect


import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import androidx.recyclerview.widget.RecyclerView
import android.media.MediaPlayer
import android.util.Log
import android.view.*
import android.widget.ImageView
import java.io.File
import java.io.IOException
import java.util.ArrayList

class MessageAdapter
/**
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used
 * by RecyclerView.
 */(
    private val localDataSet: ArrayList<Message?>,
    private val monRoomListener: OnRoomListener,
    private val userId: String
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private var player: MediaPlayer? = null
    //    private String fileName;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, var onRoomListener: OnRoomListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val username: TextView
        val msg: TextView
        val time: TextView
        val playBtn: ImageView
        override fun onClick(view: View) {
            onRoomListener.onRoomClick(adapterPosition)
        }

        init {
            // Define click listener for the ViewHolder's View
            username = view.findViewById<View>(R.id.tv_cr_name) as TextView
            msg = view.findViewById<View>(R.id.tv_cr_msg) as TextView
            time = view.findViewById<View>(R.id.tv_cr_time) as TextView
            playBtn = view.findViewById<View>(R.id.iv_btn_play_msg_audio) as ImageView
            view.setOnClickListener(this)
        }
    }

    interface OnRoomListener {
        fun onRoomClick(position: Int)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.message_list_rv, viewGroup, false)
        return ViewHolder(view, monRoomListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val msg = localDataSet[position]?.message
        viewHolder.username.text = localDataSet[position]?.userName
        viewHolder.msg.text = msg
        viewHolder.time.text = localDataSet[position]?.time
        val playBtn = viewHolder.playBtn
        playBtn.visibility = View.GONE
        if (msg!!.contains("audio-")) {
            val storageRef = FirebaseStorage.getInstance().reference.child("Audio/$msg")
            try {
                val localFile = File.createTempFile("audio", "3gp")
                Log.d("local file", "onCreate: " + localFile.absolutePath.toString())
                storageRef.getFile(localFile).addOnSuccessListener {
                    Log.d("local file", "onCreate: " + localFile.absolutePath.toString())
                    playBtn.visibility = View.VISIBLE
                    playBtn.setOnClickListener { onPlay(true, localFile.absolutePath.toString()) }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (userId == localDataSet[position]?.userId) {
            viewHolder.itemView.setBackgroundResource(R.drawable.speech_bubble)
        } else {
            viewHolder.itemView.setBackgroundResource(R.drawable.speech_bubble2)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return localDataSet.size
    }

    private fun onPlay(start: Boolean, nameOfFile: String) {
        if (start) {
            startPlaying(nameOfFile)
        } else {
            stopPlaying()
        }
    }

    private fun startPlaying(fileName: String) {
        player = MediaPlayer()
        try {
            player!!.setDataSource(fileName)
            player!!.prepare()
            player!!.start()
        } catch (e: IOException) {
            Log.e("LOG_TAG", "prepare() failed")
        }
    }

    private fun stopPlaying() {
        player!!.release()
        player = null
    }
}