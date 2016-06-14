package com.github.hintofbasil.crabbler.Caldroid;

import android.os.Bundle;

import com.github.hintofbasil.crabbler.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by will on 13/06/16.
 */
public class CaldroidCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        int resourceId = R.layout.custom_caldroid_cell;
        Bundle args = getArguments();
        if(args.containsKey("layoutResource")) {
            resourceId = args.getInt("layoutResource");
        }
        return new CaldroidCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData, resourceId);
    }
}
