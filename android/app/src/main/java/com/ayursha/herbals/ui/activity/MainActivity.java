package com.ayursha.herbals.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ayursha.herbals.R;
import com.ayursha.herbals.ui.fragment.AboutFragment;
import com.ayursha.herbals.ui.fragment.HomeFragment;
import com.ayursha.herbals.utils.AppConstants;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
		AboutFragment.OnFragmentInteractionListener
		{

	@BindView(R.id.toolbar)
    Toolbar toolbar;

	@BindView(R.id.nav_view)
	NavigationView navigationView;

	@BindView(R.id.drawer_layout)
	DrawerLayout drawer;

	//@BindView(R.id.fab)
    //FloatingActionButton fab;

	// index to identify current nav menu item
	public static int navItemIndex = 0;

	// tags used to attach the fragments
	private static final String TAG_HOME = "home";
	private static final String TAG_PHOTOS = "aboutus";
	private static final String TAG_MOVIES = "rate us";
	public static String CURRENT_TAG = TAG_HOME;

	// toolbar titles respected to selected nav menu item
	private String[] activityTitles;

	// flag to load home fragment when user presses back key
	private boolean shouldLoadHomeFragOnBackPress = true;
	private Handler mHandler;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.bind(this);
		// initializing navigation menu
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// load toolbar titles from string resources
		activityTitles = getResources().getStringArray(R.array.titles);

        mHandler = new Handler();

        setUpNavigationView();

		if (savedInstanceState == null) {
			navItemIndex = 0;
			CURRENT_TAG = TAG_HOME;
			loadHomeFragment();
		}
		/*bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				Fragment fragment = null;
				switch (item.getItemId()) {
					case R.id.action_home:
						fragment = HomeFragment.newInstance();
						break;
					case R.id.action_favor:
						fragment = FavouritesFragment.newInstance();
						break;
					case R.id.action_about:
						fragment = AboutFragment.newInstance();
						break;
					case R.id.action_setting:
						fragment = SettingsFragment.newInstance();
						break;
				}
				if (fragment != null) {
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.replace(R.id.frame_layout, fragment);
					fragmentTransaction.commit();
				}
				return true;
			}
		});*/

	}

	private void loadHomeFragment() {
		// selecting appropriate nav menu item
		selectNavMenu();

		// set toolbar title
		setToolbarTitle();

		// if user select the current navigation menu again, don't do anything
		// just close the navigation drawer
		if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
			drawer.closeDrawers();

			// show or hide the fab button
			toggleFab();
			return;
		}

		// Sometimes, when fragment has huge data, screen seems hanging
		// when switching between navigation menus
		// So using runnable, the fragment is loaded with cross fade effect
		// This effect can be seen in GMail app
		Runnable mPendingRunnable = new Runnable() {
			@Override
			public void run() {
				// update the main content by replacing fragments
				Fragment fragment = getHomeFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out);
				fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG);
				fragmentTransaction.commitAllowingStateLoss();
			}
		};

		// If mPendingRunnable is not null, then add to the message queue
		if (mPendingRunnable != null) {
			mHandler.post(mPendingRunnable);
		}

		// show or hide the fab button
		toggleFab();

		//Closing drawer on item click
		drawer.closeDrawers();

		// refresh toolbar menu
		invalidateOptionsMenu();

	}

	private Fragment getHomeFragment() {
		switch (navItemIndex) {
			case 0:
				// home
				HomeFragment homeFragment = new HomeFragment();
				return homeFragment;
			case 1:
				// photos
				AboutFragment aboutFragment = new AboutFragment();
				return aboutFragment;
			/*case 2:
				// movies fragment
				FavouritesFragment moviesFragment = new FavouritesFragment();
				return moviesFragment;*/
			default:
				return new HomeFragment();
		}
	}

	private void setToolbarTitle() {
		getSupportActionBar().setTitle(activityTitles[navItemIndex]);
	}

	private void selectNavMenu() {
		navigationView.getMenu().getItem(navItemIndex).setChecked(true);
	}

	private void setUpNavigationView() {
		//Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

			// This method will trigger on item Click of navigation menu
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {

				//Check to see which item was being clicked and perform appropriate action
				switch (menuItem.getItemId()) {
					//Replacing the main content with ContentFragment Which is our Inbox View;
					case R.id.action_home:
						navItemIndex = 0;
						CURRENT_TAG = TAG_HOME;
						break;
					case R.id.action_about:
						navItemIndex = 1;
						CURRENT_TAG = TAG_PHOTOS;
						break;
					case R.id.action_rate:
						//navItemIndex = 2;
						//CURRENT_TAG = TAG_MOVIES;
                        Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.FB_REVIEWS_URL));
                        startActivity(fbIntent);
                        drawer.closeDrawers();
                        return true;
					/*case R.id.nav_about_us:
						// launch new intent instead of loading fragment
						startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
						drawer.closeDrawers();
						return true;*/
					default:
						navItemIndex = 0;
				}

				//Checking if the item is in checked state or not, if not make it in checked state
				if (menuItem.isChecked()) {
					menuItem.setChecked(false);
				} else {
					menuItem.setChecked(true);
				}
				menuItem.setChecked(true);

				loadHomeFragment();

				return true;
			}
		});


		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

			@Override
			public void onDrawerClosed(View drawerView) {
				// Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
				super.onDrawerOpened(drawerView);
			}
		};

		//Setting the actionbarToggle to drawer layout
		drawer.setDrawerListener(actionBarDrawerToggle);

		//calling sync state is necessary or else your hamburger icon wont show up
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawers();
			return;
		}

		// This code loads home fragment when back key is pressed
		// when user is in other fragment than home
		if (shouldLoadHomeFragOnBackPress) {
			// checking if user is on other navigation menu
			// rather than home
			if (navItemIndex != 0) {
				navItemIndex = 0;
				CURRENT_TAG = TAG_HOME;
				loadHomeFragment();
				return;
			}
		}

		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		/*// show menu only when home fragment is selected
		if (navItemIndex == 0) {
			getMenuInflater().inflate(R.menu.main, menu);
		}

		// when fragment is notifications, load the menu created for notifications
		if (navItemIndex == 3) {
			getMenuInflater().inflate(R.menu.notifications, menu);
		}*/
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_logout) {
			Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
			return true;
		}

		// user is in notifications fragment
		// and selected 'Mark all as Read'
		if (id == R.id.action_mark_all_read) {
			Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
		}

		// user is in notifications fragment
		// and selected 'Clear All'
		if (id == R.id.action_clear_notifications) {
			Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
		}*/

		return super.onOptionsItemSelected(item);
	}

	// show or hide the fab
	private void toggleFab() {
		//if (navItemIndex == 0)
		//	fab.show();
		//else
		//	fab.hide();
	}


}