package com.github.hintofbasil.crabbler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

/**
 * Created by will on 14/06/16.
 */
public class ColorListAdapter<T> extends ArrayAdapter<T> {

    private int selected, max;
    private View selectedView = null;

    public ColorListAdapter(Context context, int resource, T[] data, int selected, int max) {
        super(context, resource, data);
        this.selected = selected;
        this.max = max;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setBackgroundResource(R.drawable.color_list);
        if(max >= 0 && position > max) {
            view.setBackgroundResource(R.drawable.colour_list_disabled);
        } else if(selected == position) {
            view.setBackgroundResource(R.color.questionBackground);
            selectedView = view;
        }
        return view;
    }

    public void removeDefault() {
        selected = -1;
        if(selectedView != null) {
            selectedView.setBackgroundResource(R.drawable.color_list);
        }
    }

    public void setMax(int max)
    {
        this.max = max;
    }
}
