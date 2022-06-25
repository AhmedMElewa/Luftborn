package com.elewa.luftborn.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elewa.luftborn.data.model.TaskModel
import com.elewa.luftborn.databinding.RowItemBinding

class TaskAdapter() : ListAdapter<TaskModel, TaskAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(
            oldItem: TaskModel,
            newItem: TaskModel
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.status == oldItem.status
        }

        override fun areContentsTheSame(
            oldItem: TaskModel,
            newItem: TaskModel
        ): Boolean {
            return oldItem.status == newItem.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskModel) {


            binding.txtTask.text = "${item.date} ${item.name}"



        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }

}
