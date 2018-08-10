package com.example.gabriel.puntodeapoyo;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText dni,mail,password,passwordConfirm;
    private Button submit;
    private ProgressDialog progressDialog;
    private TextInputLayout tilDni,tilMail,tilPassword,tilPasswordConfirm;
    private boolean dniValid,mailValid,passwordValid;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        dni=findViewById(R.id.resgisterDNI);
        mail=findViewById(R.id.registerMail);
        password=findViewById(R.id.registerPassword);
        passwordConfirm=findViewById(R.id.confirmpassword);
        submit=findViewById(R.id.confirmarYEnviar);
        tilDni=findViewById(R.id.tilRegisterDni);
        tilMail=findViewById(R.id.tilRegisterMail);
        tilPassword=findViewById(R.id.tilRegisterPassword);
        tilPasswordConfirm=findViewById(R.id.tilConfirmPassword);
        dni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (dni.getText().toString().length() > 8 || dni.getText().toString().length() < 7 ){
                    tilDni.setError("El documento debe contener entre 7 y 8 caracteres");
                    tilDni.setErrorEnabled(true);
                }else {
                    tilDni.setErrorEnabled(false);
                    dniValid=true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               boolean esValido= android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
               tilMail.setError("Correo no valido");
               tilMail.setErrorEnabled(!esValido);
               mailValid=esValido;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean esValido=passwordConfirm.getText().toString().equals(password.getText().toString());
                tilPasswordConfirm.setError("Las contraseñas no coinciden");
                tilPasswordConfirm.setErrorEnabled(!esValido);
                passwordValid=esValido;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean esValido=passwordConfirm.getText().toString().equals(password.getText().toString());
                tilPasswordConfirm.setError("Las contraseñas no coinciden");
                tilPasswordConfirm.setErrorEnabled(!esValido);
                passwordValid=esValido;
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampo(dni) && validarCampo(mail) && validarCampo(password) && validarCampo(passwordConfirm)) {
                    if (dniValid && mailValid && passwordValid){
                        registrarUsuario();
                    }else {
                        Snackbar.make(findViewById(R.id.registroLayout),"Verifique los campos con errores",Snackbar.LENGTH_LONG).show();
                    }
                }else {
                    Snackbar.make(findViewById(R.id.registroLayout),"Complete los campos vacios",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        requestQueue= Volley.newRequestQueue(this);
    }


    public boolean validarCampo(EditText editText){
        return !TextUtils.isEmpty(editText.getText().toString());
    }


    public void registrarUsuario(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();

        String url="https://gabiiascurra.000webhostapp.com/AddUser.php";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("registrado")){
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String textDni=dni.getText().toString();
                String textMail=mail.getText().toString();
                String textPassword=password.getText().toString();

                Map<String,String> parametros=new HashMap<>();
                parametros.put("email",textMail);
                parametros.put("dni",textDni);
                parametros.put("pass",textPassword);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}
