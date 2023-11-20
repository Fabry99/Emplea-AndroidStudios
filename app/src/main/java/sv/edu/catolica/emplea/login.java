package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;
import sv.edu.catolica.emplea.singleton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText Correo, Password;
    TextView error_message;
    Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTheme(R.style.Base_Theme_Emplea);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Correo = findViewById(R.id.txtusuario);
        Password = findViewById(R.id.txtPassword);
        btnEntrar = findViewById(R.id.btnLogin);
        error_message = findViewById(R.id.txterror_message);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarUsuario("http://192.168.1.6/emplea/validar_login.php");
            }
        });
    }

    private void validarUsuario(String URL) {
        String correo = Correo.getText().toString().trim();
        String password = Password.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (correo.isEmpty() || password.isEmpty()) {
            error_message.setText("Por favor, Complete todos los campos");
            error_message.setVisibility(View.VISIBLE);
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    String message = jsonResponse.getString("message");

                    if (success) {
                        int idTipo = jsonResponse.getInt("id_tipo");
                        singleton userDataSingleton = singleton.getInstance();
                        userDataSingleton.setFotousuario(jsonResponse.getString("foto_usuario"));
                        userDataSingleton.setNombreusuario(jsonResponse.getString("nombreUsuario"));
                        userDataSingleton.setIdUsuario(jsonResponse.getString("id_usuario"));
                        Log.d("SINGLETON", "Foto del usuario almacenada en Singleton: " + userDataSingleton.getFotousuario());
                        Log.d("SINGLETON", "Nombre del usuario almacenada en Singleton: " + userDataSingleton.getNombreusuario());
                        Log.d("SINGLETON", "Id del usuario almacenada en Singleton: " + userDataSingleton.getIdUsuario());


                        if (idTipo == 3) {
                            Log.d("LOGIN", "Login exitoso con id_tipo: " + idTipo);

                            // Aquí puedes hacer lo que necesites con idTipo
                            // Por ejemplo, puedes iniciar una nueva actividad
                            Intent intent = new Intent(login.this, inicioofertas.class);
                            intent.putExtra("nombreUsuario", jsonResponse.getString("nombreUsuario"));
                            intent.putExtra("id_usuario", jsonResponse.getString("id_usuario"));
                            intent.putExtra("foto_usuario", jsonResponse.getString("foto_usuario"));
                            startActivity(intent);
                            finish();
                        }else if(idTipo==2) {
                            Log.d("LOGIN", "Login exitoso con id_tipo: " + idTipo);

                            // Aquí puedes hacer lo que necesites con idTipo
                            // Por ejemplo, puedes iniciar una nueva actividad
                            Intent intent = new Intent(login.this, Inicio_Empresa.class);
                            intent.putExtra("nombreUsuario", jsonResponse.getString("nombreUsuario"));
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.d("LOGIN", "Login fallido: " + message);
                        error_message.setText(message);
                        if (message.equals("Usuario o contraseña incorrectos")){
                          error_message.setText("Usuario o contraseña incorrectos");
                            error_message.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON_ERROR", "Error al analizar la respuesta JSON: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("VOLLEY_ERROR", "Error de red: " + error.getMessage());
                error_message.setText("Error de red:" + error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("correo", correo);
                parametros.put("password", password);
                return parametros;
            }
        };

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("VOLLEY_ERROR", "Excepción: " + e.getMessage());
            Toast.makeText(login.this, "Error de red", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarse(View view) {
        Intent intent = new Intent(login.this, nuevousuario.class);
        startActivity(intent);
    }
}