package com.dicoding.com.storyapp.ui.view.fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.com.storyapp.R
import com.dicoding.com.storyapp.data.ResultState
import com.dicoding.com.storyapp.data.model.StoryModel
import com.dicoding.com.storyapp.databinding.FragmentMapsBinding
import com.dicoding.com.storyapp.ui.ViewModelFactory
import com.dicoding.com.storyapp.ui.viewmodel.MapsViewModel
import com.dicoding.com.storyapp.util.NetworkUtils
import com.dicoding.com.storyapp.util.showLoading
import com.dicoding.com.storyapp.util.showToast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var callback: OnMapReadyCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.no_internet)
                .setMessage(R.string.no_internet_description)
                .setPositiveButton(R.string.ok) { _, _ ->
                    requireActivity().finish()
                }
                .show()
        } else {
            setupMap()
        }
    }

    private fun setupMap() {
        callback = OnMapReadyCallback { googleMap ->
            setupGoogleMap(googleMap)
            observeStoryWithLocation(googleMap)
            setMapStyle(googleMap)
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setupGoogleMap(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
    }

    private fun observeStoryWithLocation(googleMap: GoogleMap) {
        viewModel.listStoryWithLocation.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.showLoading(true)
                }

                is ResultState.Success -> {
                    binding.progressIndicator.showLoading(false)
                    addMarkersToMap(googleMap, result.data)
                }

                is ResultState.Error -> {
                    requireContext().showToast(result.error)
                    binding.progressIndicator.showLoading(false)
                }

                is ResultState.Empty -> {
                    requireContext().showToast(R.string.result_empty.toString())
                    binding.progressIndicator.showLoading(false)
                }
            }
        }
    }

    private fun addMarkersToMap(googleMap: GoogleMap, stories: List<StoryModel>) {
        stories.forEach { story ->
            val latLng = story.lat?.let { story.lon?.let { it1 -> LatLng(it, it1) } }
            latLng?.let {
                MarkerOptions()
                    .position(it)
                    .title(story.name)
                    .snippet(story.description)
            }?.let {
                googleMap.addMarker(it)
            }
        }
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsFragment"
    }

}