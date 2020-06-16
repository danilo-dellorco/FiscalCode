package it.runningexamples.fiscalcode.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.db.AppDatabase;
import it.runningexamples.fiscalcode.db.CodiceFiscaleEntity;

@SuppressWarnings("HardCodedStringLiteral")
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> implements View.OnClickListener{
    List<CodiceFiscaleEntity> savedCF;
    CodiceFiscaleEntity lastDeleted;
    private Context mContext;
    private int lastDeletedPosition;
    String stringCode;

    RecyclerAdapter(Context ctx){
        this.mContext = ctx;
        this.savedCF = AppDatabase.getInstance(mContext).codiceFiscaleDAO().getAll();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card, parent, false));
    }


    @Override
    public void onBindViewHolder (@NonNull Holder holder, final int position)  {
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
                Intent intent = new Intent(mContext, CFDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     // altrimenti non funziona :(
                intent.putExtra("CF", currentItem);      // PARCELABLE
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedCF.size();
    }

    private CodiceFiscaleEntity getCodeAt(int position){
        return savedCF.get(position);
    }

    public void deleteItem(int position, RecyclerView rcv) {
        lastDeleted = savedCF.get(position);
        lastDeletedPosition = position;
        AppDatabase.getInstance(mContext).codiceFiscaleDAO().deleteCode(lastDeleted);
        savedCF.remove(position);
        notifyItemRemoved(position);
        showUndoSnackBar(rcv);
    }

    private void showUndoSnackBar(RecyclerView recyclerView){

        Snackbar snackbar = Snackbar.make(recyclerView, R.string.deleteElement, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undoElement, new View.OnClickListener() {
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
        ClipData clip = ClipData.newPlainText(null, stringCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mContext,R.string.clipboardCode,Toast.LENGTH_SHORT).show();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCognome, tvData, tvLuogo, tvSesso;
        Button btnCode;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvDetailNome);
            tvCognome = itemView.findViewById(R.id.tvDetailCognome);
            tvData = itemView.findViewById(R.id.tvDetailData);
            tvLuogo = itemView.findViewById(R.id.tvDetailLuogo);
            tvSesso = itemView.findViewById(R.id.tvDetailSesso);
            btnCode = itemView.findViewById(R.id.btnDetailCode);
        }
    }

}
