package com.vcloset.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vcloset.R
import com.vcloset.data.clothes_data.ClothingItemsViewModel
import com.vcloset.data.Constants
import com.vcloset.data.weather_service.WeatherViewModel
import com.vcloset.databinding.FragmentItemOptionsBinding
import com.vcloset.ui.adapters.ClothingItemsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemOptionsFragment : Fragment() {

    private var _binding: FragmentItemOptionsBinding? = null
    private val binding get() = _binding!!
    private val clothingItemsViewModel: ClothingItemsViewModel by activityViewModels()
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var shirtsAdapter: ClothingItemsAdapter
    private lateinit var pantsAdapter: ClothingItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemOptionsBinding.inflate(inflater, container, false)
        binding.addClo.setOnClickListener {
            findNavController().navigate(R.id.action_itemOptionsFragment_to_addClothingItemFragment)
        }
        binding.checkFit.setOnClickListener {
            val selectedShirt = shirtsAdapter.getSelectedItem()
            val selectedPants = pantsAdapter.getSelectedItem()

            if (selectedPants != null && selectedShirt != null) {
                val selectedItems =
                    bundleOf("selectedShirt" to selectedShirt, "selectedPants" to selectedPants)
                findNavController().navigate(
                    R.id.action_itemOptionsFragment_to_saveOutfitFragment,
                    selectedItems
                )
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.pantsOrShirtNotSelected),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shirtItemList.layoutManager = LinearLayoutManager(requireContext())
        binding.pantsItemList.layoutManager = LinearLayoutManager(requireContext())

        shirtsAdapter = ClothingItemsAdapter(requireContext())
        pantsAdapter = ClothingItemsAdapter(requireContext())

        binding.shirtItemList.adapter = shirtsAdapter
        binding.pantsItemList.adapter = pantsAdapter

        val weather = weatherViewModel.getColdOrWarm()

        if (weather == Constants.NO_WEATHER) {
            clothingItemsViewModel.getAllShirts().observe(viewLifecycleOwner) {
                shirtsAdapter.setItems(it)
            }
            clothingItemsViewModel.getAllPants().observe(viewLifecycleOwner) {
                pantsAdapter.setItems(it)
            }
        }
        else {
            clothingItemsViewModel.getShirts(weather).observe(viewLifecycleOwner) {
                shirtsAdapter.setItems(it)
            }

            clothingItemsViewModel.getPants(weather).observe(viewLifecycleOwner) {
                pantsAdapter.setItems(it)
            }
        }
    }

    override fun onContextItemSelected(menuItem: MenuItem): Boolean {
        lateinit var adapter: ClothingItemsAdapter
        if (menuItem.itemId == R.id.shirts_remove_option) {
            adapter = shirtsAdapter
        }
        else if (menuItem.itemId == R.id.pants_remove_option) {
            adapter = pantsAdapter
        }
        val itemToRemove = adapter.getItemByPosition(menuItem.groupId)
        clothingItemsViewModel.removeItem(itemToRemove)

        return super.onContextItemSelected(menuItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}