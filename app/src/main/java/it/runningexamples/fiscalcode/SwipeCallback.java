package it.runningexamples.fiscalcode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ColorDrawable redBackground;
    private Drawable deleteIcon;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView rcv;

    public SwipeCallback(RecyclerAdapter adapter, RecyclerView rcv) {
        super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT);
        recyclerAdapter = adapter;
        this.rcv = rcv;
        redBackground = new ColorDrawable();
        deleteIcon = ContextCompat.getDrawable(recyclerAdapter.getContext(), R.drawable.bin);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        recyclerAdapter.deleteItem(position, rcv);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;        //backgroundCornerOffset per mettere il background dietro la card

        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

        if (dX < 0) { // swipe verso sinistra
            Log.d("CodiceFiscale", "swipe a sinistra");
            int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            redBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // nessuno swipe
            redBackground.setBounds(0, 0, 0, 0);
        }
        redBackground.draw(c);
        deleteIcon.draw(c);
    }

}
