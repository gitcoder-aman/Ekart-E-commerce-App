package com.tech.ekart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tech.ekart.databinding.ViewpagerImageItemBinding

class ViewPager2Img : RecyclerView.Adapter<ViewPager2Img.ViewPager2ImgViewHolder>() {

    inner class ViewPager2ImgViewHolder(val binding : ViewpagerImageItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(imagePath : String){
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)
        }
    }

   private val diffCallback = object  : DiffUtil.ItemCallback<String>(){
       override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
           return oldItem == newItem
       }

       override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
          return oldItem == newItem
       }
   }
    val differ = AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImgViewHolder {
        return ViewPager2ImgViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPager2ImgViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }


}