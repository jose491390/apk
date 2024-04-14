package com.example.ga8_220501096_aa2_ev02_apk;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText txtdocumento, txtnombre, txtcargo, txtcontrasenia;
    private Button btnguardar, btnbuscar, btnactualizar, btneliminar, btnlimpiar;
    private ListView lvDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtdocumento = (EditText) findViewById(R.id.txtdocumento);
        txtnombre = (EditText) findViewById(R.id.txtnombre);
        txtcargo = (EditText) findViewById(R.id.txtcargo);
        txtcontrasenia = (EditText) findViewById(R.id.txtcontrasenia);
        btnguardar = (Button) findViewById(R.id.btnguardar);
        btnbuscar = (Button) findViewById(R.id.btnbuscar);
        btnactualizar = (Button) findViewById(R.id.btnactualizar);
        btneliminar = (Button) findViewById(R.id.btneliminar);
        btnlimpiar = (Button) findViewById(R.id.btnlimpiar);
        lvDatos = (ListView) findViewById(R.id.lvDatos);

        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
        botonLimpiar();
        listarEmpleados();

    } // esta llave cierra el oncreate

    private void botonBuscar(){
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtdocumento.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(MainActivity.this, "digite el documento a buscar", Toast.LENGTH_SHORT).show();
                }else {
                    int documento = Integer.parseInt(txtdocumento.getText().toString());
                    FirebaseDatabase bd = FirebaseDatabase.getInstance();
                    DatabaseReference bdref = bd.getReference(Empleado.class.getSimpleName());
                    bdref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(documento);
                            boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if (aux.equalsIgnoreCase(x.child("documento").getValue().toString())){
                                    res = true;
                                    ocultarTeclado();
                                    txtnombre.setText(x.child("nombre").getValue().toString());
                                    txtcargo.setText(x.child("cargo").getValue().toString());
                                    txtcontrasenia.setText(x.child("contrasenia").getValue().toString());
                                    break;
                                }
                            }
                            if (res == false){
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "el documento ("+aux+") no se encuentra registrado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }//cierra el dotodo buscar

    private void botonModificar(){
        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtdocumento.getText().toString().trim().isEmpty() ||
                        txtnombre.getText().toString().trim().isEmpty() ||
                        txtcargo.getText().toString().trim().isEmpty() ||
                        txtcontrasenia.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(MainActivity.this, "todos los campos son obligatorios!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    int documento = Integer.parseInt(txtdocumento.getText().toString());
                    String nombre = txtnombre.getText().toString();
                    String cargo = txtcargo.getText().toString();
                    String contrasenia = txtcontrasenia.getText().toString();

                    FirebaseDatabase bd = FirebaseDatabase.getInstance();
                    DatabaseReference bdref = bd.getReference(Empleado.class.getSimpleName());

                    bdref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux = Integer.toString(documento);
                            Boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if (x.child("documento").getValue().toString().equalsIgnoreCase(aux)){
                                    res = true;
                                    ocultarTeclado();
                                    x.getRef().child("cargo").setValue(cargo);
                                    x.getRef().child("contrasenia").setValue(contrasenia);
                                    txtdocumento.setText("");
                                    txtcargo.setText("");
                                    txtcontrasenia.setText("");
                                    txtnombre.setText("");
                                    listarEmpleados();
                                    Toast.makeText(MainActivity.this, "empleado actualizado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if (res == false){

                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "empleado no exite no se puede modificar", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            ocultarTeclado();
                            Toast.makeText(MainActivity.this, "no se puede guardar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }//cierra el metodo actualizar

    private void botonRegistrar(){
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtdocumento.getText().toString().trim().isEmpty() ||
                    txtnombre.getText().toString().trim().isEmpty() ||
                    txtcargo.getText().toString().trim().isEmpty() ||
                    txtcontrasenia.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(MainActivity.this, "todos los campos son obligatorios!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    int documento = Integer.parseInt(txtdocumento.getText().toString());
                    String nombre = txtnombre.getText().toString();
                    String cargo = txtcargo.getText().toString();
                    String contrasenia = txtcontrasenia.getText().toString();

                    FirebaseDatabase bd = FirebaseDatabase.getInstance();
                    DatabaseReference bdref = bd.getReference(Empleado.class.getSimpleName());

                    bdref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux = Integer.toString(documento);
                            Boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if (x.child("documento").getValue().toString().equalsIgnoreCase(aux)){
                                    res = true;
                                    ocultarTeclado();
                                    Toast.makeText(MainActivity.this, "el documento ("+aux+") ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if (res == false){
                                Empleado emp = new Empleado(documento, nombre, cargo, contrasenia);
                                bdref.push().setValue(emp);
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "empleado guardado", Toast.LENGTH_SHORT).show();
                                txtdocumento.setText("");
                                txtnombre.setText("");
                                txtcargo.setText("");
                                txtcontrasenia.setText("");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            ocultarTeclado();
                            Toast.makeText(MainActivity.this, "no se puede guardar", Toast.LENGTH_SHORT).show();


                        }
                    });

                }
            }
        });


    }//cierra el metodo registrar

    private void listarEmpleados(){
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference bdref = bd.getReference(Empleado.class.getSimpleName());

        ArrayList<Empleado>lisEmp = new ArrayList<Empleado>();
        ArrayAdapter<Empleado>ada = new ArrayAdapter<Empleado>(MainActivity.this, android.R.layout.simple_list_item_1, lisEmp);
        lvDatos.setAdapter(ada);

        bdref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                 Empleado emp = snapshot.getValue(Empleado.class);
                 lisEmp.add(emp);
                 ada.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ada.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Empleado emp = lisEmp.get(position);
                AlertDialog.Builder a  = new AlertDialog.Builder(MainActivity.this);
                a.setCancelable(true);
                a.setTitle("empleado selaccionado");
                String msg = "DOCUMENTO : " + emp.getDocumento() + "\n\n";
                msg += "NOMBRE : " + emp.getNombre() + "\n\n";
                msg += "CARGO : " + emp.getCargo() + "\n\n";

                a.setMessage(msg);
                a.show();
            }
        });

    }//cierra el metodo listar empleados

    private void botonEliminar() {
        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtdocumento.getText().toString().trim().isEmpty()) {
                    ocultarTeclado();
                    Toast.makeText(MainActivity.this, "Digite el documento a eliminar", Toast.LENGTH_SHORT).show();
                } else {
                    int documento = Integer.parseInt(txtdocumento.getText().toString());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Empleado");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(documento);
                            boolean empleadoEncontrado = false;

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("documento").getValue() != null) {
                                    String documentoEmpleado = dataSnapshot.child("documento").getValue().toString();

                                    if (aux.equalsIgnoreCase(documentoEmpleado)) {
                                        // Obtener datos del empleado
                                        String nombreEmpleado = dataSnapshot.child("nombre").getValue().toString();
                                        String cargoEmpleado = dataSnapshot.child("cargo").getValue().toString();

                                        // Mostrar detalles en AlertDialog
                                        AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                                        a.setCancelable(false);
                                        a.setTitle("Alerta");
                                        a.setMessage("¿Está seguro que desea eliminar este empleado?" +
                                                "\n\nDocumento: " + documentoEmpleado +
                                                "\nNombre: " + nombreEmpleado +
                                                "\nCargo: " + cargoEmpleado);

                                        a.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // No hacer nada si se cancela
                                            }
                                        });

                                        a.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Eliminar el empleado
                                                dataSnapshot.getRef().removeValue();
                                                ocultarTeclado();
                                                listarEmpleados(); // Actualizar la lista de empleados después de eliminar
                                                txtdocumento.setText("");
                                                txtnombre.setText("");
                                                txtcargo.setText("");
                                                txtcontrasenia.setText("");
                                                Toast.makeText(MainActivity.this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        a.show();
                                        empleadoEncontrado = true;
                                        break;
                                    }
                                }
                            }

                            if (!empleadoEncontrado) {
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "El documento (" + aux + ") no se encuentra registrado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Manejar error de Firebase
                            Log.e("Firebase", "Error al leer datos", error.toException());
                            ocultarTeclado();
                            Toast.makeText(MainActivity.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }//cierra el metodo eliminar

    private void ocultarTeclado(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    } // Cierra el método ocultarTeclado.

    private void botonLimpiar(){
        btnlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();
                txtcargo.setText("");
                txtnombre.setText("");
                txtcontrasenia.setText("");
                txtdocumento.setText("");
            }
        });

    }//cierra el metodo limpiar



} // esta llave cierra la class