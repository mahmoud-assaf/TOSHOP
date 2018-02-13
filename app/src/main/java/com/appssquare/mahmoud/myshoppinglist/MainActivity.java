package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
  public   DrawerLayout drawerLayout;
    public  ActionBarDrawerToggle mDrawerToggle;
    public ListView menuListView;
    public  Toolbar toolbar;
    public MenuListAdapter menuAdapter;
    View sideview, contentView;
    boolean isUser=false;
    TextView userTitle,toolBarTitle;
   // ImageView toolbarIcon;
    private TabLayout tabLayout;
    public ViewPager viewPager;
    ViewPagerAdapter adapter;
    int tabIcons[];
    Menu toolbarmenu;
    boolean isitemsfragment=true;
    Preferences preferences;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mAuth = FirebaseAuth.getInstance();
       preferences=Preferences.getInstance(this);
       if(!preferences.getKey("userId").equals(""))
        isUser=true;
        db=new DatabaseHelper(this);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle=(TextView)findViewById(R.id.toolbartext);
        //toolbarIcon=(ImageView)findViewById(R.id.toolbaricon);
        setSupportActionBar(toolbar);
      // getSupportActionBar().setHomeButtonEnabled(true);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView menu=(ImageView) toolbar.findViewById(R.id.toolbaricon);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawe();
            }
        });


        prepareNavigationDrawer();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab, null, false);

        LinearLayout linearLayoutOne = (LinearLayout) headerView.findViewById(R.id.ll);
        LinearLayout linearLayout2 = (LinearLayout) headerView.findViewById(R.id.ll2);
        LinearLayout linearLayout3 = (LinearLayout) headerView.findViewById(R.id.ll3);

        tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
        tabLayout.getTabAt(1).setCustomView(linearLayout2);
        tabLayout.getTabAt(2).setCustomView(linearLayout3);
        tabIcons=new int[]{R.drawable.list,R.drawable.cart,R.drawable.bell};
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here
                toolBarTitle.setText(adapter.getPageTitle(tab.getPosition()));
               // toolbarIcon.setBackgroundResource(tabIcons[tab.getPosition()]);
                if(tab.getPosition()>0){
                    isitemsfragment=false;
                }else{
                    isitemsfragment=true;
                }

                if(tab.getPosition()==2) {

                    toolbarmenu.getItem(0).setVisible(false); // here pass the index of save menu item

                }
                    else {


                    toolbarmenu.getItem(0).setVisible(true);

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e("mainactivity attatch", "attachBaseContext");
    }



    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShoppingListFragment(), getString(R.string.shopping_list));
        adapter.addFragment(new StoresFragment(), getString(R.string.stores));
        adapter.addFragment(new ReminderFragment(), getString(R.string.reminders));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setToolBarTitle(adapter.getPageTitle(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setToolBarTitle(adapter.getPageTitle(0));
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.START)){

            drawerLayout.closeDrawer(Gravity.START) ;
            mDrawerToggle.syncState();
        return;
        }
        super.onBackPressed();

    }

    public  void setToolBarTitle(String title){
        toolBarTitle.setText(title);
    }

    public void prepareNavigationDrawer(){



        sideview = findViewById(R.id.sideMenu);
        contentView = findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawer, float slideOffset) {
                                               float   rtlslideOffset;
                                                if(preferences.getKey("language_key").equals("ar"))
                                                   rtlslideOffset=-slideOffset;
                                                else
                                                    rtlslideOffset=slideOffset;
                                               contentView.setX(sideview.getWidth() * rtlslideOffset);
                                               RelativeLayout.LayoutParams lp =
                                                       (RelativeLayout.LayoutParams) contentView.getLayoutParams();
                                               lp.height = drawer.getHeight() -
                                                       (int) (drawer.getHeight() * slideOffset * 0.3f);
                                               lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                               contentView.setLayoutParams(lp);
                                           }

                                           @Override
                                           public void onDrawerClosed(View drawerView) {
                                           }
                                       }
        );
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,
                R.string.opened,  /* "open drawer" description */
                R.string.closed  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
               // toolbar.setTitle(items.get(currentPos).title);
                mDrawerToggle.syncState();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerToggle.syncState();

            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();

        //prepare menu

        final MenuItem[] menuListUser=new MenuItem[3];   //there is a menu for guest or logged in user .prepared here after checking prefs
        final MenuItem[] menuListGuest=new MenuItem[3];

        menuListGuest[0]=new MenuItem(0,getString(R.string.settings),R.drawable.settings,SettingsActivity.class);
        menuListGuest[1]=new MenuItem(1,getString(R.string.aboutus),R.drawable.aboutus,AboutUsActivity.class);
        menuListGuest[2]=new MenuItem(1,getString(R.string.sign_up),R.drawable.signup,SignUpActivity.class);


        menuListUser[0]=new MenuItem(0,getString(R.string.settings),R.drawable.settings,SettingsActivity.class);
        menuListUser[1]=new MenuItem(1,getString(R.string.aboutus),R.drawable.aboutus,AboutUsActivity.class);
        menuListUser[2]=new MenuItem(2,getString(R.string.log_out),R.drawable.logout,LogoutActivity.class);

       final MenuItem[] menu;
        userTitle=(TextView) findViewById(R.id.userTitle);

        if(isUser) {
           menu = menuListUser;
            String displayName=mAuth.getCurrentUser().getDisplayName();
            if(displayName==null)
                displayName=mAuth.getCurrentUser().getEmail();
           userTitle.setText(displayName);
       }
       else {
           menu = menuListGuest;
           userTitle.setText("Guest Account");
       }

        menuListView= (ListView) findViewById(R.id.menu_list);
        menuAdapter=new MenuListAdapter(this,menu);
        menuListView.setAdapter(menuAdapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              //  Toast.makeText(getApplicationContext(), "You Clicked "+menuList[position].title, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),menu[position].intentActivity));
                drawerLayout.closeDrawer(Gravity.START) ;
            }
        });




    }

    public  void toggleDrawe(){

        if(drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START) ;
        else
            drawerLayout.openDrawer(Gravity.START);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(Gravity.START))
                drawerLayout.closeDrawer(Gravity.START) ;
            else
                drawerLayout.openDrawer(Gravity.START);
        }
        else if(item.getItemId()==R.id.addNew){
            if(!isUser){
                Toast.makeText(this, R.string.signup_first,Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }
            if(isitemsfragment) {
                if (db.getUserShops(preferences.getKey("userId")).size() < 1) {
                    Toast.makeText(this, R.string.no_stores_toast,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,AddShopActivity.class));


                } else
                    startActivity(new Intent(MainActivity.this, AddItemActivity.class));


            } else
                startActivity(new Intent(MainActivity.this,AddShopActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbarmenu=menu;
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
