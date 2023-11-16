package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Inicio_Empresa extends AppCompatActivity {
    ImageButton nuevaoferta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_empresa);
        nuevaoferta=findViewById(R.id.NuevaOferta);
        nuevaoferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Inicio_Empresa.this, Nueva_Oferta.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void nuevaOferta(View view) {

    }
}