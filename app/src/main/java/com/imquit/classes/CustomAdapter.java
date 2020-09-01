package com.imquit.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imquit.R;
import com.imquit.model.UsageModel;

import java.util.ArrayList;

/**
 * Created by admininstrator on 7/1/18.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<UsageModel> usageModels;

    public CustomAdapter(Context context, ArrayList<UsageModel> usageModels) {
        this.context = context;
        this.usageModels = usageModels;
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
        return usageModels.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.usage_item, viewGroup, false);
        }
        ImageView imageView = view.findViewById(R.id.appImageView);
        TextView appName = view.findViewById(R.id.appNameTextView);
        TextView appPackageName = view.findViewById(R.id.appPackageTextView);
        TextView appTime = view.findViewById(R.id.appTimeTextView);
        UsageModel usageModel = (UsageModel) getItem(i);
        imageView.setImageDrawable(usageModel.getDrawable());
        appName.setText(usageModel.getAppName());
        appPackageName.setText(usageModel.getPackageName());
        appTime.setText(usageModel.getTime());
        return view;
    }
}
