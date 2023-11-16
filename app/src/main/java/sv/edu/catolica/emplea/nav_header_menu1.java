package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class nav_header_menu1 extends AppCompatActivity {
    TextView textNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_header_menu1);
        textNombre=findViewById(R.id.txtNombreUsu);

        String nombreUsuario= getIntent().getStringExtra("nombreUsuario");
        textNombre.setText(" Bienvenido " + nombreUsuario + "!");
    }
}