package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Postulaciones extends AppCompatActivity {

    List<listElement> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postulaciones);
        init();
    }
    public void init(){
        elements=new ArrayList<>();
        elements.add(new listElement("Ingeniero de automatización de procesos","Ingeniero industrial o afines, Experiencia liderando procesos de mejora continua y transformacion digital","Ingenieria","ICIA","1"));

        elements.add(new listElement("Ingeniero de Control de Calidad","graduado de ingeniería Civil, experiencia en proyectos de construcción","Ingenieria", "ICIA","2"));


        adaptador adaptador=new adaptador(elements,this);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerViewPostulaciones);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);
    }
}