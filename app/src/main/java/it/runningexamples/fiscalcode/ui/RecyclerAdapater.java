/**
 * Adapter per la RecyclerView
 */

package it.runningexamples.fiscalcode.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.db.AppDatabase;
import it.runningexamples.fiscalcode.db.CodiceFiscaleEntity;
import it.runningexamples.fiscalcode.tools.ThemeUtilities;
import java.util.List;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> implements View.OnClickListener{
    List<CodiceFiscaleEntity> savedCF;
    CodiceFiscaleEntity lastDeleted,lastSelected;
    private Context mContext;
    private int lastDeletedPosition, counterSelected;
    private String stringCode;
    public boolean selectionON = false;
    private AdapterCallback adapterCallback;

    RecyclerAdapter(Context ctx, AdapterCallback callback) {
        this.mContext = ctx;
        this.adapterCallback = callback;
        this.savedCF = AppDatabase.getInstance(mContext).codiceFiscaleDAO().getAll();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final CodiceFiscaleEntity currentItem = savedCF.get(position);
        holder.tvNome.setText(currentItem.getNome());
        holder.tvCognome.setText(currentItem.getCognome());
        holder.tvData.setText(currentItem.getDataNascita());
        holder.tvLuogo.setText(currentItem.getLuogoNascita());
        if (currentItem.getGenere().equals("M")) {
            holder.tvSesso.setText(R.string.genereMaschio);
        } else {
            holder.tvSesso.setText(R.string.genereFemmina);
        }
        holder.btnCode.setText(currentItem.getFinalFiscalCode());
        holder.btnCode.setOnClickListener(this);
        stringCode = holder.btnCode.getText().toString();

        // Gestione del click sulla singola cardview per la selezione multipla o per mostrare
        // il codice a barre
        holder.itemView.setOnClickListener(view -> {
            if (!currentItem.isSelected() & !selectionON) {
                Intent intent = new Intent(mContext, CFDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CF", currentItem);      // PARCELABLE
                mContext.startActivity(intent);
            } else {
                multipleSelection(currentItem, holder, position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            multipleSelection(currentItem, holder, position);
            return true;
        });
    }

    // Metodo che permette di eliminare i codici scelti nella selezione multipla
    void deleteSelected() {
        int itemCount = savedCF.size();
        for (int i = 0; i < savedCF.size(); i++) {
            if (savedCF.get(i).isSelected()) {
                AppDatabase.getInstance(mContext).codiceFiscaleDAO().deleteCode(savedCF.get(i));
                savedCF.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeRemoved(i, itemCount);
                i--;
            }
        }
        counterSelected = 0;
        adapterCallback.counter(false, true);
        adapterCallback.showHideItem(true, false);
        adapterCallback.changeColorActionBar(ThemeUtilities.getActionColor(mContext));
        adapterCallback.disableSwipe(true);
    }

    // Metodo che permette di effettuare la selezione multipla
    private void multipleSelection(CodiceFiscaleEntity currentItem, Holder holder, int pos) {
        if (!currentItem.isSelected()) {
            if (counterSelected == 0) {
                adapterCallback.showHideItem(true, true);
            }else if (counterSelected>0){
                adapterCallback.showHideItem(false, false);
            }
            currentItem.setSelected(true);
            lastSelected = currentItem;
            adapterCallback.counter(true, false);
            ++counterSelected;
        } else {
            if (--counterSelected == 0) {
                adapterCallback.showHideItem(true, false);
            }
            currentItem.setSelected(false);
            adapterCallback.counter(false, false);
        }

        // Cambio colore al background della cardview selezionata mantenendo la forma arrotondata
        Drawable roundRectShape = holder.itemView.getBackground();
        roundRectShape.setTint(currentItem.isSelected() ? ThemeUtilities.getSelectionColor(mContext) : ThemeUtilities.getCardColor(mContext));
        holder.itemView.setBackground(roundRectShape);

        if (counterSelected == 0){
            adapterCallback.changeColorActionBar(ThemeUtilities.getActionColor(mContext));
            selectionON = false;
        }
        if (counterSelected > 0){
            adapterCallback.changeColorActionBar(ThemeUtilities.getActionSelectedColor(mContext));
            selectionON = true;
        }
        if (selectionON){
            adapterCallback.disableSwipe(false);
        }else {
            adapterCallback.disableSwipe(true);
        }
    }

    //Metodo che permette di eliminare un oggetto in seguito allo swipe
    void deleteItem(int position, RecyclerView rcv) {
        lastDeleted = savedCF.get(position);
        lastDeletedPosition = position;
        AppDatabase.getInstance(mContext).codiceFiscaleDAO().deleteCode(lastDeleted);
        savedCF.remove(position);
        notifyItemRemoved(position);
        showUndoSnackBar(rcv);
    }

    //Metodo che permette di condividere un codice dopo averlo selezionato
    public void shareSelected() {
        adapterCallback.getLastSelected(lastSelected);
    }

    @Override
    public int getItemCount() {
        return savedCF.size();
    }

    //Metodo che mostra una Snackbar dopo aver eliminato un elemento con lo swipe, e permette
    //tramite il pulsante "UNDO" di ripristinarlo
    private void showUndoSnackBar(RecyclerView recyclerView) {
        Snackbar snackbar = Snackbar.make(recyclerView, R.string.deleteElement, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undoElement, v -> undoDelete());
        snackbar.show();
    }

    //Metodo che permette di ripristinare l'ultimo elemento eliminato dopo uno swipe
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
    public void onClick(View v) { //Copia il codice negli appunti
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, stringCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mContext, R.string.clipboardCode, Toast.LENGTH_SHORT).show();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCognome, tvData, tvLuogo, tvSesso;
        Button btnCode;
        CardView cardView;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvDetailNome);
            tvCognome = itemView.findViewById(R.id.tvDetailCognome);
            tvData = itemView.findViewById(R.id.tvDetailData);
            tvLuogo = itemView.findViewById(R.id.tvDetailLuogo);
            tvSesso = itemView.findViewById(R.id.tvDetailSesso);
            btnCode = itemView.findViewById(R.id.btnDetailCode);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    // Interfaccia utilizzata per comunicare con la Saved Activity
    public interface AdapterCallback {
        void showHideItem(boolean delete, boolean share);
        void counter(boolean add, boolean setZero);
        void getLastSelected(CodiceFiscaleEntity lastSelected);
        void changeColorActionBar(int color);
        void disableSwipe(boolean state);
    }
}
