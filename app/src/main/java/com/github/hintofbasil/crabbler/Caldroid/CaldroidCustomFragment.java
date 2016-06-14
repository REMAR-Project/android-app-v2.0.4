package com.github.hintofbasil.crabbler.Caldroid;

import android.os.Bundle;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by will on 13/06/16.
 */
public class CaldroidCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        int resourceId = R.drawable.caldroid_cell_selected;
        Bundle args = getArguments();
        if(args.containsKey(Keys.CALDROID_BACKGROUND_RESOURCE)) {
            resourceId = args.getInt(Keys.CALDROID_BACKGROUND_RESOURCE);
        }
        return new CaldroidCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData, resourceId);
    }
}
