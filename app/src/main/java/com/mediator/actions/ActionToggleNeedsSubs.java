package com.mediator.actions;

import android.app.Activity;

import com.mediator.helpers.HelperParse;
import com.mediator.model.VideoEntry;
import com.parse.ParseException;
import com.parse.SaveCallback;

/**
 * Created by luispablo on 26/04/15.
 */
public abstract class ActionToggleNeedsSubs implements IAction {

    @Override
    public void execute(Activity activity, VideoEntry videoEntry, final IActionCallback callback) {
        videoEntry.setNeedsSubs(!videoEntry.needsSubs());

        HelperParse helperParse = new HelperParse();
        helperParse.update(videoEntry, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (callback != null) callback.onDone(e == null);
            }
        });
    }
}
