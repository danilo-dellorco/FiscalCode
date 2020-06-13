package it.runningexamples.fiscalcode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    List<CodiceFiscaleEntity> savedCF;
    private Context mContext;

    RecyclerAdapter(Context ctx){
        this.mContext = ctx;
        this.savedCF = AppDatabase.getInstance(mContext).codiceFiscaleDAO().getAll();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false));

    }


    @Override
    public void onBindViewHolder (@NonNull Holder holder, final int position)  {
        final CodiceFiscaleEntity currentItem = savedCF.get(position);
        holder.tvNome.setText(currentItem.getNome());
        holder.tvCognome.setText(currentItem.getCognome());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
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

    public void deleteItem(int position) {
        AppDatabase.getInstance(mContext).codiceFiscaleDAO().deleteCode(getCodeAt(position));
        savedCF.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return mContext;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCognome;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvDetailNome);
            tvCognome = itemView.findViewById(R.id.tvDetailCognome);
        }
    }
}
