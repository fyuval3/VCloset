package com.vcloset.ui.adapters

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vcloset.R
import com.vcloset.data.clothes_data.OutfitItem
import com.vcloset.databinding.OutfitItemLayoutBinding
import com.bumptech.glide.Glide

class OutfitsAdapter(private val context: Context) :
    RecyclerView.Adapter<OutfitsAdapter.OutfitsViewHolder>() {

    private val items = ArrayList<OutfitItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = OutfitsViewHolder(
        OutfitItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: OutfitsViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<OutfitItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItemByPosition(position: Int): OutfitItem {
        return items[position]
    }

    inner class OutfitsViewHolder(private val binding: OutfitItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        init {
            binding.root.setOnCreateContextMenuListener(this)
        }

        fun bind(item: OutfitItem) {

            binding.descriptionShirt.text = item.shirtDescription
            binding.descriptionPants.text = item.pantsDescription
            binding.coldOrWarmPants.text = item.coldOrWarm
            binding.coldOrWarmShirt.text = item.coldOrWarm
            binding.itemTypePants.text = context.getString(R.string.pants)
            binding.itemTypeShirt.text = context.getString(R.string.shirts)
            if(item.shirtImage != null){
                Glide.with(context).load(item.shirtImage).into(binding.itemImageShirt)
                binding.itemImageShirt.adjustViewBounds = true
                binding.itemImageShirt.scaleType = ImageView.ScaleType.FIT_CENTER
            }
            if(item.pantsImage != null){
                Glide.with(context).load(item.pantsImage).into(binding.itemImagePants)
                binding.itemImagePants.adjustViewBounds = true
                binding.itemImagePants.scaleType = ImageView.ScaleType.FIT_CENTER

            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(
                this.absoluteAdapterPosition,
                R.id.outfits_remove_option,
                0,
                context.resources.getString(R.string.remove_outfit)
            )
        }
    }
}

