package pe.edu.idat.appnavigationdrawermaps.ui

import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import pe.edu.idat.appnavigationdrawermaps.R

class MapsFragment : Fragment(),  GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {


    private lateinit var mMap: GoogleMap

    private var lstLatLong = ArrayList<LatLng>()


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerDragListener(this)
        //val sydney = LatLng(-34.0, 151.0)
        //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        // Add a marker in Sydney and move the camera
        val puntoReferencia = LatLng(-12.074566, -77.036039)
        mMap.addMarker(
            MarkerOptions()
                .position(puntoReferencia)
                .title("Punto de Referencia")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.carrofamiliar))
                .draggable(true)
                .snippet("Ahora mismo me encuentro aquí"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoReferencia,
            16.0F))
        //mMap.isTrafficEnabled = true
        //mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        agregarPoligono()
    }
    private fun agregarPoligono(){
        val poligonoMap = mMap.addPolygon(
            PolygonOptions()
                .add(
                    LatLng(-12.092776,-76.995812),
                    LatLng(-12.089579,-77.001294),
                    LatLng(-12.088362,-77.003419),
                    LatLng(-12.093985,-77.002711)
                )
        )
        poligonoMap.tag = "QBO"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMapClick(p0: LatLng) {
        mMap.addMarker(
            MarkerOptions()
                .position(p0)
                .title("Nuevo marcador")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLng(p0))
        lstLatLong.add(p0)
        val polyLinea = PolylineOptions()
        polyLinea.color(Color.RED)
        polyLinea.width(6F)
        polyLinea.addAll(lstLatLong)
        mMap.addPolyline(polyLinea)
    }

    override fun onMarkerDrag(p0: Marker) {
        var posicion = p0.position
        p0.snippet = posicion.latitude.toString()+" - "+
                posicion.longitude.toString()
        p0.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLng(posicion))
    }

    override fun onMarkerDragEnd(p0: Marker) {
        p0.title = "Nueva posición"
        p0.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLng(p0.position))
    }

    override fun onMarkerDragStart(p0: Marker) {
        p0.showInfoWindow()
    }
}