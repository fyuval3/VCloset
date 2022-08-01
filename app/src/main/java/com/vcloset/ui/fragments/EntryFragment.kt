package com.vcloset.ui.fragments

import android.Manifest
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.car.ui.AlertDialogBuilder
import com.vcloset.R
import com.vcloset.data.*
import com.vcloset.data.weather_service.*
import com.vcloset.databinding.FragmentEntryBinding
import com.bumptech.glide.Glide


class EntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        binding.clothesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_entryFragment_to_itemOptionsFragment)
        }
        binding.outfitsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_entryFragment_to_outfitsListFragment)
        }

        weatherViewModel.weather.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                is Success -> {
                    binding.temperatureTV.text =
                        resource.status.data!!.main.temp.toInt().toString() + "°"
                    resource.status.data!!.weather[0].icon.let {
                        Glide.with(requireActivity()).load(getWeatherIconURI(it))
                            .into(binding.temperatureIMG)
                    }
                    enableButtons()
                }

                is Error -> {
                    binding.temperatureTV.text = getString(R.string.tempUnknown)
                    Glide.with(requireActivity()).load(R.drawable.ic_launcher_foreground).into(binding.temperatureIMG)
                    Toast.makeText(
                        requireActivity(),
                        resource.status.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    enableButtons()
                }
            }
        }

        if (permissionsAreGranted()) {
            fetchWeather()
        } else {
            askForPermissions()
        }

        return binding.root
    }

    private fun fetchWeather() {
        displayLoading()
        weatherViewModel.fetchWeather(requireActivity())
    }

    private fun displayLoading() {
        var imageUri: Uri? = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + requireContext().resources.getResourcePackageName(R.drawable.loading)
                    + '/' + requireContext().resources.getResourceTypeName(R.drawable.loading)
                    + '/' + requireContext().resources.getResourceEntryName(R.drawable.loading)
        )
        Glide.with(requireActivity()).load(imageUri).into(binding.temperatureIMG);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun enableButtons() {
        binding.clothesBtn.isEnabled = true
        binding.outfitsBtn.isEnabled = true
    }

    private fun getWeatherIconURI(iconID: String): Uri {
        val url = Constants.ICONS_BASE_URL.replace("[IconID]", iconID)
        return Uri.parse(url)
    }

    private fun permissionsAreGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                )
    }

    private fun askForPermissions() {
        val requestLocationPermissionLauncher = getLocationPermissionLauncher()
        val locationPermissionAlertBuilder = AlertDialogBuilder(requireActivity())
        locationPermissionAlertBuilder.setTitle(R.string.locationPermissionRequiredTitle)
        locationPermissionAlertBuilder.setMessage(R.string.locationPermissionRequiredParagraph)
        locationPermissionAlertBuilder.setPositiveButton(
            getString(R.string.Yes)
        ) { _: DialogInterface, _: Int ->
            val permissionsToRequest = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            requestLocationPermissionLauncher.launch(permissionsToRequest)
        }
        locationPermissionAlertBuilder.setNegativeButton(
            getString(R.string.No)
        ) { _: DialogInterface, _: Int -> }
        val locationPermissionAlert = locationPermissionAlertBuilder.create()
        locationPermissionAlert.show()
    }

    // Creating and returning the Request-Location-Permission launcher
    private fun getLocationPermissionLauncher(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map: Map<String, Boolean> ->
            if (map[Manifest.permission.ACCESS_COARSE_LOCATION] == false || map[Manifest.permission.ACCESS_FINE_LOCATION] == false) {
                val permissionDeniedAlertBuilder = AlertDialogBuilder(requireActivity())
                permissionDeniedAlertBuilder.setTitle(R.string.locationPermissionRequiredTitle)
                permissionDeniedAlertBuilder.setMessage(R.string.locationPermissionDeniedParagraph)
                permissionDeniedAlertBuilder.setPositiveButton(
                    getString(R.string.OK)
                ) { _: DialogInterface, _: Int -> }
                val permissionDeniedAlert = permissionDeniedAlertBuilder.create()
                permissionDeniedAlert.show()
            } else {
                fetchWeather()
            }
        }
    }
}