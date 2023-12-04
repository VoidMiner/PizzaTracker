package com.example.pizzatracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pizzatracker.databinding.FragmentPizzaTrackerBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.tileprovider.tilesource.*

class PizzaTrackerFragment : Fragment() {

    private lateinit var binding: FragmentPizzaTrackerBinding
    private var startPoint: GeoPoint? = null
    private var endPoint: GeoPoint? = null
    private val mapZoomLevel = 12.0
    private val markerOverlay: ItemizedIconOverlay<OverlayItem> by lazy {
        ItemizedIconOverlay(ArrayList(), null, requireContext())
    }
    private var lineOverlay: Polyline = Polyline()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MenuFragment", "onCreateView")

        binding = FragmentPizzaTrackerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentContainer = view.findViewById<View>(R.id.fragmentContainer)
        Log.d("PizzaTrackerFragment", "Fragment container: $fragmentContainer")
        Log.d("PizzaTrackerFragment", "Child Fragment Manager: ${childFragmentManager.findFragmentById(R.id.fragmentContainer)}")




        Configuration.getInstance().userAgentValue = activity?.packageName

        val mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(mapZoomLevel)
        mapView.controller.setCenter(GeoPoint(55.7558, 37.6176)) // Москва

        mapView.overlays.add(markerOverlay)

        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val point = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt())
                if (startPoint == null) {
                    startPoint = point as GeoPoint
                    markerOverlay.addItem(createMarker(startPoint!!, "Начало"))
                } else if (endPoint == null) {
                    endPoint = point as GeoPoint
                    markerOverlay.addItem(createMarker(endPoint!!, "Конец"))
                }
                return@setOnTouchListener true
            }
            false
        }

        // Маршрут
        binding.btnRoute.setOnClickListener {
            if (startPoint != null && endPoint != null) {
                connectPoints(mapView, startPoint!!, endPoint!!)
            }
        }

        // Центрировать
        binding.btnCenter.setOnClickListener {
            if (startPoint != null) {
                mapView.controller.animateTo(startPoint!!)
            }
        }

        // Фиксировать
        binding.btnFix.setOnClickListener {
            if (startPoint != null) {
                mapView.controller.setCenter(startPoint!!)
            }
        }

        // Сброс
        binding.btnReset.setOnClickListener {
            resetMarkersAndLines()
        }
        val btnZoomIn: ImageButton = view.findViewById(R.id.btnZoomIn)
        val btnZoomOut: ImageButton = view.findViewById(R.id.btnZoomOut)

        btnZoomIn.setOnClickListener {
            mapView.controller.zoomIn()
        }
        btnZoomOut.setOnClickListener {
            mapView.controller.zoomOut()
        }
        //кнопка меню
        val btnMenu: Button = view.findViewById(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val menuFragment = MenuFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, menuFragment)
                .addToBackStack(null)
                .commit()
        }

    }


    private fun createMarker(geoPoint: GeoPoint, title: String): OverlayItem {
        val marker = OverlayItem(title, "", geoPoint)
        val markerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.marker_icon)
        marker.setMarker(markerDrawable)
        return marker
    }

    private fun connectPoints(mapView: org.osmdroid.views.MapView, start: GeoPoint, end: GeoPoint) {
        mapView.overlays.remove(lineOverlay)

        lineOverlay = Polyline()
        lineOverlay.addPoint(start)
        lineOverlay.addPoint(end)
        lineOverlay.color = android.graphics.Color.BLUE
        mapView.overlays.add(lineOverlay)

        mapView.invalidate()
    }


    private fun resetMarkersAndLines() {
        startPoint = null
        endPoint = null

        markerOverlay.removeAllItems()
        binding.mapView.overlays.remove(lineOverlay)

        binding.mapView.invalidate()
    }

}