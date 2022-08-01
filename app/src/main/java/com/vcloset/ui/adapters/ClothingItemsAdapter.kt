package com.vcloset.ui.adapters

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vcloset.R
import com.vcloset.data.clothes_data.ClothingItem
import com.vcloset.data.Constants
import com.vcloset.databinding.ClothingItemLayoutBinding
import com.bumptech.glide.Glide

class ClothingItemsAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<ClothingItemsAdapter.ClothingItemsViewHolder>() {

    private var selectedPos = RecyclerView.NO_POSITION
    private val items = ArrayList<ClothingItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ClothingItemsViewHolder(
        ClothingItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ClothingItemsViewHolder,
        position: Int
    ) {
        holder.bind(items[position])

        holder.itemView.background.setTint(
            if (selectedPos == position) context.resources.getColor(
                androidx.appcompat.R.color.material_blue_grey_800
            )
            else context.resources.getColor(R.color.skyBlue)
        )
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<ClothingItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItemByPosition(position: Int): ClothingItem {
        return items[position]
    }


    fun getSelectedItem(): ClothingItem? {
        return if (selectedPos != RecyclerView.NO_POSITION) items[selectedPos] else null
    }

    private fun getItemsType(): String {
        return items[0].itemType!!
    }

    inner class ClothingItemsViewHolder(private val binding: ClothingItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        View.OnCreateContextMenuListener {
        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnCreateContextMenuListener(this)
        }

        override fun onClick(p0: View?) {
            notifyItemChanged(selectedPos)
            selectedPos =
                if (selectedPos != layoutPosition) layoutPosition else RecyclerView.NO_POSITION
            notifyItemChanged(selectedPos)
        }

        fun bind(item: ClothingItem) {
            binding.description.text = item.description
            binding.itemType.text = item.itemType.toString()
            binding.coldOrWarm.text = item.coldOrWarm.toString()
            if (item.image != null) {
                Glide.with(context).load(item.image).into(binding.itemImage)
                binding.itemImage.adjustViewBounds = true
                binding.itemImage.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(
                this.absoluteAdapterPosition,
                if (getItemsType() == Constants.SHIRT) R.id.shirts_remove_option else R.id.pants_remove_option,
                0,
                context.resources.getString(R.string.remove_item)
            )
        }


    }
}

