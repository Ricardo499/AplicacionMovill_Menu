package com.example.milogin4.ui.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.milogin4.R;
import com.example.milogin4.databinding.FragmentGalleryBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSIONS_REQUEST;

    //Añadida
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //Pide permisos de ubicación
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                //return;
            }

            //Enfoca el mapa en la ubicación actual
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location != null) {
                    LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();

                    Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.punto_ubicacion);
//                    Bitmap smallimg = Bitmap.createScaledBitmap(img, 40, 65, false);
                    Bitmap smallimg = Bitmap.createScaledBitmap(img, 50, 82, false);

                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(smallimg);

                    markerOptions.position(lng);
                    markerOptions.icon(bitmapDescriptor);
                    markerOptions.title("Empresa Nasa Juvenil");

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            lng, 18
                    ));

                    googleMap.addMarker(markerOptions);
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 17.0f));

                }
            });
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //Añadida
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        }
    }
}
