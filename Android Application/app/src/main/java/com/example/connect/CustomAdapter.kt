package com.example.connect


import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import com.example.iterconnect.Room
import java.util.ArrayList

class CustomAdapter
/**
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used
 * by RecyclerView.
 */(private val localDataSet: ArrayList<Room?>, private val monRoomListener: MainActivity) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, var onRoomListener: MainActivity) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView
        val imageView: ImageView
        override fun onClick(view: View) {
            onRoomListener.onRoomClick(adapterPosition)
        }

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById<View>(R.id.tv_roomname) as TextView
            imageView = view.findViewById<View>(R.id.iv_chatroom_logo) as ImageView
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
            .inflate(R.layout.text_row_item, viewGroup, false)
        return ViewHolder(view, monRoomListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = localDataSet[position]?.roomName
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return localDataSet.size
    }
}