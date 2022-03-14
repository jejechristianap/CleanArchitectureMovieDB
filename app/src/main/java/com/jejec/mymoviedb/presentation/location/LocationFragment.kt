package com.jejec.mymoviedb.presentation.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.databinding.FragmentLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


@AndroidEntryPoint
class LocationFragment : Fragment() {

    private lateinit var bind: FragmentLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentLocationBinding.inflate(inflater, container, false)
        return bind.root
    }

    @SuppressLint("TimberArgCount")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        bind.map.setTileSource(TileSourceFactory.MAPNIK)
        bind.map.setBuiltInZoomControls(true)
        bind.map.setMultiTouchControls(true)

        val mapController = bind.map.controller
        mapController.setZoom(15)

        val locationOverlay = MyLocationNewOverlay(bind.map)
        locationOverlay.enableFollowLocation()
        locationOverlay.runOnFirstFix {
            val latitude = locationOverlay.lastFix.latitude
            val longitude = locationOverlay.lastFix.longitude
            bind.tvLatitude.text = requireContext().resources.getString(
                R.string.latitude,
                latitude.toString()
            )
            bind.tvLongitude.text = requireContext().resources.getString(
                R.string.longitude,
                longitude.toString()
            )

        }
        bind.map.overlayManager.add(locationOverlay)
    }

    override fun onResume() {
        super.onResume()
        bind.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        bind.map.onPause()
    }
}