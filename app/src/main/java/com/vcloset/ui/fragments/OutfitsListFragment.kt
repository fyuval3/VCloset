package com.vcloset.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vcloset.data.clothes_data.OutfitsViewModel
import com.vcloset.databinding.FragmentOutfitsListBinding
import com.vcloset.ui.adapters.OutfitsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OutfitsListFragment : Fragment() {

    private var _binding: FragmentOutfitsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var outfitsAdapter: OutfitsAdapter
    private val outfitsViewModel: OutfitsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOutfitsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.outfitsRV.layoutManager = LinearLayoutManager(requireContext())

        outfitsAdapter = OutfitsAdapter(requireActivity())

        binding.outfitsRV.adapter = outfitsAdapter

        outfitsViewModel.getOutfits().observe(viewLifecycleOwner) {
            outfitsAdapter.setItems(it)
        }
    }

    override fun onContextItemSelected(menuItem: MenuItem): Boolean {
        val itemToRemove = outfitsAdapter.getItemByPosition(menuItem.groupId)
        outfitsViewModel.removeItem(itemToRemove)

        return super.onContextItemSelected(menuItem)
    }
}