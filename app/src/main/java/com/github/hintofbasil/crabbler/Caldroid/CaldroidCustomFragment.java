package com.github.hintofbasil.crabbler.Caldroid;

import android.os.Bundle;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by will on 13/06/16.
 */
public class CaldroidCustomFragment extends CaldroidFragment {

    int[] estimatedNewMoon = new int[3];
    int[] estimatedFullMoon = new int[3];

    // Dates taken from http://www.calendario-365.com.br/lua/fases-da-lua.html
    List<int[]> newMoonDates = new ArrayList<int[]>();
    List<int[]> fullMoonDates = new ArrayList<int[]>();

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        int resourceId = R.drawable.caldroid_cell_selected;
        Bundle args = getArguments();
        if(args.containsKey(Keys.CALDROID_BACKGROUND_RESOURCE)) {
            resourceId = args.getInt(Keys.CALDROID_BACKGROUND_RESOURCE);
        }
        getMoons(month, year);
        return new CaldroidCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData, resourceId, newMoonDates, fullMoonDates);
    }

    private void getMoons(int month, int year)
    {
        /* populate lists */

        try{
            InputStream is = getActivity().getApplicationContext().getAssets().open("moons_new.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] num = line.split(",");
                newMoonDates.add(new int[]{Integer.parseInt(num[0]), Integer.parseInt(num[1]), Integer.parseInt(num[2])});
            }

            is = getActivity().getApplicationContext().getAssets().open("moons_full.csv");
            reader = new BufferedReader(new InputStreamReader(is));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] num = line.split(",");
                fullMoonDates.add(new int[]{Integer.parseInt(num[0]), Integer.parseInt(num[1]), Integer.parseInt(num[2])});
            }
        } catch (IOException e) {Log.e("CaldroidCustomFragment", "Error: " + e);}

        /* Get algorithm expectations */

        boolean useMoonPhaseAlgorithm = true;
        List<int[]> estimatedNewMoons = new ArrayList<int[]>();
        List<int[]> estimatedFullMoons = new ArrayList<int[]>();

        for(int i=1; i<32; i++)
        {
            Calendar c = new GregorianCalendar(year, month, i);
            MoonPhase mp = new MoonPhase(c);
            if (mp.getPhaseIndex() == 0)
            {
                estimatedNewMoons.add(new int[]{i, month, year});
            }
            else if (mp.getPhaseIndex() == 4)
            {
                estimatedFullMoons.add(new int[]{i, month, year});
            }
        }
        estimatedNewMoon = estimatedNewMoons.get(estimatedNewMoons.size()-2);
        estimatedFullMoon = estimatedFullMoons.get(estimatedFullMoons.size()-2);

        /* If there are no values for the specified moon in the specified month,
        ** add the value from the algorithm to the list */

        for(int[] date : newMoonDates) {
            if (date[1] == month && date[2] == year) {
                useMoonPhaseAlgorithm = false;
            }
        }

        if (useMoonPhaseAlgorithm)
        {
            newMoonDates.add(estimatedNewMoon);
        }

        for(int[] date : fullMoonDates) {
            if (date[1] == month && date[2] == year) {
                useMoonPhaseAlgorithm = false;
            }
        }

        if (useMoonPhaseAlgorithm)
        {
            fullMoonDates.add(estimatedFullMoon);
        }

    }
}
