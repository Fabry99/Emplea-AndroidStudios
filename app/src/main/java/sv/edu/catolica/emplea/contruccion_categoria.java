package sv.edu.catolica.emplea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class contruccion_categoria extends AppCompatActivity {
    List<listElement> elements;
    NavigationView navigationView;
    ImageButton iconomenu;
    ImageButton atras;
    GestureDetectorCompat Gesto;
    adaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contruccion_categoria);
        init();
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textNombre = headerView.findViewById(R.id.txtNombreUsu);
        String nombreUsuario= getIntent().getStringExtra("nombreUsuario");
        textNombre.setText( nombreUsuario );
        Gesto = new GestureDetectorCompat(this
                , new GestureListener());
        iconomenu = findViewById(R.id.icomenu);
        atras = findViewById(R.id.btnAtras);
        View fondoLayout = findViewById(R.id.cat_construccion);
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

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setVisibility(View.INVISIBLE);
                atras.setVisibility(View.INVISIBLE);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(contruccion_categoria.this, inicioofertas.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_salir) {
                    Intent intent = new Intent(contruccion_categoria.this, login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return true;
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


    public static class MyVolleyRequest {
        public static void obtenerDatosDesdeServidor(contruccion_categoria context, final contruccion_categoria.MyVolleyRequest.VolleyCallback callback) {
            String url = "http://192.168.162.1/emplea/cat_construccion.php";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            callback.onSuccess(response);
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error.getMessage());
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonArrayRequest);
        }

        public interface VolleyCallback {
            void onSuccess(JSONArray result);

            void onError(String error);
        }
    }


    public void init() {
        contruccion_categoria.MyVolleyRequest.obtenerDatosDesdeServidor(contruccion_categoria.this, new contruccion_categoria.MyVolleyRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                elements = parseJSONArray(result);

                adaptador = new adaptador(elements, contruccion_categoria.this);
                RecyclerView recyclerView = findViewById(R.id.listRecyclerViewConstruccion);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(contruccion_categoria.this));
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(contruccion_categoria.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List<listElement> parseJSONArray(JSONArray jsonArray) {
        List<listElement> elements = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject oferta = jsonArray.getJSONObject(i);
                String id=oferta.getString("Id_Ofertas_Empleo");
                String nombre = oferta.getString("Nombre");
                String descripcion = oferta.getString("Descripcion");
                String idCategoria = oferta.getString("nom_categoria");
                String idUsuariosEmpresa = oferta.getString("nom_empresa");
                elements.add(new listElement(nombre, descripcion, idCategoria, idUsuariosEmpresa,id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return elements;
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
}