package com.quantum.app.mycourse.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantum.app.mycourse.R;
import com.quantum.app.mycourse.fragment.DayCourseFragment;
import com.quantum.app.mycourse.fragment.GradePointFragment;
import com.quantum.app.mycourse.fragment.ScoreFragment;
import com.quantum.app.mycourse.fragment.TestFragment;
import com.quantum.app.mycourse.fragment.WeekCourseFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private boolean hasLogin;

    private int mNavItemId;
    private static final String NAV_ITEM_ID = "navItemId";
    ImageView coursrLogo;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(2000);
        getWindow().setExitTransition(explode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        hasLogin = prefs.getBoolean("has_login", false);
        //判断是否登陆
        if (!hasLogin) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
//        setupWindowAnimations();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.main_activity_day));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //判断之前点击的选项
        if (null == savedInstanceState) {
            pendingIntroAnimation = true;
            mNavItemId = R.id.item1;
            getFragmentManager().beginTransaction().replace(R.id.content_layout, new DayCourseFragment()).commit();
            setTitle(getString(R.string.main_activity_day));
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        navigationView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                navigationView.removeOnLayoutChangeListener(this);
                ImageView userLogo = (ImageView) findViewById(R.id.header_user_logo);
                userLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        drawerLayout.closeDrawers();
                    }
                });
                TextView userStatus = (TextView) findViewById(R.id.header_user_status);
                userStatus.setText("已登陆");
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mNavItemId = menuItem.getItemId();
                switch (mNavItemId) {
                    case R.id.item1:
//                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new DayCourseFragment()).commit();
                        DayCourseFragment dayCourseFragment = new DayCourseFragment();
                        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                        transaction1.replace(R.id.content_layout, dayCourseFragment);
                        transaction1.addToBackStack(null);
                        transaction1.commitAllowingStateLoss();
                        setTitle(getString(R.string.main_activity_day));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item2:
//                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new WeekCourseFragment()).commit();
                        WeekCourseFragment weekCourseFragment = new WeekCourseFragment();
                        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                        transaction2.replace(R.id.content_layout, weekCourseFragment);
                        transaction2.addToBackStack(null);
                        transaction2.commitAllowingStateLoss();
                        setTitle(getString(R.string.main_activity_week));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item3:
//                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new ScoreFragment()).commit();
                        ScoreFragment scoreFragment = new ScoreFragment();
                        FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
                        transaction3.replace(R.id.content_layout, scoreFragment);
                        transaction3.addToBackStack(null);
                        transaction3.commitAllowingStateLoss();
                        setTitle(getString(R.string.main_activity_grade));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item4:
//                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new GradePointFragment()).commit();
                        GradePointFragment gradePointFragment = new GradePointFragment();
                        FragmentTransaction transaction4 = getFragmentManager().beginTransaction();
                        transaction4.replace(R.id.content_layout, gradePointFragment);
                        transaction4.addToBackStack(null);
                        transaction4.commitAllowingStateLoss();
                        setTitle(getString(R.string.main_activity_point));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item5:
                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new TestFragment()).commit();
                        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.log_out:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
        navigationView.getMenu().findItem(mNavItemId).setChecked(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    MenuItem shareItem;
    private boolean pendingIntroAnimation;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        shareItem = menu.findItem(R.id.action_share);
//        shareItem.setActionView(R.layout.menu_item_view);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        int actionbarSize = (int) (56 * Resources.getSystem().getDisplayMetrics().density);
        toolbar.setTranslationY(-actionbarSize);
        toolbar.setTranslationY(-actionbarSize);
//        shareItem.getActionView().setTranslationY(-actionbarSize);
        toolbar.animate()
                .translationY(0)
                .setDuration(300)
                .setStartDelay(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        coursrLogo.animate()
//                                .translationY(0)
//                                .setDuration(300)
//                                .setStartDelay(300);
                    }
                })
                .start();
//        coursrLogo.animate()
//                .translationY(0)
//                .setDuration(300)
//                .setStartDelay(1400);
//        shareItem.getActionView().animate()
//                .translationY(0)
//                .setDuration(300)
//                .setStartDelay(500)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
////                        startContentAnimation();
//                    }
//                })
//                .start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (mNavItemId != R.id.item1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

}
