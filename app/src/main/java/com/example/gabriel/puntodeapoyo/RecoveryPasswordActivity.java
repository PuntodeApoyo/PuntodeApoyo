package com.example.gabriel.puntodeapoyo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecoveryPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    Button enviaryrecuperarcorreo;
    TextView recoveryPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_recovery_password);
        enviaryrecuperarcorreo=(Button)findViewById(R.id.enviaryrecuperarcorreo);
        enviaryrecuperarcorreo.setOnClickListener(this);
        recoveryPassword=(TextView)findViewById(R.id.recoveryPassword);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enviaryrecuperarcorreo:
                recoveryPassword.setText(getString(R.string.mensajerecuperacion));
                break;
        }

    }
}
