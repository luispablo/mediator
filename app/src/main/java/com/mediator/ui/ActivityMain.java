package com.mediator.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mediator.R;
import com.mediator.helpers.HelperAndroid;
import com.mediator.helpers.HelperParse;
import com.mediator.model.VideoEntry;
import com.mediator.tasks.TaskRefreshLocalDB;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import static com.mediator.ui.FragmentNavigationDrawer.DrawerItem;

public class ActivityMain extends ActionBarActivity
        implements FragmentNavigationDrawer.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    FragmentNavigationDrawer navDrawerFragment;
    FragmentNavigationDrawer.DrawerItem currentDrawerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navDrawerFragment = (FragmentNavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(this, ActivitySignIn.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;

        currentDrawerItem = DrawerItem.values()[position];

        switch (currentDrawerItem) {
            case MOVIES:
                fragment = FragmentMovies.newInstance();
                break;
            case TV_SHOWS:
                fragment = FragmentTVShows.newInstance();
                break;
            case SOURCES:
                fragment = FragmentSource.newInstance();
                break;
            case SETTINGS:
                fragment = FragmentSettings.newInstance();
                break;
            case SERVERS:
                fragment = FragmentVideoServers.newInstance();
                break;
            case RESCAN:
                rescanCollection();
                break;
        }

        if (fragment != null) {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    private void rescanCollection() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.message_rescanning_db);
        progressDialog.setMessage(getString(R.string.message_cleaning_local_db));
        progressDialog.show();

        final HelperParse helperParse = new HelperParse();
        helperParse.allVideoEntries(new HelperParse.CustomFindCallback<VideoEntry>() {
            @Override
            public void done(List<VideoEntry> videoEntries, ParseException e) {
                for (VideoEntry videoEntry : videoEntries)
                    helperParse.delete(videoEntry.getObjectId(), VideoEntry.class);
            }
        });

        TaskRefreshLocalDB taskRefreshLocalDB = new TaskRefreshLocalDB(this) {
            @Override
            public void onProgress(String message) {
                progressDialog.setMessage(message);
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        };
        taskRefreshLocalDB.execute();

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(HelperAndroid.getStringByName(this, currentDrawerItem.labelKey));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // This is to transfer the event to the fragments
        super.onActivityResult(requestCode, resultCode, data);
    }
}
