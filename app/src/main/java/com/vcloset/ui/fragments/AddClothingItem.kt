package com.vcloset.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vcloset.databinding.FragmentAddClothingItemBinding
import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.vcloset.R
import com.vcloset.data.clothes_data.ClothingItem
import com.vcloset.data.Constants
import com.vcloset.data.clothes_data.ClothingItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddClothingItem : Fragment() {
    private var _binding: FragmentAddClothingItemBinding? = null
    private val binding get() = _binding!!
    private val imagePickCode = 1000
    private val imageCaptureCode = 1001
    private val clothingItemsViewModel: ClothingItemsViewModel by activityViewModels()

    companion object {
        private var itemDescription = ""
        private var errorDescription = ""
        private var itemType = ""
        private var coldOrWarm = ""
        private var imageSource: Uri? = null
    }

    private val pickImageFromGallery =
        registerForActivityResult((ActivityResultContracts.OpenDocument())) { uri: Uri? ->
            uri?.let {
                activity?.contentResolver?.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                binding.itemImg.setImageURI(it)
                imageSource = it
            }
        }

    private fun pickImageFromCamera() {
        val values = ContentValues()

        values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
        values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
        imageSource =
            activity?.contentResolver?.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )!!
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageSource)
        startActivityForResult(intent, imageCaptureCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == imagePickCode)
                imageSource = data?.data!!
            binding.itemImg.setImageURI(imageSource)
        }
    }

    private fun inputIsValid(): Boolean {
        itemDescription = binding.itemDescription.text.toString()
        if (binding.warmRadio.isChecked == binding.coldRadio.isChecked) {
            errorDescription = getString(R.string.ColdWarmError)
            return false
        }
        if (binding.shirtRadio.isChecked == binding.pantsRadio.isChecked) {
            errorDescription = getString(R.string.ItemTypeError)
            return false
        }
        coldOrWarm = if (binding.warmRadio.isChecked) Constants.WARM
        else Constants.COLD

        itemType = if (binding.shirtRadio.isChecked) Constants.SHIRT
        else Constants.PANTS

        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddClothingItemBinding.inflate(inflater, container, false)
        binding.photoGallery.setOnClickListener { pickImageFromGallery.launch(arrayOf("image/*")) }
        binding.takeAPhoto.setOnClickListener { pickImageFromCamera() }
        binding.saveButton.setOnClickListener {
            if (inputIsValid()) {
                val itemToAdd =
                    ClothingItem(
                        0,
                        if (imageSource != null) imageSource.toString() else null,
                        itemDescription,
                        itemType,
                        coldOrWarm
                    )
                clothingItemsViewModel.addItem(itemToAdd)
                activity?.onBackPressed()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.Not_Save, errorDescription),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
