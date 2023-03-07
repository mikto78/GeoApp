package com.example.geoapp.ui.map

import android.icu.text.StringSearch
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.geoapp.R
import org.koin.androidx.viewmodel.ext.android.viewModel


import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.MapView

class MapFragment : Fragment() {

    val mapView: MapView? = null

    private val viewModel: MapViewModel by viewModel()


    /// tutaj podmienic fragment_login na xml'a, jego stworzyć (map_fragment),
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_map, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel.locateUser()
        setApiKeyForApp()
        setupMap()



    }
    private fun setupMap() {

        // create a map with the BasemapStyle streets
        val map = ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)

        // set the map to be displayed in the layout's MapView
        val mapView = view?.findViewById<MapView>(R.id.mapView)
        mapView?.map = map

        // set the viewpoint, Viewpoint(latitude, longitude, scale)
        mapView?.setViewpoint(Viewpoint(52.2205593, 21.0101898, 5000.0))

    }

    override fun onPause() {
        mapView?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.resume()
    }

    override fun onDestroy() {
        mapView?.dispose()
        super.onDestroy()
    }

    private fun setApiKeyForApp() {
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISRuntimeEnvironment.setApiKey(getString(R.string.api))

    }
}