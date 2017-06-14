package com.deezerapi.streammusic.view.transition;

import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.TransitionSet;

/**
 * Created by nishant on 19.05.17.
 */
public class ImageTransition extends TransitionSet {
    public ImageTransition() {
        setOrdering(ORDERING_TOGETHER);
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMaximumAngle(15);
        arcMotion.setMinimumHorizontalAngle(90);
        arcMotion.setMinimumVerticalAngle(90);
        
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform()).
                setDuration(300).setPathMotion(arcMotion);
    }

}
