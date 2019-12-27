package com.example.supercalculator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

//адаптер для выпадающих списков в разделе "Формулы"
public class MainAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> listGroup;
    HashMap<String, List<Drawable>> listItem;

    public MainAdapter(Context context, List<String> listGroup, HashMap<String, List<Drawable>> listItem) {
        this.context=context;
        this.listGroup=listGroup;
        this.listItem=listItem;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.listItem.get(this.listGroup.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listGroup.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.listItem.get(this.listGroup.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String group = (String) getGroup(i);

        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView textView = view.findViewById(R.id.list_parent);
        textView.setText(group);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Drawable child = (Drawable) getChild(i, i1);

        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }

        ImageView imageView = view.findViewById(R.id.list_child);
        imageView.setImageDrawable(child);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
