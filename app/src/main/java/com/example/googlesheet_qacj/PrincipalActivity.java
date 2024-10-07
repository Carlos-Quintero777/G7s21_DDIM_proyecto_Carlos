package com.example.googlesheet_qacj;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue; // Importa esto
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private Spinner spinnerNombres;
    private ImageView imagenMostrar;
    private Button btnRecargar;
    private List<String> nombres = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private final String url = "https://sheets.googleapis.com/v4/spreadsheets/1eJlOowG6gXog3mPsHZYyGUI6VhhhhuQhO6ZnwY_INzw/values/'foticos'!A1:B?key=AIzaSyA1qJ0xJrwTBsOzqtCZvgXNNwq2SeCfNaQ";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnRecargar = findViewById(R.id.btnrecargar);
        spinnerNombres = findViewById(R.id.spinnernombres);
        imagenMostrar = findViewById(R.id.lblmostrar);

        queue = Volley.newRequestQueue(this);

        cargarDatosHoja(url);
        btnRecargar.setOnClickListener(v -> cargarDatosHoja(url));
    }

    private void cargarDatosHoja(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    nombres.clear();
                    urls.clear();
                    try {
                        JSONArray filas = response.getJSONArray("values");
                        for (int i = 1; i < filas.length(); i++) {
                            JSONArray fila = filas.getJSONArray(i);
                            nombres.add(fila.getString(0));
                            urls.add(fila.getString(1));
                        }
                        llenarSpinner();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PrincipalActivity.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PrincipalActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonObjectRequest);
    }

    private void llenarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNombres.setAdapter(adapter);

        spinnerNombres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String urlSeleccionado = urls.get(position);
                cargarImagen(urlSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void cargarImagen(String url) {
        Glide.with(this).load(url).into(imagenMostrar);
    }
}
