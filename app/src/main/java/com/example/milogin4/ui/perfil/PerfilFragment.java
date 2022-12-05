package com.example.milogin4.ui.perfil;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.USER_SERVICE;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import java.io.File;


import com.example.milogin4.MainActivity;
import com.example.milogin4.databinding.FragmentPerfilBinding;


import com.example.milogin4.R;
import com.example.milogin4.dbStopJumper;
import com.example.milogin4.menu_slide;

import android.content.Intent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PerfilFragment extends Fragment  {

    private static final int REQUEST_IMAGE_GET = 1;
    private static final int SELECT_IMAGE = 100;

    private FragmentPerfilBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Uri crashsound, uri;

    ImageButton Foto_Usuario;
    Boolean flag = true;
    Boolean hasPicture = false;
    String USER;
    dbStopJumper db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView Usuario = binding.textViewUsuario;
        final TextView Correo = binding.textViewCorreo;
        final TextView Fecha = binding.textViewFecha;
        final TextView Nacionalidad = binding.textViewNacionalidad;
        final TextView Tipo = binding.textViewNivel;
        final TextView Opcion = binding.opcionAdmin;
        final Button Admin =binding.btnAdmin;
        final Button Editar= binding.btnEditar;
        final Button Eliminar= binding.btnEliminar;


        SharedPreferences preferences = getActivity().getSharedPreferences("sesiones", 0);
        //SharedPreferences preferences = getSharedPreferences("sesiones", 0);
        String usuario = preferences.getString("usuario", "");
        String correo = preferences.getString("correo", "");
        String fecha = preferences.getString("fecha", "");
        String pais = preferences.getString("pais", "");
        String tipo = preferences.getString("nivel","");

        Usuario.setText(usuario);
        Correo.setText(correo);
        Fecha.setText(fecha);
        Nacionalidad.setText(pais);
        Tipo.setText(tipo);

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


        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("sesion", false);
                editor.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


       ImageButton Foto_Usuario = binding.fotousuario;
       Foto_Usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch("Selecciona un archivo a subir");
            }
        });
        return root;



    }





    private void performFileSearch(String messageTitle) {
        //uri = Uri.parse("content://com.android.externalstorage.documents/document/home");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        intent.setType("*/*");
        ((Activity) getActivity()).startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code OPEN_DIRECTORY_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                // The document selected by the user won't be returned in the intent.
                // Instead, a URI to that document will be contained in the return intent
                // provided to this method as a parameter.  Pull that uri using "resultData.getData()"
                if (resultData != null && resultData.getData() != null) {
                    uri = resultData.getData();
                    System.out.println(uri.toString());
                    pictureUpdate();
                    //Toast.makeText(getApplicationContext(),fileuri.toString(), Toast.LENGTH_SHORT).show();
                    //System.out.println("File selected successfully");
                    //System.out.println("content://com.android.externalstorage.documents"+file.getPath());
                } else {

                }
            } else {
                //System.out.println("User cancelled file browsing {}");
            }
        }
    }

    public void pictureUpdate() {
        File file =  new File(getActivity().getFilesDir(),"profile");
        try {
            InputStream in = getActivity().getContentResolver().openInputStream(uri);
            FileOutputStream out = new FileOutputStream(file, false);
            byte[] buffer = new byte[1024];
            for (int len; (len = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            Toast.makeText(getActivity().getApplicationContext(),"Imagen de perfil actualizada", Toast.LENGTH_SHORT).show();
            pictureLoad();
        } catch (Exception e) {
            System.out.println(e);
            Toast.makeText(getActivity().getApplicationContext(),"No se pudo actualizar la imagen de perfil", Toast.LENGTH_SHORT).show();
        }
    }

    public void pictureLoad() {
        Thread imgread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    File file =  new File(getActivity().getFilesDir(),"profile");
                    Drawable thumb_d = Drawable.createFromPath(file.toString());

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(file.exists()) {
                                Foto_Usuario.setImageDrawable(thumb_d);
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                   getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });
        imgread.start();
    }
}
