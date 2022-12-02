package com.example.milogin4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView Registro;
    Button Loginbtn;
    EditText Correo, Contraseña;
    private CheckBox Recordar;
    private boolean RadioButtonActivate;
    //shared
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        if(revisarSesion()){
            preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
            editor = preferences.edit();
            String correo = preferences.getString("correo","");
            String contraseña = preferences.getString("contraseña","");
            login(correo, contraseña);

        }

        Correo = findViewById(R.id.correo);
        Contraseña = findViewById(R.id.contraseña);
        Registro = findViewById(R.id.registro);
        Loginbtn = findViewById(R.id.loginbtn);
        Recordar = findViewById(R.id.recordar);

        //shared
        preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();


       /* RadioButtonActivate = Recordar.isChecked();
        Recordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RadioButtonActivate){
                    Recordar.setChecked(false);
                }
                RadioButtonActivate = Recordar.isChecked();
            }
        });*/

        Registro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
            }
        });

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String correo = Correo.getText().toString();
                String contraseña = Contraseña.getText().toString();

                if (!correo.isEmpty()) {
                    if (!contraseña.isEmpty()) {
                        login(correo, contraseña);

                    } else {
                        Toast toast = Toast.makeText(MainActivity.this, "Ingrese una contraseña", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "Ingrese un correo", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void guardarSesion(String usuario, String correo, String contraseña, String fecha, String pais, String nivel) {
        preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        boolean sesion1;

        if (Recordar.isChecked()) {
            sesion1 = true;
            editor.putBoolean("sesion", sesion1);
            editor.commit();
        } else {
            sesion1 = false;
        }

         editor.putString("usuario", usuario);
         editor.putString("correo", correo);
         editor.putString("contraseña", contraseña);
         editor.putString("fecha", fecha);
         editor.putString("pais", pais);
         editor.putString("nivel", nivel);

          editor.apply();

    }


    private void login(String correo, String contraseña) {

        String url = "https://www.carolinabr.tk/app/index.php";
        StringRequest postResquest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String value = jsonObject.getString("value");
                    if (value.equals("TRUE")) {

                        //String mensaje = jsonObject.getString("mensaje");
                        //String id_usuario = jsonObject.getString("id_usuario");
                        String usuario = jsonObject.getString("usuario");
                        String correo = jsonObject.getString("correo");
                        String contraseña = jsonObject.getString("contraseña");
                        String fecha = jsonObject.getString("fecha");
                        String pais = jsonObject.getString("pais");
                        String nivel = jsonObject.getString("nivel");

                 /*       SharedPreferences sharedPreferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //shared

                        editor.putString("usuario", usuario);
                        editor.putString("correo", correo);
                        editor.putString("fecha", fecha);
                        editor.putString("pais", pais);
                        editor.putString("nivel", nivel);

                        editor.apply();

                        guardarSesion(usuario, correo, fecha, pais, nivel);
                        startActivity(new Intent(MainActivity.this, menu_slide.class));
                        System.out.println("_____________________________________");
                        System.out.println(sharedPreferences.getAll());
                        System.out.println("________________________________________");*/
                        guardarSesion(usuario, correo, contraseña, fecha, pais, nivel);
                        startActivity(new Intent(MainActivity.this, menu_slide.class));
                    } else {
                        String mensaje = jsonObject.getString("mensaje");
                        Toast toast = Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                params.put("contraseña", contraseña);
                params.put("request", "login");
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(postResquest);
    }
    private boolean revisarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        boolean ssn = sharedPreferences.getBoolean("sesion", false);
        return ssn;
    }
}

