package app.crm.tosyali.tosyalicrm.crm_dashboard;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import app.crm.tosyali.tosyalicrm.R;
import app.crm.tosyali.tosyalicrm.models.Interview;
import app.crm.tosyali.tosyalicrm.tools.InterviewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private FirebaseAuth mAuth;

    public DashboardFragment() {
        // Required empty public constructor
    }

    private DatabaseReference mDatabase;

    private BottomNavigationView navigationView;
    private RecyclerView recyclerView;
    private ImageView iconSort;
    private SearchView searchView;

    private ArrayList<Interview> interviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("interviews/"+mAuth.getCurrentUser().getUid());

        initViews(view);
        uiEvents();

        return view;
    }

    private void initViews(View view) {

        navigationView = (BottomNavigationView) view.findViewById(R.id.menu_navigation);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        searchView = (SearchView) view.findViewById(R.id.search_view);
        iconSort = (ImageView) view.findViewById(R.id.icon_sort);

        interviews = new ArrayList<>();
        recyclerView.setAdapter(new InterviewAdapter(getContext(),interviews));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void uiEvents() {

        LoadAllItems();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_logout:
                        mAuth.signOut();
                        break;
                    case R.id.action_home:
                        Toast.makeText(getContext(),"Home Clicked", Toast.LENGTH_SHORT).show();
                        LoadAllItems();
                        break;
                    case R.id.action_add:
                        Fragment fragment = new InterviewFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame,fragment);
                        fragmentTransaction.addToBackStack(InterviewFragment.class.getSimpleName());
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                interviews.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                mDatabase.orderByChild("name")
                        .startAt(query)
                        .endAt(query+"\uf8ff")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                interviews.add(dataSnapshot.getValue(Interview.class));
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        iconSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interviews.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                mDatabase.orderByChild("date").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        interviews.add(dataSnapshot.getValue(Interview.class));
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void LoadAllItems(){
        interviews.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                interviews.add(dataSnapshot.getValue(Interview.class));
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
