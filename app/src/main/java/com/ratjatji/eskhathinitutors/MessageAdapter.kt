package com.ratjatji.eskhathinitutors

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(
    private val context: Context,
    private val messageList: ArrayList<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_DATE = 0
        const val ITEM_RECEIVE = 1
        const val ITEM_SENT = 2
        private const val TAG = "MessageAdapter"
    }

    private val items = ArrayList<Any>()

    init {
        processMessages()
    }

    private fun processMessages() {
        try {
            items.clear()
            var currentDate = -1L

            messageList.forEach { message ->
                if (message.timestamp == 0L) {
                    Log.w(TAG, "Message with zero timestamp found: $message")
                    return@forEach
                }

                val messageDate = Calendar.getInstance().apply {
                    timeInMillis = message.timestamp
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                if (messageDate != currentDate) {
                    items.add(DateHeader(messageDate))
                    currentDate = messageDate
                }
                items.add(message)
            }
            Log.d(TAG, "Processed ${items.size} items (${messageList.size} messages)")
        } catch (e: Exception) {
            Log.e(TAG, "Error processing messages", e)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return try {
            when (viewType) {
                ITEM_DATE -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.date_header, parent, false)
                    DateViewHolder(view)
                }
                ITEM_RECEIVE -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
                    ReceiveViewHolder(view)
                }
                else -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
                    SentViewHolder(view)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating ViewHolder for viewType: $viewType", e)
            throw e
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            when (val item = items.getOrNull(position)) {
                is DateHeader -> {
                    (holder as? DateViewHolder)?.let {
                        it.dateText.text = formatDate(item.timestamp, "EEEE, MMM d")
                    }
                }
                is Message -> {
                    when (holder) {
                        is SentViewHolder -> bindSentMessage(holder, item)
                        is ReceiveViewHolder -> bindReceivedMessage(holder, item)
                    }
                }
                null -> Log.e(TAG, "Null item at position $position")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error binding ViewHolder at position: $position", e)
        }
    }

    private fun bindSentMessage(holder: SentViewHolder, message: Message) {
        try {
            holder.apply {
                sentMessage.text = message.message ?: ""
                sentTime.text = formatDate(message.timestamp, "hh:mm a")

                when (message.type) {
                    "image" -> {
                        sentMessage.visibility = View.GONE
                        sentImage.visibility = View.VISIBLE
                        Glide.with(context)
                            .load(message.message)
                            .error(R.drawable.ic_error_placeholder) // Add an error placeholder drawable
                            .into(sentImage)
                    }
                    else -> {
                        sentMessage.visibility = View.VISIBLE
                        sentImage.visibility = View.GONE
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error binding sent message", e)
        }
    }

    private fun bindReceivedMessage(holder: ReceiveViewHolder, message: Message) {
        try {
            holder.apply {
                receiveMessage.text = message.message ?: ""
                receiveTime.text = formatDate(message.timestamp, "hh:mm a")

                when (message.type) {
                    "image" -> {
                        receiveMessage.visibility = View.GONE
                        receiveImage.visibility = View.VISIBLE
                        Glide.with(context)
                            .load(message.message)
                            .error(R.drawable.ic_error_placeholder) // Add an error placeholder drawable
                            .into(receiveImage)
                    }
                    else -> {
                        receiveMessage.visibility = View.VISIBLE
                        receiveImage.visibility = View.GONE
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error binding received message", e)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            when (val item = items.getOrNull(position)) {
                is DateHeader -> ITEM_DATE
                is Message -> {
                    val senderId = FirebaseAuth.getInstance().currentUser?.uid
                    if (item.senderId == senderId) ITEM_SENT else ITEM_RECEIVE
                }
                else -> {
                    Log.e(TAG, "Unknown item type at position $position")
                    ITEM_RECEIVE // Default to receive as fallback
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting item view type for position: $position", e)
            ITEM_RECEIVE // Default to receive as fallback
        }
    }

    override fun getItemCount(): Int = items.size

    private fun formatDate(timestamp: Long, pattern: String): String {
        return try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting date for timestamp: $timestamp", e)
            ""
        }
    }

    fun updateMessages() {
        try {
            processMessages()
            notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating messages", e)
        }
    }

    private data class DateHeader(val timestamp: Long)

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.date_header)
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.txt_send_message)
        val sentImage: ImageView = itemView.findViewById(R.id.img_send_message)
        val sentTime: TextView = itemView.findViewById(R.id.txt_send_time)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.txt_receive_message)
        val receiveImage: ImageView = itemView.findViewById(R.id.img_receive_message)
        val receiveTime: TextView = itemView.findViewById(R.id.txt_receive_time)
    }
}
