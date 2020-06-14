package com.tamersarioglu.personbooker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar_mainActivity;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Kisiler> kisilerArrayList;
    private KisiAdapter adapter;
    private Veritabani vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_mainActivity = findViewById(R.id.toolbar_mainActivity);
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        vt = new Veritabani(this);

        toolbar_mainActivity.setTitle("Person Identify");
        setSupportActionBar(toolbar_mainActivity);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        kisilerArrayList = new KisilerDAO().tumKisiler(vt);

        adapter = new KisiAdapter(this, kisilerArrayList, vt);

        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                alertGoster();
            }
        });
    }

    public void alertGoster() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        final View tasarim = layoutInflater.inflate(R.layout.alertview_kisi_ekle, null);

        final EditText editTextAd = tasarim.findViewById(R.id.editText_AlertView_Ad);
        final EditText editTextTel = tasarim.findViewById(R.id.editText_AlertView_Tel);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Kişi Ekle");
        alertDialog.setView(tasarim);
        alertDialog.setPositiveButton("EKLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ad = editTextAd.getText().toString().trim();
                String tel = editTextTel.getText().toString().trim();

                if (ad.isEmpty() || tel.isEmpty()){
                    Toast.makeText(MainActivity.this,"HER İKİ ALANI DA DOLDURUNUZ!!!",Toast.LENGTH_SHORT).show();
                                       
                } else {
                    new KisilerDAO().kisiEkle(vt, ad, tel);

                    kisilerArrayList = new KisilerDAO().tumKisiler(vt);
                    adapter = new KisiAdapter(MainActivity.this, kisilerArrayList, vt);
                    recyclerView.setAdapter(adapter);

                    Toast.makeText(getApplicationContext(), "" + ad + " " + tel, Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_ara);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_ara) {
            Snackbar.make(toolbar_mainActivity, "Ara Tıklandı", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("onQueryTextChange", newText);

        kisilerArrayList = new KisilerDAO().kisiAra(vt, newText);

        adapter = new KisiAdapter(this, kisilerArrayList, vt);

        recyclerView.setAdapter(adapter);

        return false;
    }
}