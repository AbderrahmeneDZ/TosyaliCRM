package app.crm.tosyali.tosyalicrm.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.crm.tosyali.tosyalicrm.authentication.LoginFragment;
import app.crm.tosyali.tosyalicrm.authentication.RegisterFragment;

public class AuthPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;

    public AuthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0) fragment = new LoginFragment();
        else fragment = new RegisterFragment();

        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
