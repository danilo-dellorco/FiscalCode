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

class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    List<CodiceFiscale> savedCF;
    private Context mContext;

    Adapter(List<CodiceFiscale> list, Context ctx){
        this.savedCF = list;
        this.mContext = ctx;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cf_card, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder (@NonNull Holder holder, final int position)  {
        final CodiceFiscale currentItem = savedCF.get(position);
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

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCognome;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvDetailNome);
            tvCognome = itemView.findViewById(R.id.tvDetailCognome);
        }
    }
}
