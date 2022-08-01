package com.vcloset.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.vcloset.R
import com.vcloset.data.clothes_data.ClothingItem
import com.vcloset.data.clothes_data.OutfitItem
import com.vcloset.data.clothes_data.OutfitsViewModel
import com.vcloset.databinding.FragmentSaveOutfitBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveOutfitFragment : Fragment() {
    private var _binding: FragmentSaveOutfitBinding? = null
    private val binding get() = _binding!!
    private lateinit var shirtItem: ClothingItem
    private lateinit var pantsItem: ClothingItem
    private val outfitsViewModel: OutfitsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shirtItem = it.get("selectedShirt") as ClothingItem
            pantsItem = it.get("selectedPants") as ClothingItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveOutfitBinding.inflate(inflater, container, false)


        binding.shirtTV.text = shirtItem.description
        binding.pantsTV.text = pantsItem.description
        if(shirtItem.image != null){
            Glide.with(requireActivity()).load(shirtItem.image!!.toUri()).into(binding.shirtPlaceHolder)
            binding.shirtPlaceHolder.adjustViewBounds = true
            binding.shirtPlaceHolder.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        if(pantsItem.image != null){
            Glide.with(requireActivity()).load(pantsItem.image!!.toUri()).into(binding.pantsPlaceHolder)
            binding.pantsPlaceHolder.adjustViewBounds = true
            binding.pantsPlaceHolder.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        binding.saveButton.setOnClickListener {
            val newOutfit = OutfitItem(0, shirtItem.description, pantsItem.description, shirtItem.image, pantsItem.image, shirtItem.coldOrWarm)
            outfitsViewModel.addItem(newOutfit)
            findNavController().navigate(R.id.action_saveOutfitFragment_to_outfitsListFragment)
        }

        return binding.root
    }
}