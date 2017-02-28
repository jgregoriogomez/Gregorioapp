package com.example.gregorio.gregorioapp.views;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gregorio.gregorioapp.LoginActivity;
import com.example.gregorio.gregorioapp.R;
import com.example.gregorio.gregorioapp.views.fragments.HomeFragment;
import com.example.gregorio.gregorioapp.views.fragments.ProfileFragment;
import com.example.gregorio.gregorioapp.views.fragments.SearchFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        //esto valida que sea un usuario valido en facebook
        /*if(AccessToken.getCurrentAccessToken()==null){
            goLoginScreen();
        }*/

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            goLoginScreen();
        }

        BottomBar bottomBar = (BottomBar) this.findViewById(R.id.bottombar);
        bottomBar.setDefaultTab(R.id.home);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        showFragment(homeFragment);
                        break;
                    case R.id.search:
                        SearchFragment searchFragment = new SearchFragment();
                        showFragment(searchFragment);
                        break;
                    case R.id.profile:
                        ProfileFragment profileFragment = new ProfileFragment();
                        showFragment(profileFragment);
                        break;
                    case R.id.logout:
                        logout();
                        break;
                }
            }

            private void showFragment (Fragment fragment){
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container,fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .addToBackStack(null).commit();
            }
        });
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
            Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
