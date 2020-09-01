package com.imquit.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.imquit.R;
import com.imquit.model.UsageModel;

import java.util.ArrayList;


public class DeleteSelectionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UsageModel> usageModels;
    private ArrayList<CheckBox> checkBoxes;
    public static ArrayList<String> deletepackageNames;
    int count;
    public DeleteSelectionAdapter(Context context, ArrayList<UsageModel> usageModels) {
        this.context = context;
        this.usageModels = usageModels;
        checkBoxes = new ArrayList<>();
        deletepackageNames = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return usageModels.size();
    }

    @Override
    public Object getItem(int i) {
        return usageModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.app_select_item, viewGroup, false);
        }
        count = i;
        ImageView appIconImageView = view.findViewById(R.id.appIconImageView);
        TextView appNameSelectionTextView = view.findViewById(R.id.appNameSelectionTextView);
        CheckBox checkBox = view.findViewById(R.id.checkBoxSelection);

        checkBoxes.add(checkBox);
        final UsageModel usageModel = (UsageModel) getItem(i);
        if (usageModel.isChecked()) {
            checkBox.setChecked(true);
        }   else {
            checkBox.setChecked(false);
        }
        appIconImageView.setImageDrawable(usageModel.getDrawable());
        appNameSelectionTextView.setText(usageModel.getAppName());
        checkBoxes.get(i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxes.get(i).isChecked()) {
                    if (deletepackageNames.contains(usageModel.getPackageName())) {
                        Log.e("size", deletepackageNames.size() + "");
                        usageModel.setChecked(true);
                    } else {
                        deletepackageNames.add(usageModel.getPackageName());
                        Log.e("size", deletepackageNames.size() + "");
                        usageModel.setChecked(true);
                    }
                } else {
                    deletepackageNames.remove(usageModel.getPackageName());
                    Log.e("size", deletepackageNames.size() + "");
                    usageModel.setChecked(false);
                }
            }
        });
        return view;
    }
}
