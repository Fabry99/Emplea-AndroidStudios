package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;
import sv.edu.catolica.emplea.singleton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class nav_header_menu1 extends AppCompatActivity {
        TextView textNombre; ImageView fotousuario;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nav_header_menu1);
            singleton userDataSingleton = singleton.getInstance();
            textNombre=findViewById(R.id.txtNombreUsu);
            fotousuario=findViewById(R.id.imageView3);

            String nombreUsuario= getIntent().getStringExtra("nombreUsuario");
            textNombre.setText(" Bienvenido " + nombreUsuario + "!");



        }
}