package sv.edu.catolica.emplea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import android.view.GestureDetector;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class inicioofertas extends AppCompatActivity {
    List<listElement> elements;
    NavigationView navigationView;
    ImageButton iconomenu;
    ImageButton atras;
    GestureDetectorCompat Gesto;
    adaptador adaptador;
    String nombreUsuario = "nombreUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicioofertas);
        init();
        obtenerDatosOfertas();
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textNombre = headerView.findViewById(R.id.txtNombreUsu);
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        textNombre.setText(nombreUsuario);
        Gesto = new GestureDetectorCompat(this, new GestureListener());
        iconomenu = findViewById(R.id.icomenu);
        atras = findViewById(R.id.btnAtras);

        // Configurar el clic en el fondo para cerrar la navegación
        View fondoLayout = findViewById(R.id.inicioferta);
        fondoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationView.isShown()) {
                    navigationView.setVisibility(View.INVISIBLE);
                    atras.setVisibility(View.INVISIBLE);
                }
            }
        });

        iconomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setVisibility(View.VISIBLE);
                atras.setVisibility(View.VISIBLE);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(inicioofertas.this, inicioofertas.class);
                    startActivity(intent);
                } else if (item.getItemId()==R.id.nav_gallery) {
                    Intent intent=new Intent(inicioofertas.this, Postulaciones.class);
                    startActivity(intent);
                    
                } else if (item.getItemId() == R.id.nav_salir) {
                    Intent intent = new Intent(inicioofertas.this, login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return true;
            }

        });


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setVisibility(View.INVISIBLE);
                atras.setVisibility(View.INVISIBLE);
            }
        });
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            if (navigationView.isShown()) {
                navigationView.setVisibility(View.INVISIBLE);
                atras.setVisibility(View.INVISIBLE);
            }
            return true;
        }
    }

    public void init() {
        elements = new ArrayList<>();
        adaptador = new adaptador(elements, this);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);
    }

    private void obtenerDatosOfertas() {
        String url = "http://192.168.162.1/emplea/ofertas_empleos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            // Limpiar la lista antes de agregar nuevos elementos
                            elements.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject oferta = jsonArray.getJSONObject(i);

                                // Obtener los datos de la oferta
                                String id= oferta.getString("Id_Ofertas_Empleo");
                                String nombre = oferta.getString("Nombre");
                                String descripcion = oferta.getString("Descripcion");
                                String idCategoria = oferta.getString("nom_categoria");
                                String idUsuariosEmpresa = oferta.getString("nom_empresa");

                                // Agregar la oferta a la lista
                                elements.add(new listElement(nombre, descripcion, idCategoria, idUsuariosEmpresa,id));
                            }


                            // Notificar al adaptador que los datos han cambiado
                            adaptador.notifyDataSetChanged();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Manejar errores de la solicitud
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (navigationView.getVisibility() == View.VISIBLE) {
            navigationView.setVisibility(View.INVISIBLE);
            atras.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    public void verIngenieria(View view) {
        Intent intent= new Intent(inicioofertas.this,Ingeniera_categoria.class);
        intent.putExtra("nombreUsuario", nombreUsuario);
        startActivity(intent);

    }

    public void verInformatica(View view) {
        Intent intent= new Intent(inicioofertas.this,Informatica_categoria.class);
        intent.putExtra("nombreUsuario", nombreUsuario);
        startActivity(intent);

    }

    public void verConstruccion(View view) {
        Intent intent= new Intent(inicioofertas.this,contruccion_categoria.class);
        intent.putExtra("nombreUsuario", nombreUsuario);
        startActivity(intent);

    }
}