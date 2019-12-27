package com.example.supercalculator.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.supercalculator.MainAdapter;
import com.example.supercalculator.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Formulas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Formulas#newInstance} factory method to
 * create an instance of this fragment.
 */

//раздел "Формулы"
public class Formulas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout formulasLL;

    private OnFragmentInteractionListener mListener;

    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<Drawable>> listItem;

    MainAdapter adapter;

    public Formulas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Formulas.
     */
    // TODO: Rename and change types and number of parameters
    public static Formulas newInstance(String param1, String param2) {
        Formulas fragment = new Formulas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_formulas, container, false);

        formulasLL = v.findViewById(R.id.formulasLL);

        expandableListView = v.findViewById(R.id.expandable_listview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(getContext(), listGroup, listItem);
        expandableListView.setAdapter(adapter);
        setListData();

        //чтение значения строк в текстовом файле "settings"
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                if (temp==1) {
                    if (line.contains("d"))
                        formulasLL.setBackgroundColor(Color.parseColor("#252525"));
                    if (line.contains("w"))
                        formulasLL.setBackgroundColor(Color.WHITE);
                }
                temp++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    //установка значений для выпадающих списков
    private void setListData() {
        listGroup.add(getString(R.string.Powers1));
        listGroup.add(getString(R.string.Logs));
        listGroup.add(getString(R.string.Derivs));
        listGroup.add(getString(R.string.Integs));
        listGroup.add(getString(R.string.Trigs));
        listGroup.add(getString(R.string.Roots));

        Drawable array;

        List<Drawable> list1 = new ArrayList<>();
        array = getResources().getDrawable(R.drawable.powsD);
        list1.add(array);

        List<Drawable> list2 = new ArrayList<>();
        array = getResources().getDrawable(R.drawable.logsD);
        list2.add(array);

        List<Drawable> list3 = new ArrayList<>();
        array = getResources().getDrawable(R.drawable.dersD);
        list3.add(array);

        List<Drawable> list4 = new ArrayList<>();
        array = getResources().getDrawable(R.drawable.intsD);
        list4.add(array);

        List<Drawable> list5 = new ArrayList<>();
        array = getResources().getDrawable(R.drawable.trigsD);
        list5.add(array);

        List<Drawable> list6 = new ArrayList<>();
        array = getResources().getDrawable(R.drawable.rootsD);
        list6.add(array);

        listItem.put(listGroup.get(0), list1);
        listItem.put(listGroup.get(1), list2);
        listItem.put(listGroup.get(2), list3);
        listItem.put(listGroup.get(3), list4);
        listItem.put(listGroup.get(4), list5);
        listItem.put(listGroup.get(5), list6);

        adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
