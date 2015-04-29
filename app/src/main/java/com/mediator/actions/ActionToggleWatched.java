package com.mediator.actions;

import android.app.Activity;

import com.mediator.helpers.HelperSnappyDB;
import com.mediator.model.VideoEntry;
import com.snappydb.SnappydbException;

import static com.mediator.helpers.TinyLogger.e;

/**
 * Created by luispablo on 27/04/15.
 */
public abstract class ActionToggleWatched implements IAction {

    @Override
    public boolean changedDB() {
        return true;
    }

    @Override
    public void execute(Activity activity, VideoEntry videoEntry) {
        videoEntry.setWatched(!videoEntry.isWatched());

        try {
            HelperSnappyDB helperSnappyDB = HelperSnappyDB.getSingleton(activity);
            helperSnappyDB.update(videoEntry);
            helperSnappyDB.close();
        } catch (SnappydbException e) {
            e(e);
        }
    }
}
