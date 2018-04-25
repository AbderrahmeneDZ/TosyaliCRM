package app.crm.tosyali.tosyalicrm.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.crm.tosyali.tosyalicrm.R;
import app.crm.tosyali.tosyalicrm.crm_dashboard.DetailFragment;
import app.crm.tosyali.tosyalicrm.models.Interview;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Interview> interviews;

    public InterviewAdapter(Context context, ArrayList<Interview> interviews){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.interviews = interviews;
    }


    @NonNull
    @Override
    public InterviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_interview,parent,false);
        InterviewViewHolder holder = new InterviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewViewHolder holder, final int position) {

        holder.getTextName().setText(interviews.get(position).getName());
        holder.getTextAddress().setText(interviews.get(position).getAddress());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, hh:mm");
        holder.getTextDate().setText(formatter.format(new Date(interviews.get(position).getDate())));

        holder.getContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = (FragmentActivity) context;
                Fragment fragment = new DetailFragment(interviews.get(position));
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,fragment);
                fragmentTransaction.addToBackStack(DetailFragment.class.getSimpleName());
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return interviews.size();
    }
}
