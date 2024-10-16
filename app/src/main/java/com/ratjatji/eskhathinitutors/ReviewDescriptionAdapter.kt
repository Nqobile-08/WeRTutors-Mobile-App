package com.ratjatji.eskhathinitutors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewDescriptionAdapter()
//    private var reviewDescriptions: ArrayList<ReviewDesc>) :
//RecyclerView.Adapter<ReviewDescriptionAdapter.GoodDescriptionViewHolder>(){
//
////GoodReviewOptions = arrayOf(
////            "Incompatible teaching style", "Late arrival", "Lack of subject expertise", "Overload of information",
////            "Inadequate preparations", "Unprofessional behaviour", "Ineffective communication", "Struggles to adapt",
//    //        "Difficulty with virtual lessons"
////        )
//
////    BadReviewOptions = arrayOf(
////    "Effective communication", "strong subject knowledge", "arrived on time", "Explains topics well",
////    "Preparation & organisation", "Adaptability", "Provides additional work & resources", "Good time management"
//    // ,"Professional", "Continuous assessment" "Encouraging"
////    )
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodDescriptionViewHolder{
//       } val itemView = LayoutInflater.from(parent.context).inflate(
//        R.layout.item_good_description, parent, false)
//            return GoodDescriptionViewHolder(view)
//        }else{
//            val view: View = LayoutInflater.from(context).inflate(R.layout.item_bad_description, parent, false)
//            return BadDesciptionViewHolder(view)
//        }
//    }
//    override fun getItemViewType(position: Int): Int {
//        val currentMessage = messageList[position]
//
//    }
//    override fun getItemCount(): Int {
//        return messageList.size
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val currentMessage = messageList[position]
//
//        if(holder.javaClass == GoodDescriptionViewHolder::class.java){
//            val viewHolder = holder as GoodDescriptionViewHolder
//            holder.goodDescriptionText.text = currentMessage.message
//        }else{
//            val viewHolder = holder as BadDesciptionViewHolder
//            holder.badDescriptionText.text = currentMessage.message
//        }
//    }
//    class GoodDescriptionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val goodDescriptionText = itemView.findViewById<TextView>(R.id.tvGoodReviewDescrion)
//    }
//    class BadDesciptionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val badDescriptionText = itemView.findViewById<TextView>(R.id.tvBadReviewDescrion)
//    }
