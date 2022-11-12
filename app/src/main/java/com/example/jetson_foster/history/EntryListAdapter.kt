package com.example.jetson_foster.history

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jetson_foster.R
import com.example.jetson_foster.databinding.EntryListItemBinding
import com.example.jetson_foster.room.ActiveEntry

class EntryListAdapter(
    private val onItemClicked: (ActiveEntry) -> Unit,
    val context: Context
) : ListAdapter<ActiveEntry,EntryListAdapter.EntryListViewHolder>(DiffCallback) {

    private var prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val unitKey = context.resources.getString(R.string.unit_system)
    private var unit : String = prefs.getString(unitKey,"metric")!!

    inner class EntryListViewHolder(
        private var binding: EntryListItemBinding
        ): RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: ActiveEntry) {
            val titleText = "${entry.inputString()}: ${entry.activityString()}, ${entry.dateTimeString()}"
            val timeText = entry.durationString()
            val listener = SharedPreferences.OnSharedPreferenceChangeListener {p,key ->
                if (key == unitKey) {
                    unit = prefs.getString(key,"metric")!!
                    binding.entryListSubtitle.text = "${entry.distanceString(unit)} , $timeText"
                }
            }
            prefs.registerOnSharedPreferenceChangeListener(listener)
            val subtitleText = "${entry.distanceString(unit)} , $timeText"
            binding.entryListTitle.text = titleText
            binding.entryListSubtitle.text = subtitleText
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ActiveEntry>() {
            override fun areItemsTheSame(oldItem: ActiveEntry, newItem: ActiveEntry): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ActiveEntry, newItem: ActiveEntry): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryListViewHolder {

        val viewHolder = EntryListViewHolder(
            EntryListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EntryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}