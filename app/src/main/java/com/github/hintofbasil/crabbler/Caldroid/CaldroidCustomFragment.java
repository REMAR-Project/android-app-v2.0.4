package com.github.hintofbasil.crabbler.Caldroid;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by will on 13/06/16.
 */
public class CaldroidCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CaldroidCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }
}
