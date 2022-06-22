package com.inferno.mobile.bedon_waseet.ui.map


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.ItemCalloutViewBinding
import com.inferno.mobile.bedon_waseet.databinding.MapFragmentBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.LocationPermissionHelper
import com.inferno.mobile.bedon_waseet.utils.RealEstateType
import com.inferno.mobile.bedon_waseet.utils.getToken
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.Annotation
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.lang.ref.WeakReference


class MapFragment : Fragment() {

    private lateinit var binding: MapFragmentBinding
    private val viewModel: MapViewModel by viewModels()
    private val locationChange = MutableLiveData<Point>()
    private lateinit var pointAnnotationManager: PointAnnotationManager

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private var firstInput = true
    private var firstTime = true
    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        if (firstInput) {
            binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
            binding.mapView.gestures.focalPoint =
                binding.mapView.getMapboxMap().pixelForCoordinate(it)
        }
        firstInput = false
        locationChange.value = it
    }
    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
//            onCameraTrackingDismissed()
//            pointAnnotationManager.deleteAll()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {
//            val centerPoint = binding.mapView.getMapboxMap().cameraState.center
//            locationChange.value = centerPoint
            if (firstTime) {
                val centerPoint = locationChange.value
                if (centerPoint != null) {
                    loadRealEstateAroundPoint(centerPoint)
                }
                firstTime = false
            }
        }
    }

    private fun loadRealEstateAroundPoint(point: Point) {
        val token = getToken(requireContext())
        viewModel.getRealEstateInsideCircle(token, point.latitude(), point.longitude())
            .observe(requireActivity(), this::showEstateOnMap)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkLocationEnable()
        binding = MapFragmentBinding.inflate(inflater, container, false)

        locationPermissionHelper = LocationPermissionHelper(WeakReference(requireActivity()))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
        return binding.root
    }

    private fun onMapReady() {
        binding.mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        binding.mapView.getMapboxMap().loadStyleUri(
            Style.SATELLITE_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
            val annos = binding.mapView.annotations
            pointAnnotationManager = annos.createPointAnnotationManager()
        }

    }


    private fun createPoint(estate: RealEstate) {
        val point = Point.fromLngLat(estate.lng!!.toDouble(), estate.lat!!.toDouble())
        val bitmap: Bitmap?
        when (estate.stateType) {
            RealEstateType.ارض ->
                bitmap = convertDrawableToBitmap(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_marker
                    )
                )
            RealEstateType.شقة ->
                bitmap = convertDrawableToBitmap(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_marker
                    )
                )
            else -> bitmap = convertDrawableToBitmap(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_marker
                )
            )
        }
        val circleAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap!!)
        pointAnnotationManager.create(circleAnnotationOptions)
        pointAnnotationManager.addClickListener {
            Toast.makeText(requireContext(), estate.owner.name, Toast.LENGTH_SHORT).show()
            val viewAnnotationManager = binding.mapView.viewAnnotationManager
            val viewAnnotation = viewAnnotationManager.addViewAnnotation(
                resId = R.layout.item_callout_view,
                options = viewAnnotationOptions {
                    geometry(point)
                })
            val _binding = ItemCalloutViewBinding.bind(viewAnnotation)
            _binding.textNativeView.text = estate.owner.name
            true
        }

    }

    private fun setupGesturesListener() {
        binding.mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = binding.mapView.location

        locationComponentPlugin.updateSettings {
            enabled = true
            locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@MapFragment.requireContext(),
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@MapFragment.requireContext(),
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )

    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(requireContext(), "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        binding.mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
//        binding.mapView.location
//            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        binding.mapView.gestures.removeOnMoveListener(onMoveListener)
    }


    override fun onDestroy() {
        super.onDestroy()
//        binding.mapView.location
//            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        binding.mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        binding.mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    private fun showEstateOnMap(estates: ArrayList<RealEstate>) {

        for (estate in estates) {

            createPoint(estate)
        }
    }


    private fun checkLocationEnable() {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (!gpsEnabled && !networkEnabled) {
            // notify user
            AlertDialog.Builder(context)
                .setMessage("Location is disabled in your device. Please enable it.")
                .setPositiveButton(
                    "Open Settings"
                ) { dialog, _ ->
                    requireContext().startActivity(
                        Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                    )
                    dialog.dismiss()
                }
                .setNegativeButton("Close", null)
                .show()
        }
    }


    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

}


