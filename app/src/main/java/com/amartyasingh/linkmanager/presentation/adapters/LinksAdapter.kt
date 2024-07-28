package com.amartyasingh.linkmanager.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amartyasingh.linkmanager.data.models.LinksModel
import com.amartyasingh.linkmanager.databinding.LinksItemBinding
import com.amartyasingh.linkmanager.utils.DateUtil
import com.bumptech.glide.Glide

class  LinksAdapter(private val copyClickListener: (String) -> Unit) : RecyclerView.Adapter<LinksAdapter.LinksViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        val binding = LinksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class LinksViewHolder(private val binding: LinksItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            val link = differ.currentList[position]

            Glide.with(binding.linkImage.context).load(link.original_image).centerCrop().dontAnimate().into(binding.linkImage)
            binding.linkTitle.text = link.title
            binding.clickCounts.text = link.total_clicks.toString()
            binding.linkText.text = link.web_link
            binding.linkDate.text = DateUtil.formatTimestampToDate(link.created_at).toString()
            binding.copyBtn.setOnClickListener {
                copyClickListener(link.web_link)
            }

        }
    }

    private var callback = object : DiffUtil.ItemCallback<LinksModel>() {
        override fun areItemsTheSame(oldItem: LinksModel, newItem: LinksModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LinksModel, newItem: LinksModel): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, callback)


}