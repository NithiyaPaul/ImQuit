package com.imquit.classes;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.imquit.activity.GraphActivity;
import com.imquit.activity.HomeActivity;

import java.util.List;

/**
 * Created by admininstrator on 9/1/18.
 */

public class MyBarDataSet extends BarDataSet {

    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if(getEntryForXIndex(index).getVal() < HomeActivity.min) // less than 95 green
            return mColors.get(0);
        else if(getEntryForXIndex(index).getVal() < HomeActivity.max) // less than 100 orange
            return mColors.get(1);
        else // greater or equal than 100 red
            return mColors.get(2);
    }

}