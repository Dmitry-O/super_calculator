package com.example.supercalculator.ui;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supercalculator.MainActivity;
import com.example.supercalculator.NotificationReciever;
import com.example.supercalculator.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Scanner;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link History.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */

//раздел "История"
public class History extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MainActivity main;
    ScrollView sv;

    LinearLayout scrollView, historyLL;
    ImageButton clearAll, copyAllBut;
    View v;
    TextView tempTextV, copyTV;

    String copiedStr="";

    int idExp=0, idResult=0;

    File file;

    private OnFragmentInteractionListener mListener;

    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void scrollDialogDown() {
        sv.post(new Runnable() {

            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

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
        v = inflater.inflate(R.layout.fragment_history, container, false);

        scrollView = v.findViewById(R.id.scrollView);
        clearAll = v.findViewById(R.id.clearAll);
        copyAllBut = v.findViewById(R.id.copyAllBut);
        copyTV = v.findViewById(R.id.copyTV);

        tempTextV = new TextView(getActivity());
        tempTextV.setId(0);
        tempTextV.setText(R.string.hist_theres_no_values_for_now);
        tempTextV.setTextSize(25);
        scrollView.addView(tempTextV);

        sv = v.findViewById(R.id.sv_h);

        historyLL = v.findViewById(R.id.historyLL);

        //чтение значения строк в текстовом файле "settings"
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                if (temp==1) {
                    if (line.contains("d")) {
                        historyLL.setBackgroundColor(Color.parseColor("#252525"));
                        clearAll.getDrawable().setTint(Color.WHITE);
                        tempTextV.setTextColor(Color.WHITE);
                        copyTV.setTextColor(Color.WHITE);
                        copyAllBut.getDrawable().setTint(Color.WHITE);
                    }
                    if (line.contains("w")) {
                        historyLL.setBackgroundColor(Color.WHITE);
                        clearAll.getDrawable().setTint(Color.BLACK);
                        tempTextV.setTextColor(Color.BLACK);
                        copyTV.setTextColor(Color.BLACK);
                        copyAllBut.getDrawable().setTint(Color.BLACK);
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

        //чтение значения строк в текстовом файле "history"
        //и создание textView для каждого из них
        file = new File("history");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput(file.getName())));
            String line = "";
            if (reader.readLine() != null)
                scrollView.removeAllViews();
            int temp=0;
            while((line = reader.readLine()) != null) {
                if (temp%2==0) {
                    TextView expression = new TextView(getActivity());
                    expression.setText(line);
                    expression.setId(idExp);
                    expression.setTextColor(Color.parseColor("#03A9F4"));
                    scrollView.addView(expression);
                    idExp++;
                    copiedStr+=line+"\n";
                }
                else {
                    TextView result = new TextView(getActivity());
                    result.setText(line);
                    result.setId(idResult);
                    result.setTextColor(Color.parseColor("#FF4040"));
                    scrollView.addView(result);
                    idResult++;
                    copiedStr+=line+"\n\n";
                }

                temp++;
            }

            scrollDialogDown();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            scrollView.addView(tempTextV);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.removeAllViews();
                scrollView.addView(tempTextV);
                scrollView.scrollTo(0, 0);
                try {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getContext().openFileOutput("history", Context.MODE_PRIVATE)));
                    writer.write("");
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        copyAllBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE)).setText(copiedStr);
                Toast.makeText(getContext(), R.string.copied_history, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    public View getV() {
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
