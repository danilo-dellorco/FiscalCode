package it.runningexamples.fiscalcode;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> implements View.OnClickListener{
    private List<CodiceFiscaleEntity> savedCF;
    private CodiceFiscaleEntity lastDeleted;
    private Context mContext;
    private int lastDeletedPosition;
    private String stringCode;
    private List<CodiceFiscaleEntity> selectedItem = new ArrayList<>();
    private boolean selectionON = false;
    private PreferenceManager preferenceManager;
    private AdapterCallback adapterCallback;

    RecyclerAdapter(Context ctx, AdapterCallback callback){
        this.mContext = ctx;
        this.adapterCallback = callback;
        this.savedCF = AppDatabase.getInstance(mContext).codiceFiscaleDAO().getAll();
        preferenceManager = new PreferenceManager(mContext);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false));
    }


    @Override
    public void onBindViewHolder (@NonNull final Holder holder, final int position)  {
        final CodiceFiscaleEntity currentItem = savedCF.get(position);
        holder.tvNome.setText(currentItem.getNome());
        holder.tvCognome.setText(currentItem.getCognome());
        holder.tvData.setText(currentItem.getDataNascita());
        holder.tvLuogo.setText(currentItem.getLuogoNascita());
        if (currentItem.getGenere().equals("M")){
            holder.tvSesso.setText(R.string.genereMaschio);
        }
        else{
            holder.tvSesso.setText(R.string.genereFemmina);
        }
        holder.btnCode.setText(currentItem.getFinalFiscalCode());
        holder.btnCode.setOnClickListener(this);
        stringCode = holder.btnCode.getText().toString();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentItem.isSelected() & !selectionON) {
                    Intent intent = new Intent(mContext, CFDetail.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     // altrimenti non funziona :(
                    intent.putExtra("CF", currentItem);      // PARCELABLE
                    mContext.startActivity(intent);
                }else{
                    multipleSelection(currentItem, holder);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                multipleSelection(currentItem, holder);
                return true;
            }
        });
    }

    void deleteSelected() {
        for (int i = 0; i < selectedItem.size(); i++) {
            savedCF.remove(selectedItem.get(i));
            AppDatabase.getInstance(mContext).codiceFiscaleDAO().deleteCode(selectedItem.get(i));
            notifyItemRemoved(i);
        }
        adapterCallback.showHideItem(R.id.deleteAll);
    }

    private void multipleSelection(CodiceFiscaleEntity currentItem, Holder holder){
        adapterCallback.showHideItem(R.id.deleteAll);
        if (!currentItem.isSelected()) {
            currentItem.setSelected(true);
            selectedItem.add(currentItem);
        }else   {
            currentItem.setSelected(false);
            selectedItem.remove(currentItem);
        }
        if (preferenceManager.getTheme() == 1){
            holder.itemView.setBackgroundColor(currentItem.isSelected() ? Color.CYAN : ContextCompat.getColor(mContext, R.color.colorCardDark));
        }else{
            holder.itemView.setBackgroundColor(currentItem.isSelected() ? Color.CYAN : ContextCompat.getColor(mContext, R.color.colorCardLight));
        }
        selectionON = selectedItem.size() > 0;  /* parametro utilizzato per gestire
                                             la selezione multipla anche con l'OnClick.
                                             se la selezione multipla è in atto, allora anche la onClick aggiungerà elementi*/
    }
    private int getCardColor(){
        int colorId;
        if (preferenceManager.getTheme() == 1){
            colorId = ContextCompat.getColor(mContext, R.color.colorCardDark);
        }else{
            colorId = ContextCompat.getColor(mContext, R.color.colorCardLight);
        }
        return colorId;
    }

    public void selectAll() {
        for (int i = 0; i < savedCF.size(); i++) {
            savedCF.get(i).setSelected(true);
        }
    }

    @Override
    public int getItemCount() {
        return savedCF.size();
    }

    private CodiceFiscaleEntity getCodeAt(int position){
        return savedCF.get(position);
    }

    void deleteItem(int position, RecyclerView rcv) {
        lastDeleted = savedCF.get(position);
        lastDeletedPosition = position;
        AppDatabase.getInstance(mContext).codiceFiscaleDAO().deleteCode(lastDeleted);
        savedCF.remove(position);
        notifyItemRemoved(position);
        showUndoSnackBar(rcv);
    }

    private void showUndoSnackBar(RecyclerView recyclerView){

        Snackbar snackbar = Snackbar.make(recyclerView, R.string.itemRemoved, Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        savedCF.add(lastDeleted);
        AppDatabase.getInstance(mContext).codiceFiscaleDAO().saveNewCode(lastDeleted);
        notifyDataSetChanged();
        lastDeleted = null;

    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View v) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Codice Fiscale", stringCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mContext, R.string.cfCopied,Toast.LENGTH_SHORT).show();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCognome, tvData, tvLuogo, tvSesso;
        Button btnCode;
        Holder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvDetailNome);
            tvCognome = itemView.findViewById(R.id.tvDetailCognome);
            tvData = itemView.findViewById(R.id.tvDetailData);
            tvLuogo = itemView.findViewById(R.id.tvDetailLuogo);
            tvSesso = itemView.findViewById(R.id.tvDetailSesso);
            btnCode = itemView.findViewById(R.id.btnDetailCode);
        }
    }

    public interface AdapterCallback{
        void showHideItem(int id);
    }
}
