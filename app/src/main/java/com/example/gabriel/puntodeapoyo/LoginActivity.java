package com.example.gabriel.puntodeapoyo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registro,
                    resetPassword,
                    iniciar;
    private EditText usuario,
                    contraseña;

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
                //Intent intent2 = new Intent(LoginActivity.this,NavigationDrawerActivity.class);
                //startActivity(intent2);
                //onPause();
                break;

        }

    }
}
