package com.scheffsblend.emoconnect.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.clj.fastble.data.BleDevice
import com.scheffsblend.emoconnect.R

class EmosAdapter(private val onClick: (BleDevice) -> Unit) :
    ListAdapter<BleDevice, EmosAdapter.EmosViewHolder>(EmosDiffCallback) {
    /* ViewHolder for EMO, takes in the inflated view and the onClick behavior. */
    class EmosViewHolder(itemView: View, val onClick: (BleDevice) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val emoTextView: TextView = itemView.findViewById(R.id.emo_text)
        private var currentEmo: BleDevice? = null

        init {
            itemView.setOnClickListener {
                currentEmo?.let {
                    onClick(it)
                }
            }
        }

        /* Bind emo data. */
        fun bind(emo: BleDevice) {
            currentEmo = emo

            emoTextView.text = emo.name
        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emo_item, parent, false)
        return EmosViewHolder(view, onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: EmosViewHolder, position: Int) {
        val emo = getItem(position)
        holder.bind(emo)
    }
}

object EmosDiffCallback : DiffUtil.ItemCallback<BleDevice>() {
    override fun areItemsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean {
        return oldItem.device.address == newItem.device.address
    }
}
