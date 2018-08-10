package com.example.gabriel.puntodeapoyo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registro, resetPassword, iniciar;
    private EditText usuario, contraseña;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        usuario = findViewById(R.id.usuario);
        usuario.setSelection(0);
        contraseña = findViewById(R.id.contraseña);
        contraseña.setSelection(0);
        registro=findViewById(R.id.registro);
        registro.setOnClickListener(this);
        resetPassword=findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(this);
        iniciar=findViewById(R.id.iniciar);
        iniciar.setOnClickListener(this);
        requestQueue= Volley.newRequestQueue(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registro:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                onPause();
                break;
            case R.id.resetPassword:
                Intent intent1 = new Intent(LoginActivity.this,RecoveryPasswordActivity.class);
                startActivity(intent1);
                onPause();
                break;
            case R.id.iniciar:
                if ((usuario.getText().toString().isEmpty()) || (contraseña.getText().toString().isEmpty())){
                    Snackbar.make(findViewById(R.id.layoutLogin),
                            "Hay campos vacios",Snackbar.LENGTH_SHORT).show();
                }else {

                    verificarUsuario();
                }
                break;

        }

    }
    public void verificarUsuario(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión");
        progressDialog.show();
        String url="https://gabiiascurra.000webhostapp.com/LoginUser.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                if (response.trim().equalsIgnoreCase("Logueado")){
                    iniciarSesion(usuario.getText().toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //String textDni=dni.getText().toString();
                String textMail=usuario.getText().toString();
                String textPassword=contraseña.getText().toString();

                Map<String,String> parametros=new HashMap<>();
                parametros.put("email",textMail);
                //parametros.put("dni",textDni);
                parametros.put("pass",textPassword);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
    public void iniciarSesion(String email){
        SharedPreferences preferences=getSharedPreferences("Sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("email",email);
        editor.commit();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
