package app.crm.tosyali.tosyalicrm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.crm.tosyali.tosyalicrm.authentication.AuthFormsFragment;
import app.crm.tosyali.tosyalicrm.crm_dashboard.DashboardFragment;

import static app.crm.tosyali.tosyalicrm.tools.AuthFunctions.UpdateUI;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                boolean userExist = (firebaseAuth.getCurrentUser() != null);
                Log.i("onStateChanged","user exist -> "+(!userExist));
                openFragment(userExist);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UpdateUI(currentUser);
    }


    private void openFragment(boolean userExist){
        Fragment fragment = userExist ? new DashboardFragment() : new AuthFormsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment);
        fragmentTransaction.commit();
    }

}
