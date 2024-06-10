package com.dicoding.com.storyapp.ui.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.com.storyapp.data.model.StoryModel
import com.dicoding.com.storyapp.databinding.ItemStoryBinding


class StoryAdapter(private val onItemClick: (StoryModel, ActivityOptionsCompat) -> Unit) :
    PagingDataAdapter<StoryModel, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    class MyViewHolder(
        private val binding: ItemStoryBinding,
        val onItemClick: (StoryModel, ActivityOptionsCompat) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryModel) {
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvItemDescription, "description"),
                    )
                onItemClick(story, optionsCompat)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryModel> =
            object : DiffUtil.ItemCallback<StoryModel>() {
                override fun areItemsTheSame(oldStory: StoryModel, newStory: StoryModel): Boolean {
                    return oldStory.id == newStory.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldStory: StoryModel,
                    newStory: StoryModel
                ): Boolean {
                    return oldStory == newStory
                }
            }
    }
}