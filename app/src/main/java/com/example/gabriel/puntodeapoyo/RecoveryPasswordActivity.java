package com.example.gabriel.puntodeapoyo;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecoveryPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    Button enviaryrecuperarcorreo;
    TextView recoveryPassword;
    ProgressDialog progressDialog;
    TextInputLayout dni,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_recovery_password);
        enviaryrecuperarcorreo=findViewById(R.id.recoveySubmit);
        enviaryrecuperarcorreo.setOnClickListener(this);
        recoveryPassword=findViewById(R.id.recoveryPassword);
        dni=findViewById(R.id.recoveryDNI);
        email=findViewById(R.id.recoveryMail);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.recoveySubmit:
                if ((dni.getEditText().getText().toString().isEmpty()) || (email.getEditText().getText().toString().isEmpty())){
                    Snackbar.make(findViewById(R.id.layoutRecovery),
                            "Hay campos vacios",Snackbar.LENGTH_SHORT).show();
                }else{
                progressDialog=new ProgressDialog(this);
                progressDialog.setMessage("Realizando operación");
                progressDialog.show();
                }
                //recoveryPassword.setText(getString(R.string.mensajerecuperacion));
                break;
        }

    }
    public void recuperarContraseña(){

    }
}
