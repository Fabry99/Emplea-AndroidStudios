package sv.edu.catolica.emplea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class VerOfertas extends AppCompatActivity {
    private static final int SELECCIONAR_DOCUMENTO = 1;
    private RequestQueue requestQueue;
    private String url = "http://192.168.1.6/emplea/enviarcurriculum.php";
    private Button btnsubir, btnEnviar;
    private TextView textVerRuta, textTitulo, textDescripcion, textempresa,textcat,id_oferta;
    private Uri uriDocumentoSeleccionado;

    // Obtén la referencia a tu proyecto de Firebase Storage
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://emplea-8cff0.appspot.com");
    private StorageReference storageRef = storage.getReference();
    NavigationView navigationView;
    ImageButton iconomenu;
    ImageButton atras;
    GestureDetectorCompat Gesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ofertas);
        btnsubir = findViewById(R.id.btnAplicar);
        btnEnviar = findViewById(R.id.btnenviar);
        textVerRuta = findViewById(R.id.nombres);
        textTitulo=findViewById(R.id.txtTitulo);
        textDescripcion=findViewById(R.id.txtDescripcion);
        textcat=findViewById(R.id.txtCat);
        id_oferta=findViewById(R.id.id_oferta);
        textempresa=findViewById(R.id.txtEmpresa);
        navigationView = findViewById(R.id.nav_view);
        Gesto = new GestureDetectorCompat(this
                , new VerOfertas.GestureListener());
        iconomenu = findViewById(R.id.icomenu);
        atras = findViewById(R.id.btnAtras);

        requestQueue = Volley.newRequestQueue(this);

        View fondoLayout = findViewById(R.id.verOferta);
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
                if (item.getItemId()==R.id.nav_home){
                    Intent intent=new Intent(VerOfertas.this, inicioofertas.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    

        String Id_oferta=getIntent().getStringExtra("Id_oferta");
        id_oferta.setText(Id_oferta);
        String nombreUsuario= getIntent().getStringExtra("Nombre");
        textTitulo.setText(nombreUsuario );
        String descripcion=getIntent().getStringExtra("Empresa");
        textDescripcion.setText(descripcion);
        String Categoria=getIntent().getStringExtra("Direccion");
        textcat.setText( Categoria);
        String Empresa=getIntent().getStringExtra("Publicado");
        textempresa.setText(  Empresa);


        btnsubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarDocumento();

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriDocumentoSeleccionado != null) {
                    enviarADocumentoFirebase();
                } else {
                    Toast.makeText(VerOfertas.this, "Selecciona un documento antes de enviar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void seleccionarDocumento() {
        // Intenta seleccionar un documento
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Seleccionar un archivo"), SELECCIONAR_DOCUMENTO);
        } catch (android.content.ActivityNotFoundException ex) {
            // Manejar excepción
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECCIONAR_DOCUMENTO && resultCode == RESULT_OK) {
            if (data != null) {
                uriDocumentoSeleccionado = data.getData();
                String nombreDocumento = obtenerNombreDocumento(uriDocumentoSeleccionado);
                textVerRuta.setText(nombreDocumento);
            }
        }
    }

    private String obtenerNombreDocumento(Uri uri) {
        // Obtener el nombre del documento
        String nombreDocumento = null;

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    nombreDocumento = cursor.getString(displayNameIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return nombreDocumento;
    }

    private void enviarADocumentoFirebase() {
        // Crear una referencia al lugar en Firebase Storage donde se almacenará el documento
        StorageReference documentoRef = storageRef.child("documentos/" + obtenerNombreDocumento(uriDocumentoSeleccionado));

        try {
            InputStream stream = getContentResolver().openInputStream(uriDocumentoSeleccionado);
            documentoRef.putStream(stream)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Manejar el éxito del proceso de carga
                        Toast.makeText(VerOfertas.this, "Documento enviado exitosamente", Toast.LENGTH_SHORT).show();
                        enviarRutaDocumentoAlServidor(obtenerNombreDocumento(uriDocumentoSeleccionado));
                        textVerRuta.setText("");
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Manejar el fallo del proceso de carga
                        Toast.makeText(VerOfertas.this, "Error al enviar el documento ", Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(VerOfertas.this, "Error al abrir el archivo", Toast.LENGTH_SHORT).show();
        }
    }
    private void enviarRutaDocumentoAlServidor(String nombreDocumento) {

        // Crear una StringRequest para enviar la ruta completa del documento al servidor
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            Toast.makeText(VerOfertas.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores que ocurrieron al intentar hacer la solicitud
                        Toast.makeText(VerOfertas.this, "Error en la solicitud Volley", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                // Establecer los parámetros para la solicitud POST
                java.util.Map<String, String> params = new java.util.HashMap<>();

                // Combinar la ruta de la carpeta y el nombre del documento
                String rutaCompleta = "documentos/" + nombreDocumento;

                params.put("rutaDocumentoFirebase", rutaCompleta);

                // Agregar el id_oferta como parámetro
                String id_oferta_value = id_oferta.getText().toString(); // Obtener el valor del TextView id_oferta
                params.put("id_oferta", id_oferta_value);

                return params;
            }
        };

        // Agregar la solicitud a la RequestQueue
        requestQueue.add(stringRequest);
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