package it.runningexamples.fiscalcode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ColorDrawable redBackground, yellowBackground;
    private Drawable deleteIcon, starIcon;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView rcv;
    int intrinsicWidthDelete, intrinsicHeightDelete, intrinsicWidthStar, intrinsicHeightStar;

    SwipeCallback(RecyclerAdapter adapter, RecyclerView rcv) {
        super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        recyclerAdapter = adapter;
        this.rcv = rcv;
        redBackground = new ColorDrawable(Color.parseColor("#B80000"));
        yellowBackground = new ColorDrawable(Color.YELLOW);
        deleteIcon = ContextCompat.getDrawable(recyclerAdapter.getContext(), R.drawable.bin72);
        starIcon = ContextCompat.getDrawable(recyclerAdapter.getContext(), R.drawable.cityicon);
        intrinsicWidthDelete = deleteIcon.getIntrinsicWidth();
        intrinsicHeightDelete = deleteIcon.getIntrinsicHeight();
        intrinsicWidthStar= starIcon.getIntrinsicWidth();
        intrinsicHeightStar= starIcon.getIntrinsicHeight();
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
        int itemHeight = itemView.getHeight();
        int backgroundCornerOffset = 10;        //backgroundCornerOffset per mettere il background dietro la card

        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeightDelete) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeightDelete) / 8;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidthDelete;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeightDelete;

        int starIconTop = itemView.getTop() + (itemHeight - intrinsicHeightStar) / 2;
        int starIconMargin = (itemHeight - intrinsicHeightStar) / 8;
        int starIconLeft = itemView.getRight() - starIconMargin -intrinsicHeightStar;
        int starIconRight = itemView.getRight() - starIconMargin;
        int starIconBottom = deleteIconTop + intrinsicHeightStar;

        if (dX > 0){ //swipe verso destra
            starIcon.setBounds(starIconLeft, starIconTop, starIconRight, starIconBottom);
            yellowBackground.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset,
            itemView.getTop(), itemView.getRight(), itemView.getLeft());
        }
        if (dX < 0) { // swipe verso sinistra
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            redBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // nessuno swipe
            redBackground.setBounds(0, 0, 0, 0);
        }
        redBackground.draw(c);
        deleteIcon.draw(c);
    }
}
