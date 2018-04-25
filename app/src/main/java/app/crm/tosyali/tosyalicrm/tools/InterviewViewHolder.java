package app.crm.tosyali.tosyalicrm.tools;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import app.crm.tosyali.tosyalicrm.R;

public class InterviewViewHolder extends RecyclerView.ViewHolder {

    private TextView textName, textAddress, textDate;
    private CardView container;


    public InterviewViewHolder(View itemView) {
        super(itemView);
        textName = (TextView)itemView.findViewById(R.id.text_item_name);
        textAddress = (TextView)itemView.findViewById(R.id.text_item_address);
        textDate = (TextView)itemView.findViewById(R.id.text_item_date);
        container = (CardView) itemView.findViewById(R.id.item_container);
    }

    public TextView getTextAddress() {
        return textAddress;
    }

    public TextView getTextDate() {
        return textDate;
    }

    public TextView getTextName() {
        return textName;
    }

    public CardView getContainer() {
        return container;
    }
}

