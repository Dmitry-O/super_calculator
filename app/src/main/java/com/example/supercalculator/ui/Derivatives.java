package com.example.supercalculator.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.SymbolTable;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supercalculator.MainActivity;
import com.example.supercalculator.R;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;


//import net.objecthunter.exp4j.Expression;
//import net.objecthunter.exp4j.ExpressionBuilder;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import static java.lang.Double.NaN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Derivatives.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Derivatives#newInstance} factory method to
 * create an instance of this fragment.
 */

//раздел "Научный калькулятор"
public class Derivatives extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DecimalFormat df;

    private EditText funcText, xValue, rootNLL, aLog, bArgInt, aArgInt, argInt, argDer, rootFunc, absFunc, logFunc, intFunc, derFunc, yValue, fValue, gValue, aValue, bValue, tValue;
    private Button funcButt, plusBut, minusBut, multBut, divBut, xBut, xnBut, sqrtxBut, lnxBut, logxBut, eBut, piBut, hookOpBut,
            hookClBut, sinBut, cosBut, tanBut, ctgBut, wipeOffBut, but1, but2, but3, but4, but5, but6, but7, but8, but9, but0, dotBut, xVButOK,
            commaBut, perCentBut, yBut, fBut, gBut, aBut, bBut, tBut, log10xBut, rootBut, notBut, lessBut, moreBut, equalsBut, braketOpBut,
            braketClBut, absBut, intDefBut, derBut, clearBut, notEqualsBut, addExp;
    private String fStr;
    private boolean[] trigFuncs, argClicked;
    private LinearLayout tempLL, rootLL, logLL, absLL, intDefLL, derLL, funcLL, containerResults, varLL, derivMainLL;
    boolean switchState, xVClicked;
    boolean varClicked[];
    int idResult, idExp, scrollCount;
    File file;
    MainActivity main;
    String tempStr="";
    ScrollView sv;
    ImageButton copyBut;
    ImageView img;

    String fileStr="", lang="";
    boolean toVibr=false, manySymb = false;
    int decTemp=-1, wait=0;

    Expression calcField;

    TimerTask timerTask;
    Timer timer;

    FragmentActivity frAct;

    private void scrollDialogDown() {
        sv.post(new Runnable() {

            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }


    private OnFragmentInteractionListener mListener;

    /*public void rollingUp() {
        lpVert.height=280;
        lpVert.width=75;
        vertLL.setLayoutParams(lpVert);
        lpHor.width=372;
        lpHor.height = LinearLayout.LayoutParams.MATCH_PARENT;
        horLL.setLayoutParams(lpHor);
        xVButOK.callOnClick();
    }*/
    static long back_pressed;

    public void showToast() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                frAct.runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        if ((funcText.getText().toString().length() == 28 || funcText.getText().toString().length() > 28) && !manySymb) {
                            Toast.makeText(frAct, R.string.scroll_filed, Toast.LENGTH_SHORT).show();
                            manySymb = true;
                            img.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1);
    }

    public Derivatives() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Derivatives.
     */
    // TODO: Rename and change types and number of parameters
    public static Derivatives newInstance(String param1, String param2) {
        Derivatives fragment = new Derivatives();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_derivatives, container, false);

        file = new File("history");

        frAct = getActivity();
        main = (MainActivity) getActivity();

        img = v.findViewById(R.id.imgUp);

        funcText = v.findViewById(R.id.funcText);
        funcButt = v.findViewById(R.id.funcButt);
        xValue = v.findViewById(R.id.xValue);
        xVButOK = v.findViewById(R.id.xVButOK);
        yValue = v.findViewById(R.id.yValue);
        fValue = v.findViewById(R.id.fValue);
        gValue = v.findViewById(R.id.gValue);
        aValue = v.findViewById(R.id.aValue);
        bValue = v.findViewById(R.id.bValue);
        tValue = v.findViewById(R.id.tValue);

        sv = v.findViewById(R.id.sv);

        copyBut = v.findViewById(R.id.copyBut);
        copyBut.setEnabled(false);

        rootNLL = v.findViewById(R.id.rootNLL);

        containerResults = v.findViewById(R.id.containerResults);

        varLL = v.findViewById(R.id.variablesLL);

        plusBut = v.findViewById(R.id.plusBut);
        minusBut = v.findViewById(R.id.minusBut);
        multBut = v.findViewById(R.id.multBut);
        divBut = v.findViewById(R.id.divBut);
        xBut = v.findViewById(R.id.xBut);
        xnBut = v.findViewById(R.id.xnBut);
        lnxBut = v.findViewById(R.id.lnxBut);
        logxBut = v.findViewById(R.id.logxBut);
        eBut = v.findViewById(R.id.eBut);
        piBut = v.findViewById(R.id.piBut);
        sinBut = v.findViewById(R.id.sinbut);
        cosBut = v.findViewById(R.id.cosBut);
        tanBut = v.findViewById(R.id.tanBut);
        ctgBut = v.findViewById(R.id.ctgBut);
        hookOpBut = v.findViewById(R.id.hookOpBut);
        hookClBut = v.findViewById(R.id.hookClBut);
        dotBut = v.findViewById(R.id.dotBut);
        wipeOffBut = v.findViewById(R.id.wipeOffBut);
        but1 = v.findViewById(R.id.but1);
        but2 = v.findViewById(R.id.but2);
        but3 = v.findViewById(R.id.but3);
        but4 = v.findViewById(R.id.but4);
        but5 = v.findViewById(R.id.but5);
        but6 = v.findViewById(R.id.but6);
        but7 = v.findViewById(R.id.but7);
        but8 = v.findViewById(R.id.but8);
        but9 = v.findViewById(R.id.but9);
        but0 = v.findViewById(R.id.but0);
        notEqualsBut = v.findViewById(R.id.notEqualsBut);

        commaBut = v.findViewById(R.id.commaBut);
        perCentBut = v.findViewById(R.id.perCentBut);
        yBut = v.findViewById(R.id.yBut);
        fBut = v.findViewById(R.id.fBut);
        gBut = v.findViewById(R.id.gBut);
        aBut = v.findViewById(R.id.aBut);
        bBut = v.findViewById(R.id.bBut);
        tBut = v.findViewById(R.id.tBut);
        log10xBut = v.findViewById(R.id.log10xBut);
        rootBut = v.findViewById(R.id.rootBut);
        notBut = v.findViewById(R.id.notBut);
        lessBut = v.findViewById(R.id.lessBut);
        moreBut = v.findViewById(R.id.moreBut);
        equalsBut = v.findViewById(R.id.equalsBut);
        braketOpBut = v.findViewById(R.id.braketOpBut);
        braketClBut = v.findViewById(R.id.braketClBut);
        absBut = v.findViewById(R.id.absBut);
        intDefBut = v.findViewById(R.id.intDefBut);
        derBut = v.findViewById(R.id.derBut);

        addExp = v.findViewById(R.id.addExp);

        tempLL = v.findViewById(R.id.tempLL);
        rootLL = v.findViewById(R.id.rootLL);
        logLL = v.findViewById(R.id.logLL);
        absLL = v.findViewById(R.id.absLL);
        intDefLL = v.findViewById(R.id.intDefLL);
        derLL = v.findViewById(R.id.derLL);
        funcLL = v.findViewById(R.id.funcLL);

        aLog = v.findViewById(R.id.aLog);
        bArgInt = v.findViewById(R.id.bArgInt);
        aArgInt = v.findViewById(R.id.aArgInt);
        argInt = v.findViewById(R.id.argInt);
        argDer = v.findViewById(R.id.argDer);
        sqrtxBut = v.findViewById(R.id.sqrtxBut);

        rootFunc = v.findViewById(R.id.rootFunc);
        logFunc = v.findViewById(R.id.logFunc);
        absFunc = v.findViewById(R.id.absFunc);
        intFunc = v.findViewById(R.id.intFunc);
        derFunc = v.findViewById(R.id.derFunc);

        clearBut = v.findViewById(R.id.clearBut);

        derivMainLL = v.findViewById(R.id.derivMainLL);

        lessBut.setText("<");
        moreBut.setText(">");

        trigFuncs = new boolean[10];
        argClicked = new boolean[11];
        varClicked = new boolean[7];

        switchState = false;
        xVClicked = false;

        idResult=0;
        idExp=0;

        funcText.setMovementMethod(new ScrollingMovementMethod());


        View.OnScrollChangeListener scrollChangeListener = new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (view == funcText)
                    img.setVisibility(View.INVISIBLE);
            }
        };
        funcText.setOnScrollChangeListener(scrollChangeListener);

        funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);

        //чтение значения строк в текстовом файле "settings"
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                if (temp==0) {
                    lang = line;
                }
                if (temp==1) {
                    if (line.contains("d")) {
                        derivMainLL.setBackgroundColor(Color.parseColor("#252525"));
                        funcButt.setBackgroundTintList(getResources().getColorStateList(R.color.buttonBackground));
                        funcText.setTextColor(Color.WHITE);
                        aLog.setTextColor(Color.WHITE);
                        bArgInt.setTextColor(Color.WHITE);
                        aArgInt.setTextColor(Color.WHITE);
                        argInt.setTextColor(Color.WHITE);
                        argDer.setTextColor(Color.WHITE);
                        rootFunc.setTextColor(Color.WHITE);
                        logFunc.setTextColor(Color.WHITE);
                        absFunc.setTextColor(Color.WHITE);
                        intFunc.setTextColor(Color.WHITE);
                        derFunc.setTextColor(Color.WHITE);
                        copyBut.getDrawable().setTint(Color.WHITE);
                        img.getDrawable().setTint(Color.WHITE);
                    }
                    if (line.contains("w")) {
                        derivMainLL.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        funcButt.setBackgroundTintList(getResources().getColorStateList(R.color.someColor));
                        funcText.setTextColor(Color.BLACK);
                        aLog.setTextColor(Color.BLACK);
                        bArgInt.setTextColor(Color.BLACK);
                        aArgInt.setTextColor(Color.BLACK);
                        argInt.setTextColor(Color.BLACK);
                        argDer.setTextColor(Color.BLACK);
                        rootFunc.setTextColor(Color.BLACK);
                        logFunc.setTextColor(Color.BLACK);
                        absFunc.setTextColor(Color.BLACK);
                        intFunc.setTextColor(Color.BLACK);
                        derFunc.setTextColor(Color.BLACK);
                        copyBut.getDrawable().setTint(Color.BLACK);
                        img.getDrawable().setTint(Color.BLACK);
                    }
                }
                if (temp==2)
                    decTemp = Integer.parseInt(line);
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

        fStr = "";

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vib.vibrate(VibrationEffect.createOneShot(20, 1));
                } else {
                    //deprecated in API 26
                    vib.vibrate(20);
                }
                //очистка поля ввода
                if (view.getId() == R.id.wipeOffBut) {
                    if (argClicked[0]) rootFunc.setText("");
                    if (argClicked[1]) aLog.setText("");
                    if (argClicked[2]) rootNLL.setText("");
                    if (argClicked[3]) bArgInt.setText("");
                    if (argClicked[4]) aArgInt.setText("");
                    if (argClicked[5]) argInt.setText("");
                    if (argClicked[6]) argDer.setText("");
                    if (argClicked[7]) logFunc.setText("");
                    if (argClicked[8]) absFunc.setText("");
                    if (argClicked[9]) intFunc.setText("");
                    if (argClicked[10]) derFunc.setText("");
                    if (xVClicked) xValue.setText("");

                    if (!argClicked[0] && !argClicked[1] && !argClicked[2] && !argClicked[3] && !argClicked[4] && !argClicked[5] && !argClicked[6] && !argClicked[7] && !argClicked[8] && !argClicked[9] && !argClicked[10]) {
                        funcText.setText("");
                        fStr = "";
                    }
                    return true;
                }
                if (view.getId()==R.id.sinbut) { writingInFunc("asin("); return true; }
                if (view.getId()==R.id.cosBut) { writingInFunc("acos("); return true; }
                if (view.getId()==R.id.tanBut) { writingInFunc("atan("); return true; }
                if (view.getId()==R.id.ctgBut) { writingInFunc("actan(");  return true; }

                return true;
            }
        };
        wipeOffBut.setOnLongClickListener(longClickListener);
        sinBut.setOnLongClickListener(longClickListener);
        cosBut.setOnLongClickListener(longClickListener);
        tanBut.setOnLongClickListener(longClickListener);
        ctgBut.setOnLongClickListener(longClickListener);

        showToast();

        //оформление интерфейсной частти
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                                funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                for (int i=0; i < varClicked.length; i++)
                                    if (varClicked[i] == true)
                                        varClicked[i] = false;

                                if (view.getId() == R.id.xValue) {
                                    xVClicked = true;
                                    varClicked[0] = true;
                                    xValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                    xValue.setText("");
                                }
                                else xValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                if (view.getId()== R.id.yValue) {
                                    xVClicked = true;
                                    varClicked[1] = true;
                                    yValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                    yValue.setText("");
                                }
                                else yValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                if (view.getId()== R.id.fValue) {
                                    xVClicked = true;
                                    varClicked[2] = true;
                                    fValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                    fValue.setText("");
                                }
                                else fValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                if (view.getId()== R.id.gValue) {
                                    xVClicked = true;
                                    varClicked[3] = true;
                                    gValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                    gValue.setText("");
                                }
                                else gValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                if (view.getId()== R.id.aValue) {
                                    xVClicked = true;
                                    varClicked[4] = true;
                                    aValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                    aValue.setText("");
                                }
                                else aValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                if (view.getId()== R.id.bValue) {
                                    xVClicked = true;
                                    varClicked[5] = true;
                                    bValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                    bValue.setText("");
                                }
                                else bValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                if (view.getId()== R.id.tValue) {
                                    xVClicked = true;
                                    varClicked[6] = true;
                                    tValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                    tValue.setText("");
                                }
                                else tValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

                                if (view.getId() == R.id.rootFunc) {
                                    argClicked[0] = true;
                                    rootFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    rootFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[0] = false;
                                }

                                if (view.getId() == R.id.aLog) {
                                    argClicked[1] = true;
                                    aLog.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    aLog.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[1] = false;
                                }

                                if (view.getId() == R.id.rootNLL) {
                                    argClicked[2] = true;
                                    rootNLL.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    rootNLL.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[2] = false;
                                }

                                if (view.getId() == R.id.bArgInt) {
                                    argClicked[3] = true;
                                    bArgInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    bArgInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[3] = false;
                                }

                                if (view.getId() == R.id.aArgInt) {
                                    argClicked[4] = true;
                                    aArgInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    aArgInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[4] = false;
                                }

                                if (view.getId() == R.id.argInt) {
                                    argClicked[5] = true;
                                    argInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    argInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[5] = false;
                                }

                                if (view.getId() == R.id.argDer) {
                                    argClicked[6] = true;
                                    argDer.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    argDer.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[6] = false;
                                }

                                if (view.getId() == R.id.logFunc) {
                                    argClicked[7] = true;
                                    logFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    logFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[7] = false;
                                }

                                if (view.getId() == R.id.absFunc) {
                                    argClicked[8] = true;
                                    absFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    absFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[8] = false;
                                }

                                if (view.getId() == R.id.intFunc) {
                                    argClicked[9] = true;
                                    intFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    intFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[9] = false;
                                }

                                if (view.getId() == R.id.derFunc) {
                                    argClicked[10] = true;
                                    derFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                }
                                else {
                                    derFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                    argClicked[10] = false;
                                }
                        break;
                }
                return true;
            }
        };
        xValue.setOnTouchListener(touchListener);
        rootFunc.setOnTouchListener(touchListener);
        argInt.setOnTouchListener(touchListener);
        rootNLL.setOnTouchListener(touchListener);
        aLog.setOnTouchListener(touchListener);
        bArgInt.setOnTouchListener(touchListener);
        aArgInt.setOnTouchListener(touchListener);
        argDer.setOnTouchListener(touchListener);
        argDer.setOnTouchListener(touchListener);
        logFunc.setOnTouchListener(touchListener);
        absFunc.setOnTouchListener(touchListener);
        intFunc.setOnTouchListener(touchListener);
        derFunc.setOnTouchListener(touchListener);
        yValue.setOnTouchListener(touchListener);
        fValue.setOnTouchListener(touchListener);
        gValue.setOnTouchListener(touchListener);
        aValue.setOnTouchListener(touchListener);
        bValue.setOnTouchListener(touchListener);
        tValue.setOnTouchListener(touchListener);

       scrollCount=0;
        //обработка нажатий на разные кнопки в разделе
        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

                if (toVibr) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vib.vibrate(VibrationEffect.createOneShot(20, 1));
                    } else {
                        //deprecated in API 26
                        vib.vibrate(20);
                    }
                }

                switch (view.getId()){
                    case R.id.copyBut:
                        ((ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE)).setText(tempStr);
                        Toast.makeText(getContext(), R.string.copied_answer, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addExp:
                        funcText.setText(funcText.getText().toString() + tempStr);
                        fStr = fStr + tempStr;
                        break;
                    case R.id.clearBut:
                        containerResults.removeAllViews();
                        scrollCount=0;
                        containerResults.scrollTo(0, 0);
                        addExp.setEnabled(false);
                        break;
                    case R.id.xVButOK:
                        xVClicked = !xVClicked;
                        if (!xVClicked) {
                            xVButOK.setText("\\/");
                            varLL.setVisibility(View.GONE);
                            //lpVert = new LinearLayout.LayoutParams(150,280);
                            //vertLL.setLayoutParams(lpVert);
                            //lpHor = new LinearLayout.LayoutParams(372, LinearLayout.LayoutParams.MATCH_PARENT);
                            //horLL.setLayoutParams(lpHor);
                        }
                        else {
                            //if (!trigFuncs[9] && !trigFuncs[4] && !trigFuncs[5] && !trigFuncs[6] && !trigFuncs[7]) {
                                xVButOK.setText("/\\");
                                varLL.setVisibility(View.VISIBLE);
                                //lpVert = new LinearLayout.LayoutParams(200,1200);
                                //vertLL.setLayoutParams(lpVert);
                                //lpHor = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.MATCH_PARENT);
                                //horLL.setLayoutParams(lpHor);
                            //}
                            //else {
                            //    Toast.makeText(getActivity(), R.string.finish_entering, Toast.LENGTH_LONG).show();
                            //}
                        }
                        funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                        xValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                        yValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                        fValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                        gValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                        aValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                        bValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                        tValue.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case R.id.funcButt:
                        Argument x = new Argument("x=" + xValue.getText().toString());
                        Argument y = new Argument("y=" + yValue.getText().toString());
                        Argument f = new Argument("f=" + fValue.getText().toString());
                        Argument z = new Argument("z=" + gValue.getText().toString());
                        Argument h = new Argument("h=" + aValue.getText().toString());
                        Argument k = new Argument("k=" + bValue.getText().toString());
                        Argument m = new Argument("m=" + tValue.getText().toString());

                        calcField = new Expression(funcText.getText().toString());

                        if (!xValue.getText().toString().isEmpty())
                            calcField.addArguments(x);
                        else if (funcText.getText().toString().contains("x")) { Toast.makeText(getActivity(), R.string.tst_input_x, Toast.LENGTH_SHORT).show(); break; }

                        if (!yValue.getText().toString().isEmpty())
                            calcField.addArguments(y);
                        else if (funcText.getText().toString().contains("y")) { Toast.makeText(getActivity(), R.string.tst_input_y, Toast.LENGTH_SHORT).show(); break; }

                        if (!fValue.getText().toString().isEmpty())
                            calcField.addArguments(f);
                        else if (funcText.getText().toString().contains("f")) { Toast.makeText(getActivity(), R.string.tst_input_f, Toast.LENGTH_SHORT).show(); break; }

                        if (!gValue.getText().toString().isEmpty())
                            calcField.addArguments(z);
                        else if (funcText.getText().toString().contains("z")) { Toast.makeText(getActivity(), R.string.tst_input_g, Toast.LENGTH_SHORT).show(); break; }

                        if (!aValue.getText().toString().isEmpty())
                            calcField.addArguments(h);
                        else if (funcText.getText().toString().contains("h")) { Toast.makeText(getActivity(), R.string.tst_input_a, Toast.LENGTH_SHORT).show(); break; }

                        if (!bValue.getText().toString().isEmpty())
                            calcField.addArguments(k);
                        else if (funcText.getText().toString().contains("k")) { Toast.makeText(getActivity(), R.string.tst_input_b, Toast.LENGTH_SHORT).show(); break; }

                        if (!tValue.getText().toString().isEmpty())
                            calcField.addArguments(m);
                        else if (funcText.getText().toString().contains("m")) { Toast.makeText(getActivity(), R.string.tst_input_t, Toast.LENGTH_SHORT).show(); break; }


                        String decString="";

                        if (decTemp<0)
                            decString = "";
                        if (decTemp==0)
                            decString = "#";
                        if (decTemp>0) {
                            decString = "#.";
                            for (int i = 0; i < decTemp; i++)
                                decString += "#";
                        }

                        df = new DecimalFormat(decString);
                        df.setRoundingMode(RoundingMode.HALF_DOWN);

                        if (!funcText.getText().toString().isEmpty()) {
                            TextView expression = new TextView(getActivity());
                            expression.setText(getString(R.string.expression) + calcField.getExpressionString());
                            fileStr = expression.getText().toString() + "\n";
                            expression.setId(idExp);
                            expression.setTextColor(Color.parseColor("#03A9F4"));
                            expression.setTextSize(18);
                            containerResults.addView(expression);
                            idExp++;

                            TextView result = new TextView(getActivity());
                            result.setId(idResult);
                            if (Double.toString(calcField.calculate()) != "NaN") {
                                if (decTemp==-1)
                                    result.setText(getString(R.string.answer) + calcField.calculate());
                                else result.setText(getString(R.string.answer) + df.format(calcField.calculate()));
                                tempStr = df.format(calcField.calculate());
                                addExp.setEnabled(true);
                                result.setTextColor(Color.parseColor("#FF4040"));
                                funcText.setText("");
                                fStr = "";
                                copyBut.setEnabled(true);
                            } else {
                                result.setText(R.string.error);
                                result.setTextColor(Color.parseColor("#FF9800"));
                                addExp.setEnabled(false);
                                copyBut.setEnabled(false);
                            }
                            fileStr = fileStr + result.getText().toString() + "\n";

                            try {
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getContext().openFileOutput("history", Context.MODE_PRIVATE | Context.MODE_APPEND)));
                                writer.append(fileStr);
                                writer.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            result.setTextSize(18);
                            containerResults.addView(result);
                            idResult++;

                            scrollDialogDown();
                        }
                        break;
                    case R.id.plusBut:
                        writingInFunc("+");
                        break;
                    case R.id.minusBut:
                        writingInFunc("-");
                        break;
                    case R.id.multBut:
                        writingInFunc("*");
                        break;
                    case R.id.divBut:
                        writingInFunc("/");
                        break;
                    case R.id.commaBut:
                        writingInFunc(",");
                        break;
                    case R.id.perCentBut:
                        writingInFunc("%");
                        break;
                    case R.id.yBut:
                        writingInFunc("y");
                        break;
                    case R.id.fBut:
                        writingInFunc("f");
                        break;
                    case R.id.gBut:
                        writingInFunc("z");
                        break;
                    case R.id.aBut:
                        writingInFunc("h");
                        break;
                    case R.id.bBut:
                        writingInFunc("k");
                        break;
                    case R.id.tBut:
                        writingInFunc("m");
                        break;
                    case R.id.notBut:
                        writingInFunc("!");
                        break;
                    case R.id.lessBut:
                        writingInFunc("<");
                        break;
                    case R.id.moreBut:
                        writingInFunc(">");
                        break;
                    case R.id.equalsBut:
                        writingInFunc("=");
                        break;
                    case R.id.braketOpBut:
                        writingInFunc("[");
                            break;
                    case R.id.braketClBut:
                        writingInFunc("]");
                        break;
                    case R.id.notEqualsBut:
                        writingInFunc("!=");
                        break;
                    case R.id.logxBut:
                        if (!trigFuncs[9]) {
                            tempLL.setVisibility(View.VISIBLE);
                            logLL.setVisibility(View.VISIBLE);
                            trigFuncs[9]=!trigFuncs[9];
                            funcLL.setVisibility(View.GONE);
                            absBut.setEnabled(false);
                            intDefBut.setEnabled(false);
                            derBut.setEnabled(false);
                            rootBut.setEnabled(false);
                            funcButt.setEnabled(false);
                            logxBut.setBackgroundColor(Color.parseColor("#584646"));
                            logxBut.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else {
                            if ((aLog.getText().toString().isEmpty() && logFunc.getText().toString().isEmpty()) || (!aLog.getText().toString().isEmpty() && !logFunc.getText().toString().isEmpty())) {
                                if (!(aLog.getText().toString().isEmpty() && logFunc.getText().toString().isEmpty()))
                                    fStr += "log(" + aLog.getText() + "," + logFunc.getText() + ")";
                                aLog.setText("");
                                logFunc.setText("");
                                tempLL.setVisibility(View.GONE);
                                logLL.setVisibility(View.GONE);
                                trigFuncs[9] = !trigFuncs[9];
                                funcLL.setVisibility(View.VISIBLE);
                                resetAllFields();
                                funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                absBut.setEnabled(true);
                                intDefBut.setEnabled(true);
                                derBut.setEnabled(true);
                                rootBut.setEnabled(true);
                                funcButt.setEnabled(true);
                                logxBut.setTextColor(getResources().getColor(android.R.color.background_dark));
                                aLog.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                logxBut.setBackground(clearBut.getBackground());
                            }
                            else if (aLog.getText().toString().isEmpty() || logFunc.getText().toString().isEmpty())
                                Toast.makeText(getActivity(), R.string.tst_fill_in_all_fields, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.log10xBut:
                        writingInFunc("log(10,");
                        break;
                    case R.id.rootBut:
                        if (!trigFuncs[4]) {
                            tempLL.setVisibility(View.VISIBLE);
                            rootLL.setVisibility(View.VISIBLE);
                            trigFuncs[4]=!trigFuncs[4];
                            funcLL.setVisibility(View.GONE);
                            absBut.setEnabled(false);
                            intDefBut.setEnabled(false);
                            derBut.setEnabled(false);
                            logxBut.setEnabled(false);
                            funcButt.setEnabled(false);
                            rootBut.setBackgroundColor(Color.parseColor("#584646"));
                            rootBut.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else {
                            if ((rootNLL.getText().toString().isEmpty() && rootFunc.getText().toString().isEmpty()) || (!rootNLL.getText().toString().isEmpty() && !rootFunc.getText().toString().isEmpty())) {
                                if (!rootNLL.getText().toString().isEmpty() && !rootFunc.getText().toString().isEmpty())
                                    fStr += "root(" + rootNLL.getText() + "," + rootFunc.getText() + ")";
                                rootFunc.setText("");
                                rootNLL.setText("");
                                tempLL.setVisibility(View.GONE);
                                rootLL.setVisibility(View.GONE);
                                trigFuncs[4] = !trigFuncs[4];
                                funcLL.setVisibility(View.VISIBLE);
                                resetAllFields();
                                funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                absBut.setEnabled(true);
                                intDefBut.setEnabled(true);
                                derBut.setEnabled(true);
                                logxBut.setEnabled(true);
                                funcButt.setEnabled(true);
                                rootBut.setTextColor(Color.parseColor("#1C1C1C"));
                                rootFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                rootNLL.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                rootBut.setBackground(clearBut.getBackground());
                            }
                            else if (rootNLL.getText().toString().isEmpty() || rootFunc.getText().toString().isEmpty())
                                Toast.makeText(getActivity(), R.string.tst_fill_in_all_fields, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.absBut:
                        if (!trigFuncs[5]) {
                            tempLL.setVisibility(View.VISIBLE);
                            absLL.setVisibility(View.VISIBLE);
                            trigFuncs[5]=!trigFuncs[5];
                            funcLL.setVisibility(View.GONE);
                            logxBut.setEnabled(false);
                            intDefBut.setEnabled(false);
                            derBut.setEnabled(false);
                            rootBut.setEnabled(false);
                            funcButt.setEnabled(false);
                            absBut.setBackgroundColor(Color.parseColor("#584646"));
                            absBut.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else {
                            if (!absFunc.getText().toString().isEmpty())
                                fStr += "abs(" + absFunc.getText() + ")";
                            absFunc.setText("");
                            tempLL.setVisibility(View.GONE);
                            absLL.setVisibility(View.GONE);
                            trigFuncs[5] = !trigFuncs[5];
                            funcLL.setVisibility(View.VISIBLE);
                            resetAllFields();
                            funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                            logxBut.setEnabled(true);
                            intDefBut.setEnabled(true);
                            derBut.setEnabled(true);
                            rootBut.setEnabled(true);
                            funcButt.setEnabled(true);
                            absBut.setTextColor(Color.parseColor("#1C1C1C"));
                            absFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                            absBut.setBackground(clearBut.getBackground());
                        }
                        break;
                    case R.id.derBut:
                        if (!trigFuncs[6]) {
                            tempLL.setVisibility(View.VISIBLE);
                            derLL.setVisibility(View.VISIBLE);
                            trigFuncs[6]=!trigFuncs[6];
                            funcLL.setVisibility(View.GONE);
                            absBut.setEnabled(false);
                            intDefBut.setEnabled(false);
                            logxBut.setEnabled(false);
                            rootBut.setEnabled(false);
                            funcButt.setEnabled(false);
                            derBut.setBackgroundColor(Color.parseColor("#584646"));
                            derBut.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else {
                            if ((argDer.getText().toString().isEmpty() && derFunc.getText().toString().isEmpty()) || (!argDer.getText().toString().isEmpty() && !derFunc.getText().toString().isEmpty())) {
                                if (!argDer.getText().toString().isEmpty() && !derFunc.getText().toString().isEmpty())
                                    fStr += "der(" + derFunc.getText() + "," + argDer.getText() + "," + xValue.getText() + ")";
                                derFunc.setText("");
                                argDer.setText("");
                                tempLL.setVisibility(View.GONE);
                                derLL.setVisibility(View.GONE);
                                trigFuncs[6] = !trigFuncs[6];
                                funcLL.setVisibility(View.VISIBLE);
                                resetAllFields();
                                funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                absBut.setEnabled(true);
                                intDefBut.setEnabled(true);
                                logxBut.setEnabled(true);
                                rootBut.setEnabled(true);
                                funcButt.setEnabled(true);
                                derBut.setTextColor(Color.parseColor("#1C1C1C"));
                                derFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                argDer.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                derBut.setBackground(clearBut.getBackground());
                            }
                            else if (argDer.getText().toString().isEmpty() || derFunc.getText().toString().isEmpty())
                                Toast.makeText(getActivity(), R.string.tst_fill_in_all_fields, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.intDefBut:
                        if (!trigFuncs[7]) {
                            tempLL.setVisibility(View.VISIBLE);
                            intDefLL.setVisibility(View.VISIBLE);
                            trigFuncs[7]=!trigFuncs[7];
                            funcLL.setVisibility(View.GONE);
                            absBut.setEnabled(false);
                            logxBut.setEnabled(false);
                            derBut.setEnabled(false);
                            rootBut.setEnabled(false);
                            funcButt.setEnabled(false);
                            intDefBut.setBackgroundColor(Color.parseColor("#584646"));
                            intDefBut.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else {
                            if ((intFunc.getText().toString().isEmpty() && argInt.getText().toString().isEmpty() && aArgInt.getText().toString().isEmpty() && bArgInt.getText().toString().isEmpty()) ||
                                    (!intFunc.getText().toString().isEmpty() && !argInt.getText().toString().isEmpty() && !aArgInt.getText().toString().isEmpty() && !bArgInt.getText().toString().isEmpty())) {
                                if (!(intFunc.getText().toString().isEmpty() && argInt.getText().toString().isEmpty() && aArgInt.getText().toString().isEmpty() && bArgInt.getText().toString().isEmpty()))
                                    fStr += "int(" + intFunc.getText() + "," + argInt.getText() + "," + bArgInt.getText() + "," + aArgInt.getText() + ")";
                                intFunc.setText("");
                                argInt.setText("");
                                bArgInt.setText("");
                                aArgInt.setText("");
                                tempLL.setVisibility(View.GONE);
                                intDefLL.setVisibility(View.GONE);
                                trigFuncs[7] = !trigFuncs[7];
                                funcLL.setVisibility(View.VISIBLE);
                                resetAllFields();
                                funcText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                                absBut.setEnabled(true);
                                logxBut.setEnabled(true);
                                derBut.setEnabled(true);
                                rootBut.setEnabled(true);
                                funcButt.setEnabled(true);
                                intDefBut.setTextColor(Color.parseColor("#1C1C1C"));
                                intFunc.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                argInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                aArgInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                bArgInt.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                                intDefBut.setBackground(clearBut.getBackground());
                            }
                            else if (intFunc.getText().toString().isEmpty() || argInt.getText().toString().isEmpty() || aArgInt.getText().toString().isEmpty() || bArgInt.getText().toString().isEmpty())
                                Toast.makeText(getActivity(), R.string.tst_fill_in_all_fields, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.sinbut:
                        writingInFunc("sin(");
                        break;
                    case R.id.cosBut:
                        writingInFunc("cos(");
                        break;
                    case R.id.tanBut:
                        writingInFunc("tan(");
                        break;
                    case R.id.ctgBut:
                        writingInFunc("ctan(");
                        break;
                    case R.id.xBut:
                        writingInFunc("x");
                        break;
                    case R.id.xnBut:
                        writingInFunc("^");
                        break;
                    case R.id.lnxBut:
                        writingInFunc("ln(");
                        break;
                    case R.id.eBut:
                        writingInFunc("e");
                        break;
                    case R.id.piBut:
                        writingInFunc("pi");
                        break;
                    case R.id.sqrtxBut:
                        writingInFunc("sqrt(");
                        break;
                    case R.id.dotBut:
                        if (!xVClicked)
                            writingInFunc(".");
                        if (xVClicked) writingInVar(".");
                        break;
                    case R.id.hookOpBut:
                        if (!xVClicked) writingInFunc("(");
                        if (xVClicked) writingInVar("(");
                        break;
                    case R.id.hookClBut:
                        if (!xVClicked) writingInFunc(")");
                        if (xVClicked) writingInVar(")");
                        break;
                    case R.id.but0:
                        if (!xVClicked) writingInFunc("0");
                        if (xVClicked) writingInVar("0");
                        break;
                    case R.id.but1:
                        if (!xVClicked) writingInFunc("1");
                        if (xVClicked) writingInVar("1");
                        break;
                    case R.id.but2:
                        if (!xVClicked) writingInFunc("2");
                        if (xVClicked) writingInVar("2");
                        break;
                    case R.id.but3:
                        if (!xVClicked) writingInFunc("3");
                        if (xVClicked) writingInVar("3");
                        break;
                    case R.id.but4:
                        if (!xVClicked) writingInFunc("4");
                        if (xVClicked) writingInVar("4");
                        break;
                    case R.id.but5:
                        if (!xVClicked) writingInFunc("5");
                        if (xVClicked) writingInVar("5");
                        break;
                    case R.id.but6:
                        if (!xVClicked) writingInFunc("6");
                        if (xVClicked) writingInVar("6");
                        break;
                    case R.id.but7:
                        if (!xVClicked) writingInFunc("7");
                        if (xVClicked) writingInVar("7");
                        break;
                    case R.id.but8:
                        if (!xVClicked) writingInFunc("8");
                        if (xVClicked) writingInVar("8");
                        break;
                    case R.id.but9:
                        if (!xVClicked) writingInFunc("9");
                        if (xVClicked) writingInVar("9");
                        break;
                    case R.id.wipeOffBut:
                        if (argClicked[0] && rootFunc != null && rootFunc.length() > 0)
                            rootFunc.setText(rootFunc.getText().toString().substring(0, rootFunc.getText().length()-1));

                        if (argClicked[1] && aLog != null && aLog.length() > 0)
                            aLog.setText(aLog.getText().toString().substring(0, aLog.getText().length()-1));

                        if (argClicked[2] && rootNLL != null && rootNLL.length() > 0)
                            rootNLL.setText(rootNLL.getText().toString().substring(0, rootNLL.getText().length()-1));

                        if (argClicked[3] && bArgInt != null && bArgInt.length() > 0)
                            bArgInt.setText(bArgInt.getText().toString().substring(0, bArgInt.getText().length()-1));

                        if (argClicked[4] && aArgInt != null && aArgInt.length() > 0)
                            aArgInt.setText(aArgInt.getText().toString().substring(0, aArgInt.getText().length()-1));

                        if (argClicked[5] && argInt != null && argInt.length() > 0)
                            argInt.setText(argInt.getText().toString().substring(0, argInt.getText().length()-1));

                        if (argClicked[6] && argDer != null && argDer.length() > 0)
                            argDer.setText(argDer.getText().toString().substring(0, argDer.getText().length()-1));

                        if (argClicked[7] && logFunc != null && logFunc.length() > 0)
                            logFunc.setText(logFunc.getText().toString().substring(0, logFunc.getText().length()-1));

                        if (argClicked[8] && absFunc != null && absFunc.length() > 0)
                            absFunc.setText(absFunc.getText().toString().substring(0, absFunc.getText().length()-1));

                        if (argClicked[9] && intFunc != null && intFunc.length() > 0)
                            intFunc.setText(intFunc.getText().toString().substring(0, intFunc.getText().length()-1));

                        if (argClicked[10] && derFunc != null && derFunc.length() > 0)
                            derFunc.setText(derFunc.getText().toString().substring(0, derFunc.getText().length()-1));


                        if (!argClicked[0] && !argClicked[1] && !argClicked[2] && !argClicked[3] && !argClicked[4] && !argClicked[5] && !argClicked[6] && !argClicked[7] && !argClicked[8] && !argClicked[9] && !argClicked[10]
                            && !varClicked[0] && !varClicked[1] && !varClicked[2] && !varClicked[3] && !varClicked[4] && !varClicked[5] && !varClicked[6])
                            if (fStr != null && fStr.length() > 0)
                             fStr = fStr.substring(0, fStr.length() - 1);

                        if (varClicked[0])
                           if (xValue != null && xValue.length() > 0)
                               xValue.setText(xValue.getText().toString().substring(0, xValue.length() - 1));
                        if (varClicked[1])
                            if (yValue != null && yValue.length() > 0)
                                yValue.setText(yValue.getText().toString().substring(0, yValue.length() - 1));
                        if (varClicked[2])
                            if (fValue != null && fValue.length() > 0)
                                fValue.setText(fValue.getText().toString().substring(0, fValue.length() - 1));
                        if (varClicked[3])
                            if (gValue != null && gValue.length() > 0)
                                gValue.setText(gValue.getText().toString().substring(0, gValue.length() - 1));
                        if (varClicked[4])
                            if (aValue != null && aValue.length() > 0)
                                aValue.setText(aValue.getText().toString().substring(0, aValue.length() - 1));
                        if (varClicked[5])
                            if (bValue != null && bValue.length() > 0)
                                bValue.setText(bValue.getText().toString().substring(0, bValue.length() - 1));
                        if (varClicked[6])
                            if (tValue != null && tValue.length() > 0)
                                tValue.setText(tValue.getText().toString().substring(0, tValue.length() - 1));
                        break;
                    default:
                        break;
                }
                funcText.setText(fStr);
            }
        };
        derBut.setOnClickListener(clickListener);
        funcButt.setOnClickListener(clickListener);
        plusBut.setOnClickListener(clickListener);
        minusBut.setOnClickListener(clickListener);
        multBut.setOnClickListener(clickListener);
        divBut.setOnClickListener(clickListener);
        xBut.setOnClickListener(clickListener);
        xnBut.setOnClickListener(clickListener);
        lnxBut.setOnClickListener(clickListener);
        logxBut.setOnClickListener(clickListener);
        eBut.setOnClickListener(clickListener);
        piBut.setOnClickListener(clickListener);
        sinBut.setOnClickListener(clickListener);
        cosBut.setOnClickListener(clickListener);
        tanBut.setOnClickListener(clickListener);
        ctgBut.setOnClickListener(clickListener);
        hookOpBut.setOnClickListener(clickListener);
        hookClBut.setOnClickListener(clickListener);
        dotBut.setOnClickListener(clickListener);
        wipeOffBut.setOnClickListener(clickListener);
        but1.setOnClickListener(clickListener);
        but2.setOnClickListener(clickListener);
        but3.setOnClickListener(clickListener);
        but4.setOnClickListener(clickListener);
        but5.setOnClickListener(clickListener);
        but6.setOnClickListener(clickListener);
        but7.setOnClickListener(clickListener);
        but8.setOnClickListener(clickListener);
        but9.setOnClickListener(clickListener);
        but0.setOnClickListener(clickListener);
        xVButOK.setOnClickListener(clickListener);
        commaBut.setOnClickListener(clickListener);
        perCentBut.setOnClickListener(clickListener);
        yBut.setOnClickListener(clickListener);
        fBut.setOnClickListener(clickListener);
        gBut.setOnClickListener(clickListener);
        aBut.setOnClickListener(clickListener);
        bBut.setOnClickListener(clickListener);
        tBut.setOnClickListener(clickListener);
        log10xBut.setOnClickListener(clickListener);
        rootBut.setOnClickListener(clickListener);
        notBut.setOnClickListener(clickListener);
        lessBut.setOnClickListener(clickListener);
        moreBut.setOnClickListener(clickListener);
        equalsBut.setOnClickListener(clickListener);
        braketOpBut.setOnClickListener(clickListener);
        braketClBut.setOnClickListener(clickListener);
        absBut.setOnClickListener(clickListener);
        intDefBut.setOnClickListener(clickListener);
        clearBut.setOnClickListener(clickListener);
        notEqualsBut.setOnClickListener(clickListener);
        sqrtxBut.setOnClickListener(clickListener);
        addExp.setOnClickListener(clickListener);
        copyBut.setOnClickListener(clickListener);

        return v;
    }

    //убрать указатель с выбранного функционального поля
    public void resetAllFields(){
        for (int i=0; i < argClicked.length; i++)
            argClicked[i] = false;
    }

    //запись значения, веденного с клавиатуры в соответствующее поле
    public void writingInFunc(String s) {
        if (argClicked[0])
            rootFunc.setText(rootFunc.getText() + s);
        if (argClicked[1])
            aLog.setText(aLog.getText() + s);
        if (argClicked[2])
            rootNLL.setText(rootNLL.getText() + s);
        if (argClicked[3])
            bArgInt.setText(bArgInt.getText() + s);
        if (argClicked[4])
            aArgInt.setText(aArgInt.getText() + s);
        if (argClicked[5])
            argInt.setText(argInt.getText() + s);
        if (argClicked[6])
            argDer.setText(argDer.getText() + s);
        if (argClicked[7])
            logFunc.setText(logFunc.getText() + s);
        if (argClicked[8])
            absFunc.setText(absFunc.getText() + s);
        if (argClicked[9])
            intFunc.setText(intFunc.getText() + s);
        if (argClicked[10])
            derFunc.setText(derFunc.getText() + s);

        if (!argClicked[0] && !argClicked[1] && !argClicked[2] && !argClicked[3] && !argClicked[4] && !argClicked[5] && !argClicked[6] && !argClicked[7] && !argClicked[8] && !argClicked[9] && !argClicked[10]) {
            fStr += s;
            funcText.setText(funcText.getText() + s);
        }
    }

    //запись значения, веденного с клавиатуры в соответствующее поле
    public void writingInVar(String s) {
        if (varClicked[0])
            xValue.setText(xValue.getText()+s);
        if (varClicked[1])
            yValue.setText(yValue.getText()+s);
        if (varClicked[2])
            fValue.setText(fValue.getText()+s);
        if (varClicked[3])
            gValue.setText(gValue.getText()+s);
        if (varClicked[4])
            aValue.setText(aValue.getText()+s);
        if (varClicked[5])
            bValue.setText(bValue.getText()+s);
        if (varClicked[6])
            tValue.setText(tValue.getText()+s);
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
