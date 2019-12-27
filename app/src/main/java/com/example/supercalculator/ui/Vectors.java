package com.example.supercalculator.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.*;
import android.widget.Toast;

import com.example.supercalculator.R;
import com.example.supercalculator.VectorMaster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Vectors.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Vectors#newInstance} factory method to
 * create an instance of this fragment.
 */

//раздел "Векторы"
public class Vectors extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button aGetCBut, bGetCBut, lGetConstBut, clearBut, calcBut;
    private EditText aX, aY, aZ, bX, bY, bZ, lambda;
    private VectorMaster a, b, c;
    private LinearLayout containerList, vectLL;
    private String[] actions = {"a+b", "a•b", "a×b", "|a|", "|b|", "aƛ", "bƛ", "cos(a,b)"};
    private String actionString, answer, copyStr="";
    private int idExp, idResult;
    private double lamb, tempC;
    private boolean abEntered[], dark = false, toVibr = false;
    File file;
    TextView tvA, tvB, tvL, tvA1, tvB1, tv33, tv45, tv31, tv32, tv43, tv44;
    ScrollView sv;
    ImageButton copyBut;

    public Vectors() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Vectors.
     */
    // TODO: Rename and change types and number of parameters
    public static Vectors newInstance(String param1, String param2) {
        Vectors fragment = new Vectors();
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

    //UI: установка необходимой цветовой гаммы
    //в соответствии с фоном приложения
    public void setColors(int color) {
        if (color == Color.WHITE) {
            dark = true;
            vectLL.setBackgroundColor(Color.parseColor("#252525"));
        }
        else vectLL.setBackgroundColor(Color.WHITE);

        tvA.setTextColor(color);
        tvB.setTextColor(color);
        tvA1.setTextColor(color);
        tvB1.setTextColor(color);
        tvL.setTextColor(color);
        tv33.setTextColor(color);
        tv45.setTextColor(color);
        tv31.setTextColor(color);
        tv32.setTextColor(color);
        tv43.setTextColor(color);
        tv44.setTextColor(color);
        aX.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        aY.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        aZ.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        bX.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        bY.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        bZ.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        aX.setTextColor(color);
        aY.setTextColor(color);
        aZ.setTextColor(color);
        bX.setTextColor(color);
        bY.setTextColor(color);
        bZ.setTextColor(color);
        copyBut.getDrawable().setTint(color);
        lambda.setTextColor(color);
        lambda.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vectors, container, false);
        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, actions);
        actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spActions = (Spinner) v.findViewById(R.id.actionList);

        spActions.setAdapter(actionsAdapter);

        lamb=0;

        file = new File("history");

        copyBut = v.findViewById(R.id.copyBut2);
        copyBut.setEnabled(false);

        spActions.setOnItemSelectedListener(onItemSelectedListener());

        Drawable spinnerDrawable = spActions.getBackground().getConstantState().newDrawable();

        aX = v.findViewById(R.id.aX);
        aY = v.findViewById(R.id.aY);
        aZ = v.findViewById(R.id.aZ);
        bX = v.findViewById(R.id.bX);
        bY = v.findViewById(R.id.bY);
        bZ = v.findViewById(R.id.bZ);
        lambda = v.findViewById(R.id.lambda);

        aGetCBut = v.findViewById(R.id.aGetCBut);
        bGetCBut = v.findViewById(R.id.bGetCBut);
        lGetConstBut = v.findViewById(R.id.lGetConstBut);
        clearBut = v.findViewById(R.id.clearBut);
        calcBut = v.findViewById(R.id.calcBut);

        vectLL = v.findViewById(R.id.vectLL);

        tvA = v.findViewById(R.id.textView343);
        tvB = v.findViewById(R.id.textView30);
        tvL = v.findViewById(R.id.textView38);
        tvA1 = v.findViewById(R.id.textView46);
        tvB1= v.findViewById(R.id.textView47);
        tv33 = v.findViewById(R.id.textView33);
        tv45 = v.findViewById(R.id.textView45);
        tv31 = v.findViewById(R.id.textView31);
        tv32 = v.findViewById(R.id.textView32);
        tv43 = v.findViewById(R.id.textView43);
        tv44 = v.findViewById(R.id.textView44);


        sv = v.findViewById(R.id.scrollView);
        containerList = v.findViewById(R.id.containerList);

        tempC=0;
        abEntered = new boolean[3];

        //чтение значения строк в текстовом файле "settings"
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                if (temp==1) {
                    if (line.contains("d")) {
                        vectLL.setBackgroundColor(Color.parseColor("#252525"));
                        spinnerDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        setColors(Color.WHITE);
                    }
                    if (line.contains("w")) {
                        vectLL.setBackgroundColor(Color.WHITE);
                        spinnerDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                        setColors(Color.BLACK);
                    }
                    spActions.setBackground(spinnerDrawable);
                }
                if (temp==3) {
                    if (line.contains("t"))
                        toVibr = true;
                    if (line.contains("f"))
                        toVibr = false;
                }
                temp++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //обработка нажатий на различные кнопки в разделе
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aGetCBut.setEnabled(false);
                bGetCBut.setEnabled(false);
                lGetConstBut.setEnabled(false);
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

                if (toVibr) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vib.vibrate(VibrationEffect.createOneShot(20, 1));
                    } else {
                        //deprecated in API 26
                        vib.vibrate(20);
                    }
                }
                switch (view.getId()) {
                    case R.id.copyBut2:
                        ((ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE)).setText(copyStr);
                        Toast.makeText(getContext(), R.string.copied_answer, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.calcBut:
                        if (!aX.getText().toString().isEmpty() && !aY.getText().toString().isEmpty() && !aZ.getText().toString().isEmpty()) {
                            a = new VectorMaster(Double.parseDouble(aX.getText().toString()), Double.parseDouble(aY.getText().toString()), Double.parseDouble(aZ.getText().toString()));
                            abEntered[0]=true;
                        }
                        if (!bX.getText().toString().isEmpty() && !bY.getText().toString().isEmpty() && !bZ.getText().toString().isEmpty()) {
                            b = new VectorMaster(Double.parseDouble(bX.getText().toString()), Double.parseDouble(bY.getText().toString()), Double.parseDouble(bZ.getText().toString()));
                            abEntered[1] = true;
                        }
                        if ((!aX.getText().toString().isEmpty() && !aY.getText().toString().isEmpty() && !aZ.getText().toString().isEmpty()) ||
                                !bX.getText().toString().isEmpty() && !bY.getText().toString().isEmpty() && !bZ.getText().toString().isEmpty()) {
                            c = new VectorMaster(0, 0, 0);
                            if ((!aX.getText().toString().isEmpty() && !aY.getText().toString().isEmpty() && !aZ.getText().toString().isEmpty()) &&
                                    !bX.getText().toString().isEmpty() && !bY.getText().toString().isEmpty() && !bZ.getText().toString().isEmpty())
                                    abEntered[2] = true;
                        }
                        if (!lambda.getText().toString().isEmpty())
                            lamb = Double.parseDouble(lambda.getText().toString());
                        answer="";
                        switch(actionString) {
                            case "a+b":
                                if (abEntered[2]) {
                                    answer = c.adding(a, b).toString();
                                    c = c.adding(a, b);
                                    aGetCBut.setEnabled(true);
                                    bGetCBut.setEnabled(true);
                                }
                                break;
                            case "a•b":
                                if (abEntered[2]) {
                                    answer = Double.toString(c.scalarMultiply(a, b));
                                    tempC = Double.parseDouble(answer);
                                    lGetConstBut.setEnabled(true);
                                }
                                break;
                            case "a×b":
                                if (abEntered[2]) {
                                    answer = c.vectorMultiply(a, b).toString();
                                    c = c.vectorMultiply(a, b);
                                    aGetCBut.setEnabled(true);
                                    bGetCBut.setEnabled(true);
                                }
                                break;
                            case "|a|":
                                if (abEntered[0]) {
                                    answer = Double.toString(a.getScalar());
                                    tempC = Double.parseDouble(answer);
                                    lGetConstBut.setEnabled(true);
                                }
                                break;
                            case "|b|":
                                if (abEntered[1]) {
                                    answer = Double.toString(b.getScalar());
                                    tempC = Double.parseDouble(answer);
                                    lGetConstBut.setEnabled(true);
                                }
                                break;
                            case "aƛ":
                                if (abEntered[0]) {
                                    if(!lambda.getText().toString().isEmpty()) {
                                        answer = a.constMultiply(lamb).toString();
                                        c = a.constMultiply(lamb);
                                        aGetCBut.setEnabled(true);
                                        bGetCBut.setEnabled(true);
                                    }
                                }
                                break;
                            case "bƛ":
                                if (abEntered[1]) {
                                    if(!lambda.getText().toString().isEmpty()) {
                                        answer = b.constMultiply(lamb).toString();
                                        c = b.constMultiply(lamb);
                                        aGetCBut.setEnabled(true);
                                        bGetCBut.setEnabled(true);
                                    }
                                }
                                break;
                            case "cos(a,b)":
                                if (abEntered[2]) {
                                    answer = Double.toString(c.getAngle(a, b));
                                    tempC = Double.parseDouble(answer);
                                    lGetConstBut.setEnabled(true);
                                }
                                break;
                        }

                        String fileStr="";

                        //вывод результатов вычислений на экран

                        TextView expression = new TextView(getActivity());
                        expression.setText(getString(R.string.expression) + actionString);
                        fileStr = expression.getText().toString() + "\n";
                        expression.setId(idExp);
                        expression.setTextColor(Color.parseColor("#03A9F4"));
                        containerList.addView(expression);
                        idExp++;

                        TextView result = new TextView(getActivity());
                        if (answer!="") {
                            result.setText(getString(R.string.matr_vect_answer) + " c = {"+ answer + "}");
                            result.setTextColor(Color.parseColor("#FF4040"));
                            copyStr = answer;
                            copyBut.setEnabled(true);
                        }
                        else {
                            result.setText(getString(R.string.vect_error_input_all_values));
                            result.setTextColor(Color.parseColor("#FF9800"));
                            copyBut.setEnabled(false);
                        }
                        fileStr = fileStr + result.getText().toString() + "\n";
                        result.setId(idResult);
                        containerList.addView(result);
                        idResult++;

                        try {
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getContext().openFileOutput("history", Context.MODE_PRIVATE | Context.MODE_APPEND)));
                            writer.append(fileStr);
                            writer.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        scrollDialogDown();

                        clearBut.setEnabled(true);
                        abEntered[0]=abEntered[1]=abEntered[2]=false;
                        break;
                    case R.id.clearBut:
                        containerList.removeAllViews();
                        containerList.scrollTo(0,0);
                        clearBut.setEnabled(false);
                        break;
                    case R.id.aGetCBut:
                        a = c;
                        aX.setText(Double.toString(a.getX()));
                        aY.setText(Double.toString(a.getY()));
                        aZ.setText(Double.toString(a.getZ()));
                        break;
                    case R.id.bGetCBut:
                        b = c;
                        bX.setText(Double.toString(b.getX()));
                        bY.setText(Double.toString(b.getY()));
                        bZ.setText(Double.toString(b.getZ()));
                        break;
                    case R.id.lGetConstBut:
                        lamb = tempC;
                        lambda.setText(Double.toString(lamb));
                }
            }
        };
        aGetCBut.setOnClickListener(clickListener);
        bGetCBut.setOnClickListener(clickListener);
        lGetConstBut.setOnClickListener(clickListener);
        clearBut.setOnClickListener(clickListener);
        calcBut.setOnClickListener(clickListener);
        copyBut.setOnClickListener(clickListener);

        return v;
    }

    //работа со Spinner
    AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                actionString = adapterView.getSelectedItem().toString();
                if (dark)
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                else ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
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
