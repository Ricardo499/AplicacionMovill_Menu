package com.example.milogin4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    EditText Usuario, Correo,
            Contraseña, Contraseña2;
    Button Fecha, Aceptar, Cancelar;
    private Spinner Pais;
    TextView inicio;

    int campos_correctos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        Usuario = findViewById(R.id.user);
        Correo = findViewById(R.id.Correo);
        Contraseña= findViewById(R.id.contraseña);
        Contraseña2 = findViewById(R.id.contraseña2);
        Fecha = findViewById(R.id.btnNacimiento);
        Pais = findViewById(R.id.Nacionalidad);
        Cancelar= findViewById(R.id.btnCancelar);
        Aceptar = findViewById(R.id.btnAceptar);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String[] paises = getResources().getStringArray(R.array.paises);
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, paises);
        Pais.setAdapter(adapter);

        //Mostrar calendario
        Fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog = new DatePickerDialog(Registro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = year+"-"+month+"-"+day;
                        Fecha.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //Ir al inicio
        Cancelar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Registro.this, MainActivity.class));
            }
        });

        //Registrar usuario
        Aceptar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //Obtenemos los datos del usuario
                String usuario = Usuario.getText().toString();
                String correo = Correo.getText().toString();
                String contraseña = Contraseña.getText().toString();
                String conf_contraseña = Contraseña2.getText().toString();
                String fecha = Fecha.getText().toString();
                //String paises = Pais.getText().toString();
                String paises = Pais.getSelectedItem().toString();

                sedimentación(usuario,correo,contraseña,conf_contraseña,fecha,paises);
                if (campos_correctos == 6) {
                    crear(usuario,correo,contraseña,fecha,paises);
                    startActivity(new Intent(Registro.this, MainActivity.class));
                }
            }
        });

    }

    //Sedimentación
    private void sedimentación(String usuario, String correo, String contraseña,String conf_contraseña, String fecha, String paises){
        campos_correctos = 0;
        //Validar campos no vacios

        if (!usuario.isEmpty()){
            usuario = usuario.replace(" ", "").toLowerCase();
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (!correo.isEmpty()){
            correo = correo.replace(" ", "").toLowerCase();
            Pattern pattern_correo = Patterns.EMAIL_ADDRESS;
            if (pattern_correo.matcher(correo).matches()){
                campos_correctos = campos_correctos+1;
            }else{
                campos_correctos = campos_correctos-1;
                Toast toast = Toast.makeText(Registro.this, "Formato no valido", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Ingrese un correo", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (!contraseña.isEmpty()){
            contraseña = contraseña.replace(" ", "");
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Ingrese una contraseña", Toast.LENGTH_SHORT);
            toast.show();
            campos_correctos = campos_correctos-1;
        }

        if (!conf_contraseña.isEmpty()){
            conf_contraseña = conf_contraseña.replace(" ", "");
            if (contraseña.equals(conf_contraseña)) {
                campos_correctos = campos_correctos+1;
            }else{
                campos_correctos = campos_correctos-1;
                Toast toast = Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
                toast.show();
                campos_correctos = campos_correctos-1;
            }
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Confirme la contraseña", Toast.LENGTH_SHORT);
            toast.show();
            campos_correctos = campos_correctos-1;
        }

        if (!fecha.isEmpty()){
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Seleccione una fecha", Toast.LENGTH_SHORT);
            toast.show();
            campos_correctos = campos_correctos-1;
        }

        if (!paises.isEmpty()){
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Seleccione un país", Toast.LENGTH_SHORT);
            toast.show();
            campos_correctos = campos_correctos-1;
        }
    }
    private void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,String msg) {
        textInputLayout.setError(msg);
        textInputLayout.setErrorEnabled(msg != null);
    }

    private void  crear(String usuario, String correo, String contraseña, String fecha, String paises){
        String url = "https://www.carolinabr.tk/app/index.php";
        StringRequest postResquest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String value = jsonObject.getString("value");
                    String mensaje = jsonObject.getString("mensaje");
                    if (value.equals("TRUE")){
                        Toast toast = Toast.makeText(Registro.this, mensaje, Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(Registro.this, mensaje, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("error",error.getMessage());
            }
        })
        {
            protected Map<String, String > getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("contraseña", contraseña);
                params.put("usuario", usuario);
                params.put("correo", correo);
                params.put("fecha", fecha);
                params.put("pais", paises);
                params.put("request", "registrar");
                params.put("nivel", "normi");
                return params;
            }
        };
        Volley.newRequestQueue(Registro.this).add(postResquest);
    }
}