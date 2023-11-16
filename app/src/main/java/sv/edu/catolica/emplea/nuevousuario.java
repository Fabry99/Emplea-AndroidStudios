package sv.edu.catolica.emplea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class nuevousuario extends AppCompatActivity {
    public TextInputEditText nombre, apellido, correos, contra, dui;
    private Button btnregistrar;
    TextView error_message, titulo; RadioButton rbUsuario,rbEmpresa; RadioGroup tipousuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevousuario);

        nombre = findViewById(R.id.nombres);
        apellido = findViewById(R.id.apellido);
        correos = findViewById(R.id.correo);
        contra = findViewById(R.id.password);
        btnregistrar = findViewById(R.id.bntenviar);
        dui = findViewById(R.id.txIidentificacion);
        error_message=findViewById(R.id.error_message);
        titulo =findViewById(R.id.txt_titulo);
        rbEmpresa=findViewById(R.id.rbEmpresa);
        rbUsuario=findViewById(R.id.rbUsuario);
        tipousuario=findViewById(R.id.user_type);
        titulo.setText("Registro de Usuario");
        titulo.setVisibility(View.VISIBLE);
        nombre.setHint("Nombres");
        apellido.setHint("Apellidos");
        correos.setHint("Correo");
        contra.setHint("Contraseña");
        dui.setHint("Dui");

        tipousuario.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId ==rbUsuario.getId()) {
                    titulo.setText("Registro de Usuario");
                    titulo.setVisibility(View.VISIBLE);
                    nombre.setHint("Nombres");
                    apellido.setHint("Apellidos");
                    correos.setHint("Correo");
                    contra.setHint("Contraseña");
                    dui.setHint("DUI");
                    nombre.setText("");
                    apellido.setText("");
                    correos.setText("");
                    contra.setText("");
                    dui.setText("");

                } else if (checkedId == rbEmpresa.getId()) {
                    titulo.setText("Registro de Empresa");
                    titulo.setVisibility(View.VISIBLE);
                    nombre.setHint("Nombre");
                    apellido.setHint("Tipo de Empresa");
                    correos.setHint("Correo de Empresa");
                    contra.setHint("Contraseña");
                    dui.setHint("NRC");
                    nombre.setText("");
                    apellido.setText("");
                    correos.setText("");
                    contra.setText("");
                    dui.setText("");
                }
            }
        });



        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbUsuario.isChecked()){
                    registrarDatos();
                } else if (rbEmpresa.isChecked()) {
                    registrarDatosEmpresa();
                }else{
                    error_message.setText("Ocurrio un error");
                }

            }
        });
    }

    private void registrarDatos() {
        String nombres = nombre.getText().toString().trim();
        String apellidos = apellido.getText().toString().trim();
        String correo = correos.getText().toString().trim();
        String password = contra.getText().toString().trim();
        String identificacion = dui.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");

        if (nombres.isEmpty() || apellidos.isEmpty() || password.isEmpty() || identificacion.isEmpty()) {
            error_message.setText("Por favor complete todos los campos");
            error_message.setVisibility(View.VISIBLE);
        } else if (!isValidEmail(correo)) {
            error_message.setText("El correo electrónico ingresado no es válido");
            error_message.setVisibility(View.VISIBLE);
        } else if (!isValidPassword(password)) {
            error_message.setText("La contraseña debe contener al menos una letra mayúscula, un número y un carácter especial.");
            error_message.setVisibility(View.VISIBLE);
        } else if (!isValidDUI(identificacion)) {
            error_message.setText("El DUI ingresado no es válido");
            error_message.setVisibility(View.VISIBLE);
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.162.1/emplea/registrarUsuario.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Datos ingresados correctamente")) {
                                Toast.makeText(nuevousuario.this, "Tus datos han sido ingresados correctamente", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(nuevousuario.this, login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 1200);
                            } else {
                                error_message.setText("No se pudo registrar");
                                error_message.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(nuevousuario.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nombres", nombres);
                    params.put("apellidos", apellidos);
                    params.put("correo", correo);
                    params.put("password", password);
                    params.put("identificacion", identificacion);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(nuevousuario.this);
            requestQueue.add(request);
        }
    }

    private void registrarDatosEmpresa() {
        String nombres = nombre.getText().toString().trim();
        String tipoEmpresa = apellido.getText().toString().trim();
        String correo = correos.getText().toString().trim();
        String password = contra.getText().toString().trim();
        String identificacion = dui.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");

        if (nombres.isEmpty() || tipoEmpresa.isEmpty() ||correo.isEmpty() || password.isEmpty() || identificacion.isEmpty()) {
            error_message.setText("Por favor complete todos los campos");
            error_message.setVisibility(View.VISIBLE);
        } else if (!isValidEmail(correo)) {
            error_message.setText("El correo electrónico ingresado no es válido");
            error_message.setVisibility(View.VISIBLE);
        } else if (!isValidPassword(password)) {
            error_message.setText("La contraseña debe contener al menos una letra mayúscula, un número y un carácter especial.");
            error_message.setVisibility(View.VISIBLE);
        } else if (!isValidDUI(identificacion)) {
            error_message.setText("El Codigo de empresa ingresado no es válido");
            error_message.setVisibility(View.VISIBLE);
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.1.6/emplea/registrarEmpresa.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Datos ingresados correctamente")) {
                                Toast.makeText(nuevousuario.this, "Tus datos han sido ingresados correctamente", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(nuevousuario.this, login.class);
                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(nuevousuario.this);
                                        startActivity(intent, options.toBundle());
                                        finish();
                                    }
                                }, 1200);
                            } else {
                                error_message.setText("No se pudo registrar");
                                error_message.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(nuevousuario.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nombres", nombres);
                    params.put("tipoEmpresa", tipoEmpresa);
                    params.put("correo", correo);
                    params.put("password", password);
                    params.put("identificacion", identificacion);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(nuevousuario.this);
            requestQueue.add(request);
        }
    }

    private boolean isValidDUI(String dui) {
        // verifica el formato del DUI (9 dígitos)
        String duiPattern = "^[0-9]{9}$";
        return dui.matches(duiPattern);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // verifica la contraseña (al menos 1 mayúscula, 1 número y 1 carácter especial)
        String passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=,._:;-])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }
}