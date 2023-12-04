package com.example.pizzatracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pizzatracker.databinding.FragmentPizzaTrackerBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline

class PizzaTrackerFragment : Fragment() {

    private lateinit var binding: FragmentPizzaTrackerBinding
    private var startPoint: GeoPoint? = null
    private var endPoint: GeoPoint? = null
    private val mapZoomLevel = 12.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPizzaTrackerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }
    //1st commit

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().userAgentValue = activity?.packageName

        val mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(mapZoomLevel)
        mapView.controller.setCenter(GeoPoint(55.7558, 37.6176)) // Москва

        val markerOverlay = ItemizedIconOverlay<OverlayItem>(ArrayList(), null, requireContext())
        mapView.overlays.add(markerOverlay)

        // Пример геолок
        markerOverlay.addItem(createMarker(GeoPoint(55.7558, 37.6176), "Москва"))

        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val point = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt())
                if (startPoint == null) {
                    startPoint = point as GeoPoint
                    markerOverlay.addItem(createMarker(startPoint!!, "Начало"))
                } else if (endPoint == null) {
                    endPoint = point as GeoPoint
                    markerOverlay.addItem(createMarker(endPoint!!, "Конец"))
                    connectPoints(mapView, startPoint!!, endPoint!!)
                }
                return@setOnTouchListener true
            }
            false
        }

        // Маршрут, очистка маркеров
        binding.btnRoute.setOnClickListener {
            startPoint = null
            endPoint = null
            clearMarkersAndLines(mapView, markerOverlay)
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
    }

    private fun createMarker(geoPoint: GeoPoint, title: String): OverlayItem {
        val marker = OverlayItem(title, "", geoPoint)
        val markerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.marker_icon)
        marker.setMarker(markerDrawable)
        return marker
    }

    private fun connectPoints(mapView: org.osmdroid.views.MapView, start: GeoPoint, end: GeoPoint) {
        val line = Polyline()
        line.addPoint(start)
        line.addPoint(end)
        line.color = android.graphics.Color.BLUE
        mapView.overlays.add(line)
    }

    private fun clearMarkersAndLines(mapView: org.osmdroid.views.MapView, markerOverlay: ItemizedIconOverlay<OverlayItem>) {
        markerOverlay.removeAllItems()
        mapView.overlays.clear()
        mapView.invalidate()
    }
}





