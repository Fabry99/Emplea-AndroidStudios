package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.PopUpToBuilder;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PerfilUsuario extends AppCompatActivity {
    private PopupWindow popupWindow;
    private ImageView perfilImageView;
    private static final int SELECCIONAR_IMAGEN_REQUEST = 1;
    private StorageReference storageReference;
    String mostrarIDRecib="id_usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        mostrarIDRecib= getIntent().getStringExtra("id_usuario");

        perfilImageView = findViewById(R.id.perfil);

        final ImageView cambiarFoto = findViewById(R.id.cambiar);
        storageReference = FirebaseStorage.getInstance().getReference().child("imagenes_perfil");

        cambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.activity_menucambiarfoto, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                int[] location = new int[2];
                cambiarFoto.getLocationOnScreen(location);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                int width = displayMetrics.widthPixels;

                int xPos = (width - popupWindow.getWidth()) / 2;
                int yPos = location[1] + cambiarFoto.getHeight();

                popupWindow.showAtLocation(cambiarFoto, Gravity.NO_GRAVITY, xPos, yPos);

                // Manejar clics en los elementos del menú emergente
                popupView.findViewById(R.id.opcion1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Abrir la galería del teléfono para seleccionar una imagen
                        seleccionarImagenDesdeGaleria();

                        // Cierra el popup después de un breve retraso
                        popupView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popupWindow.dismiss();
                            }
                        }, 100);
                    }
                });

                popupView.findViewById(R.id.opcion2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Redirigir a la actividad Ingeniera_categoria
                        Intent intent = new Intent(PerfilUsuario.this, Ingeniera_categoria.class);
                        startActivity(intent);
                        popupView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popupWindow.dismiss();
                            }
                        }, 100);
                    }
                });
            }
        });
        // Manejar clic en el ImageView con el ID "perfil" en activity_perfil_usuario.xml
        perfilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pasa la información de la imagen a través de una variable
                int imagenEnGrandeResId = R.drawable.persona;
                mostrarImagenEnGrande(imagenEnGrandeResId);
            }
        });

        // Cerrar el PopupWindow cuando se toca cualquier parte de la pantalla
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void mostrarImagenEnGrande(int imagenResId) {
        // Abre la actividad de imagen en grande mediante un Intent y pasa la información de la imagen
        Intent intent = new Intent(PerfilUsuario.this, imagenengrande.class);
        intent.putExtra("imagen_res_id", imagenResId);
        startActivity(intent);
    }


    private void seleccionarImagenDesdeGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECCIONAR_IMAGEN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECCIONAR_IMAGEN_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imagenSeleccionadaUri = data.getData();
            if (imagenSeleccionadaUri != null) {
                // Mostrar la imagen seleccionada en el perfilImageView
                perfilImageView.setImageURI(imagenSeleccionadaUri);

                // Guardar la ruta con el nombre de la imagen en Firebase
                String nombreImagen = obtenerNombreDeArchivo(imagenSeleccionadaUri);
                subirImagenAFirebaseStorage(imagenSeleccionadaUri);
            }
        }
    }
    private void subirImagenAFirebaseStorage(Uri imageUri) {
        String nombreArchivo = obtenerNombreDeArchivo(imageUri);
        StorageReference imagenRef = storageReference.child(nombreArchivo);

        UploadTask uploadTask = imagenRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imagenRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String urlImagen = uri.toString();
                String rutaCompletaImagen = urlImagen + "/" + nombreArchivo;
                guardarDatosEnPHP(nombreArchivo, rutaCompletaImagen);
            });

            Toast.makeText(PerfilUsuario.this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(PerfilUsuario.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
        });
    }
    private void guardarDatosEnPHP(String nombreImagen, String rutaImagen) {
        String url = "http://192.168.1.6/emplea/subir_fotos.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(PerfilUsuario.this, response, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(PerfilUsuario.this, "Error en la solicitud HTTP", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombreImagen", nombreImagen);
                params.put("rutaImagen", rutaImagen);
                params.put("id_usuario",mostrarIDRecib);
                return params;
            }
        };

        queue.add(request);
    }

    private String obtenerNombreDeArchivo(Uri uri) {
        String resultado = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        resultado = cursor.getString(columnIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (resultado == null) {
            resultado = uri.getLastPathSegment();
        }
        return resultado;
    }
}