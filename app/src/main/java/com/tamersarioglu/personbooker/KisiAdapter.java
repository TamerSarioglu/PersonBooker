package com.tamersarioglu.personbooker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class KisiAdapter extends RecyclerView.Adapter<KisiAdapter.KartTasarimTutucu> {

    private Context mContext;
    private List<Kisiler> kisilerListView;
    private Veritabani vt;

    public KisiAdapter(Context mContext, List<Kisiler> kisilerListView, Veritabani vt) {
        this.mContext = mContext;
        this.kisilerListView = kisilerListView;
        this.vt = vt;
    }

    @NonNull
    @Override
    public KartTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_kart_tasarim, parent, false);
        return new KartTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KartTasarimTutucu holder, final int position) {
        final Kisiler kisi = kisilerListView.get(position);
        holder.textViewKisiBilgi.setText(String.format("%s - %s", kisi.getKisi_ad(), kisi.getKisi_tel()));
        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_sil:
                                Snackbar.make(v, "Kişi Silinsin Mi?", BaseTransientBottomBar.LENGTH_SHORT).setAction("SİL", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new KisilerDAO().kisiSil(vt, kisi.getKisi_id());
                                        kisilerListView = new KisilerDAO().tumKisiler(vt);
                                        notifyDataSetChanged();
                                    }
                                }).show();

                                return true;
                            case R.id.action_guncelle:
                                alertGoster(kisi);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kisilerListView.size();
    }

    public static class KartTasarimTutucu extends RecyclerView.ViewHolder {

        private TextView textViewKisiBilgi;
        private ImageView imageViewNokta;

        public KartTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            textViewKisiBilgi = itemView.findViewById(R.id.textViewKisiBilgi);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);
        }
    }

    public void alertGoster(final Kisiler kisi) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        final View tasarim = layoutInflater.inflate(R.layout.alertview_kisi_ekle, null);

        final EditText editTextAd = tasarim.findViewById(R.id.editText_AlertView_Ad);
        final EditText editTextTel = tasarim.findViewById(R.id.editText_AlertView_Tel);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Kişi Güncelle");
        alertDialog.setView(tasarim);
        alertDialog.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ad = editTextAd.getText().toString().trim();
                String tel = editTextTel.getText().toString().trim();

                new KisilerDAO().kisiGuncelle(vt, kisi.getKisi_id(), ad, tel);
                kisilerListView = new KisilerDAO().tumKisiler(vt);
                notifyDataSetChanged();
                Toast.makeText(mContext, "" + ad + " " + tel, Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.create().show();
    }

}
