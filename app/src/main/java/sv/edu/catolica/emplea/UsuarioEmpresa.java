package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UsuarioEmpresa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_empresa);
    }
    public void Ingresar(View view) {
        Intent intent= new Intent(UsuarioEmpresa.this, UsuarioEmpresa.class);
        startActivity(intent);
        finish();
    }
}