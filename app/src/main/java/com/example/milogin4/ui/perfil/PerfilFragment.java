package com.example.milogin4.ui.perfil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.milogin4.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment{

    private FragmentPerfilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView Usuario = binding.textViewUsuario;
        final TextView Correo = binding.textViewCorreo;
        final TextView Fecha = binding.textViewFecha;
        final TextView Nacionalidad = binding.textViewNacionalidad;
        final TextView Opcion = binding.opcionAdmin;
        final Button Admin =binding.btnAdmin;
        final Button Editar= binding.btnEditar;
        final Button Eliminar= binding.btnEliminar;

        SharedPreferences preferences = getActivity().getSharedPreferences("sesiones", 0);
        String usuario = preferences.getString("usuario", "");
        String correo = preferences.getString("correo", "");
        String fecha = preferences.getString("fecha", "");
        String pais = preferences.getString("pais", "");

        Usuario.setText(usuario);
        Correo.setText(correo);
        Fecha.setText(fecha);
        Nacionalidad.setText(pais);

        String Nivel = preferences.getString("nivel", "normi");
        if (Nivel.equals("admin")){
            Opcion.setVisibility(View.VISIBLE);
            Admin.setVisibility(View.VISIBLE);
            Editar.setVisibility(View.VISIBLE);
            Eliminar.setVisibility(View.VISIBLE);
        }else{
            Opcion.setVisibility(View.INVISIBLE);
            Admin.setVisibility(View.INVISIBLE);
            Editar.setVisibility(View.INVISIBLE);
            Eliminar.setVisibility(View.INVISIBLE);
        }

        return root;
    }


}
