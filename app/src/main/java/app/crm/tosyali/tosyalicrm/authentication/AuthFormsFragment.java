package app.crm.tosyali.tosyalicrm.authentication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.crm.tosyali.tosyalicrm.R;
import app.crm.tosyali.tosyalicrm.tools.AuthPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthFormsFragment extends Fragment {


    public AuthFormsFragment() {
        // Required empty public constructor
    }
    private static ViewPager currentPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_forms, container, false);

        ViewPager pager = (ViewPager) view.findViewById(R.id.auth_pager_container);
        pager.setAdapter(new AuthPagerAdapter(getChildFragmentManager()));

        this.currentPager = pager;

        return view;
    }


    public static void switchToFragment(int position){
        if (currentPager != null)
            currentPager.setCurrentItem(position,true);
    }

}
