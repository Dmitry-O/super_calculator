package com.example.supercalculator.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supercalculator.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link About.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link About#newInstance} factory method to
 * create an instance of this fragment.
 */

//раздел "О программе"
public class About extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView emailText, tv, tv5, tv34, tv35, tv36, tv37;
    LinearLayout aboutLL;
    ImageView logo;
    Button checkUpdates;

    ClipboardManager clipboardManager;
    ClipData clipData;

    public About() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment About.
     */
    // TODO: Rename and change types and number of parameters
    public static About newInstance(String param1, String param2) {
        About fragment = new About();
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
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        emailText = v.findViewById(R.id.emailText);
        aboutLL = v.findViewById(R.id.aboutLL);
        tv = v.findViewById(R.id.textView);
        tv5 = v.findViewById(R.id.textView5);
        tv34 = v.findViewById(R.id.textView34);
        tv35 = v.findViewById(R.id.textView35);
        tv36 = v.findViewById(R.id.textView36);
        tv37 = v.findViewById(R.id.textView37);
        logo = v.findViewById(R.id.imageView3);

        checkUpdates = v.findViewById(R.id.checkUpdates);

        //чтение значения строк в текстовом файле "settings"
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                if (temp==1) {
                    if (line.contains("d")) {
                        aboutLL.setBackgroundColor(Color.parseColor("#252525"));
                        tv.setTextColor(Color.WHITE);
                        tv5.setTextColor(Color.WHITE);
                        tv34.setTextColor(Color.WHITE);
                        tv35.setTextColor(Color.WHITE);
                        tv36.setTextColor(Color.WHITE);
                        tv37.setTextColor(Color.WHITE);
                        logo.setBackgroundColor(Color.parseColor("#252525"));
                        checkUpdates.setBackgroundTintList(getResources().getColorStateList(R.color.buttonBackground));
                        checkUpdates.setTextColor(Color.BLACK);
                    }
                    if (line.contains("w")) {
                        aboutLL.setBackgroundColor(Color.WHITE);
                        tv.setTextColor(Color.BLACK);
                        tv5.setTextColor(Color.BLACK);
                        tv34.setTextColor(Color.BLACK);
                        tv35.setTextColor(Color.BLACK);
                        tv36.setTextColor(Color.BLACK);
                        tv37.setTextColor(Color.BLACK);
                        logo.setBackgroundColor(Color.WHITE);
                        checkUpdates.setBackgroundTintList(getResources().getColorStateList(R.color.someColor));
                        checkUpdates.setTextColor(Color.WHITE);
                    }
                }
                temp++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                    //обратная связь с разработчиком
                    if (view.getId() == R.id.emailText) {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dimasic9420@gmail.com"});
                        email.setType("message/rfc822");
                        getContext().startActivity(Intent.createChooser(email, getString(R.string.about_choose_app)));
                    }
                    //проверка обновлений приложения
                    if (view.getId()==R.id.checkUpdates) {
                        Intent intent;
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id=" + "com.google.android.gm"));
                        getContext().startActivity(intent);
                    }
                }
                return true;
            }
        };
        emailText.setOnTouchListener(touchListener);
        checkUpdates.setOnTouchListener(touchListener);

        return v;
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
