package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Tipo_Usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_usuario);
    }

    public void registroEmpresa(View view) {
        Intent intent= new Intent(Tipo_Usuario.this, UsuarioEmpresa.class);
        startActivity(intent);
        finish();
    }

    public void registropersonal(View view) {
        Intent intent= new Intent(Tipo_Usuario.this, nuevousuario.class);
        startActivity(intent);
        finish();

    }
}