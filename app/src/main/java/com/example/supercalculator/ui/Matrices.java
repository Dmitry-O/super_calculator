package com.example.supercalculator.ui;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supercalculator.MainActivity;
import com.example.supercalculator.Matrix;
import com.example.supercalculator.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */

//раздел "Матрицы"
public class Matrices extends Fragment {
    String[] actions = {"A+B", "A-B", "B-A", "ƛA", "ƛB", "A*B", "B*A", "Aᵀ", "Bᵀ", "|A|", "|B|", "A⁻ᶦ", "B⁻ᶦ"};
    String actionString, tempAnswer, tempErrors, copyStr="";
    TableLayout tlMatrixA, tlMatrixB;
    int aRows=1, aColumns=1, bRows=1, bColumns=1, id=0, idExp=0, idAnswer=0, scrollCount=0, idErrors=0, tARows=1, tAColumns=1, tBRows=1, tBColumns=1;
    EditText lamb;
    double l, tempL=0;
    EditText A1, A2, B1, B2;
    Matrix matrix;
    Button buttonA, buttonB, calculateButton, clearBut, aGetC, bGetC, lGetConst;
    LinearLayout linearLayoutA, linearLayoutB, containerAnswers, matrMainLL;
    View view;
    int idMatrix[][], decTemp;
    double[][] A, B, C;
    EditText[][] matrixA, matrixB;
    boolean bTransp = false, itsConst=false, toVibr = false, dark = false, aClicked = false, bClicked = false;
    File file;
    ScrollView scrollView;
    DecimalFormat df;
    TextView tvLambda;
    MainActivity main;
    ImageButton copyBut;

    public Matrices() {
        // Required empty public constructor
    }

    private void scrollDialogDown() {
        scrollView.post(new Runnable() {

            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    //UI: установка необходимой цветовой гаммы
    //в соответствии с фоном приложения
    public void setColors(int color) {
        if (color == Color.WHITE) {
            dark = true;
            matrMainLL.setBackgroundColor(Color.parseColor("#252525"));
            linearLayoutA.setBackgroundColor(Color.parseColor("#252525"));
            linearLayoutB.setBackgroundColor(Color.parseColor("#252525"));
        }
        else {
            matrMainLL.setBackgroundColor(Color.WHITE);
            linearLayoutA.setBackgroundColor(Color.WHITE);
            linearLayoutB.setBackgroundColor(Color.WHITE);
        }

        A1.setTextColor(color);
        A2.setTextColor(color);
        B1.setTextColor(color);
        B2.setTextColor(color);
        A1.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        A2.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        B1.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        B2.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        tvLambda.setTextColor(color);
        lamb.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        lamb.setTextColor(color);
        copyBut.getDrawable().setTint(color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matrices, container, false);
        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, actions);

        file = new File("history");

        actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        buttonA = (Button)view.findViewById(R.id.buttonA);
        buttonB = (Button)view.findViewById(R.id.buttonB);

        copyBut = view.findViewById(R.id.copyBut1);
        copyBut.setEnabled(false);

        scrollView = view.findViewById(R.id.scroll);

        clearBut = view.findViewById(R.id.clearBut);
        calculateButton = view.findViewById(R.id.button);
        lGetConst = view.findViewById(R.id.lGetConst);

        containerAnswers = view.findViewById(R.id.containerAnswers);

        Spinner spActions = view.findViewById(R.id.listActions);

        spActions.setAdapter(actionsAdapter);

        spActions.setOnItemSelectedListener(onItemSelectedListener());


        tvLambda = view.findViewById(R.id.textView7);

        tlMatrixA = view.findViewById(R.id.tlMatrixA);
        tlMatrixB = view.findViewById(R.id.tlMatrixB);

        lamb = view.findViewById(R.id.lambda);
        lamb.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);

        tempAnswer="";

        Drawable spinnerDrawable = spActions.getBackground().getConstantState().newDrawable();

        A1 = view.findViewById(R.id.A1);
        A2 = view.findViewById(R.id.A2);
        B1 = view.findViewById(R.id.B1);
        B2 = view.findViewById(R.id.B2);

        matrMainLL = view.findViewById(R.id.matrMainLL);

        aGetC = view.findViewById(R.id.aGetC);
        bGetC = view.findViewById(R.id.bGetC);

        aRows = Integer.parseInt(A1.getText().toString());
        aColumns = Integer.parseInt(A2.getText().toString());
        bRows = Integer.parseInt(B1.getText().toString());
        bColumns = Integer.parseInt(B2.getText().toString());

        Button mA1 = view.findViewById(R.id.mA1);
        Button pA1 = view.findViewById(R.id.pA1);
        Button mA2 = view.findViewById(R.id.mA2);
        Button pA2 = view.findViewById(R.id.pA2);

        Button mB1 = view.findViewById(R.id.mB1);
        Button pB1 = view.findViewById(R.id.pB1);
        Button mB2 = view.findViewById(R.id.mB2);
        Button pB2 = view.findViewById(R.id.pB2);

        linearLayoutA = view.findViewById(R.id.llA);
        linearLayoutB = view.findViewById(R.id.llB);

        //чтение значения строк в текстовом файле "settings"
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                if (temp==1) {
                    if (line.contains("d")) {
                        spinnerDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        spActions.setBackground(spinnerDrawable);
                        setColors(Color.WHITE);
                    }
                    if (line.contains("w")) {
                        spinnerDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                        spActions.setBackground(spinnerDrawable);
                        setColors(Color.BLACK);
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

        //создание матрицы по умолчанию (1х1)
        createMatrix(false, true);

        //обработка нажатий на различные кнопки в разделе
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (toVibr) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vib.vibrate(VibrationEffect.createOneShot(20, 1));
                    } else {
                        //deprecated in API 26
                        vib.vibrate(20);
                    }
                }
                switch (v.getId()) {
                    case R.id.copyBut1:
                        ((ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE)).setText(copyStr);
                        Toast.makeText(getContext(), R.string.copied_answer, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.clearBut:
                        containerAnswers.removeAllViews();
                        containerAnswers.scrollTo(0, 0);
                        scrollCount=0;
                        break;
                    case R.id.mA1:
                        if (aRows>1) aRows--;
                        createMatrix(true, false);
                        aGetC.setEnabled(false);
                        break;
                    case R.id.pA1:
                        if(aRows<20) aRows++;
                        createMatrix(true, false);
                        aGetC.setEnabled(false);
                        break;
                    case R.id.mA2:
                        if (aColumns>1) aColumns--;
                        createMatrix(true, false);
                        aGetC.setEnabled(false);
                        break;
                    case R.id.pA2:
                        if (aColumns<20) aColumns++;
                        createMatrix(true, false);
                        aGetC.setEnabled(false);
                        break;

                    case R.id.mB1:
                        if (bRows>1) bRows--;
                        createMatrix(false, false);
                        bGetC.setEnabled(false);
                        break;
                    case R.id.pB1:
                        if(bRows<20) bRows++;
                        createMatrix(false, false);
                        bGetC.setEnabled(false);
                        break;
                    case R.id.mB2:
                        if (bColumns>1) bColumns--;
                        createMatrix(false, false);
                        bGetC.setEnabled(false);
                        break;
                    case R.id.pB2:
                        if (bColumns<20) bColumns++;
                        createMatrix(false, false);
                        bGetC.setEnabled(false);
                        break;
                        
                    case R.id.buttonA:
                        aClicked = !aClicked;
                        if (aClicked) {
                            linearLayoutA.setVisibility(view.VISIBLE);
                            scrollView.setVisibility(view.GONE);
                            buttonB.setEnabled(false);
                            buttonA.setTextColor(Color.WHITE);
                            buttonB.setTextColor(Color.BLACK);
                        }
                        else {
                            linearLayoutA.setVisibility(view.GONE);
                            scrollView.setVisibility(view.VISIBLE);
                            buttonB.setEnabled(true);
                            buttonA.setTextColor(Color.BLACK);
                        }
                        break;
                    case R.id.buttonB:
                        bClicked = !bClicked;
                        if (bClicked) {
                            linearLayoutB.setVisibility(view.VISIBLE);
                            scrollView.setVisibility(view.GONE);
                            buttonA.setEnabled(false);
                            buttonB.setTextColor(Color.WHITE);
                        }
                        else {
                            linearLayoutB.setVisibility(view.GONE);
                            scrollView.setVisibility(view.VISIBLE);
                            buttonA.setEnabled(true);
                            buttonB.setTextColor(Color.BLACK);
                        }
                        break;
                    case R.id.button:
                        tempErrors="";
                        onButtonClick();
                        break;
                    case R.id.aGetC:
                        if (!bTransp)
                            for(int i=0; i<matrixA.length; i++)
                                for(int j=0; j<matrixA[0].length; j++)
                                    matrixA[i][j].setText(Double.toString(C[i][j]));
                        aGetC.setEnabled(false);
                        break;
                    case R.id.bGetC:
                        if (!bTransp)
                            for(int i=0; i<matrixB.length; i++)
                                for(int j=0; j<matrixB[0].length; j++)
                                    matrixB[i][j].setText(Double.toString(C[i][j]));
                        bGetC.setEnabled(false);
                        break;
                    case R.id.lGetConst:
                        lamb.setText(Double.toString(tempL));
                        lGetConst.setEnabled(false);
                        break;
                }
                A1.setText(Integer.toString(aRows));
                A2.setText(Integer.toString(aColumns));
                B1.setText(Integer.toString(bRows));
                B2.setText(Integer.toString(bColumns));

                A1.setText(Integer.toString(aRows));
                A2.setText(Integer.toString(aColumns));
                B1.setText(Integer.toString(bRows));
                B2.setText(Integer.toString(bColumns));
            }
        };
        mA1.setOnClickListener(clickListener);
        pA1.setOnClickListener(clickListener);
        mA2.setOnClickListener(clickListener);
        pA2.setOnClickListener(clickListener);
        copyBut.setOnClickListener(clickListener);

        mB1.setOnClickListener(clickListener);
        pB1.setOnClickListener(clickListener);
        mB2.setOnClickListener(clickListener);
        pB2.setOnClickListener(clickListener);

        calculateButton.setOnClickListener(clickListener);
        buttonA.setOnClickListener(clickListener);
        buttonB.setOnClickListener(clickListener);
        clearBut.setOnClickListener(clickListener);
        aGetC.setOnClickListener(clickListener);
        bGetC.setOnClickListener(clickListener);
        lGetConst.setOnClickListener(clickListener);

        return view;
    }

    //метод динамического создания матриц А и В
    public void createMatrix(boolean A, boolean common) {
        id=-10000070;

        int color;
        if (dark)
            color = Color.WHITE;
        else color = Color.BLACK;

        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(50, 50);

        //пересоздание матрицы А
        if (A) {
            tlMatrixA.removeAllViews();
            matrixA = new EditText[aRows][aColumns];
            for (int i = 0; i < aRows; i++) {
                TableRow tableRow = new TableRow(getActivity());
                tlMatrixA.addView(tableRow);
                for (int j = 0; j < aColumns; j++) {
                    EditText text = new EditText(getActivity());
                    text.setId(id);
                    text.setTextColor(color);
                    text.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    text.setText("0");
                    tableRow.addView(text);
                    text.getLayoutParams().width=100;
                    text.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    matrixA[i][j] = text;
                    id--;
                }
            }
        }

        //пересоздание матрицы В
        if (!A &&!common) {
            tlMatrixB.removeAllViews();
            matrixB = new EditText[bRows][bColumns];
            for (int i = 0; i < bRows; i++) {
                TableRow tableRow = new TableRow(getActivity());
                tlMatrixB.addView(tableRow);
                for (int j = 0; j < bColumns; j++) {
                    EditText text = new EditText(getActivity());
                    text.setText("0");
                    text.setId(id);
                    text.setTextColor(color);
                    text.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    tableRow.addView(text);
                    text.getLayoutParams().width=100;
                    text.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    matrixB[i][j] = text;
                    id--;
                }
            }
        }

        //пересоздание обеих матриц
        if (!A && common) {
            tlMatrixA.removeAllViews();
            tlMatrixB.removeAllViews();
            matrixA = new EditText[aRows][aColumns];
            matrixB = new EditText[bRows][bColumns];
            for (int i=0; i<aRows; i++) {
                TableRow tableRow = new TableRow(getActivity());
                tlMatrixA.addView(tableRow);
                for (int j = 0; j < aColumns; j++) {
                    EditText text = new EditText(getActivity());
                    text.setId(id);
                    text.setTextColor(color);
                    text.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    text.setText("0");
                    tableRow.addView(text);
                    text.getLayoutParams().width=100;
                    text.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    matrixA[i][j] = text;
                    id--;
                }
            }
            for (int i=0; i<bRows; i++) {
                TableRow tableRow = new TableRow(getActivity());
                tlMatrixB.addView(tableRow);
                for (int j = 0; j < bColumns; j++) {
                    EditText text = new EditText(getActivity());
                    text.setText("0");
                    text.setId(id);
                    text.setTextColor(color);
                    text.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    tableRow.addView(text);
                    text.getLayoutParams().width=100;
                    text.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    matrixB[i][j] = text;
                    id--;
                }
            }
        }
    }

    //метод нажатия на кнопку вычисления значений матриц
    public void onButtonClick() {
        A = new double[aRows][aColumns];
        B = new double[bRows][bColumns];

        bTransp = itsConst = false;

        for (int i=0; i<aRows; i++)
            for (int j=0; j<aColumns; j++)
                A[i][j] = Double.parseDouble(matrixA[i][j].getText().toString());
        for (int i=0; i<bRows; i++)
            for (int j=0; j<bColumns; j++)
                B[i][j] = Double.parseDouble(matrixB[i][j].getText().toString());

        switch (actionString) {
            case "A+B":
                if (aRows==bRows && aColumns==bColumns) {
                    tempAnswer = matrix.toString(matrix.addition(A, B));
                    C = new double[aRows][aColumns];
                    C = matrix.addition(A, B);
                    aGetC.setEnabled(true);
                    bGetC.setEnabled(true);
                }
                else
                    tempErrors = getString(R.string.matr_errors_plus);
                break;
            case "A-B":
                if (aRows==bRows && aColumns==bColumns) {
                    tempAnswer = matrix.toString(matrix.subtraction(A, B));
                    C = new double[aRows][aColumns];
                    C = matrix.subtraction(A, B);
                    aGetC.setEnabled(true);
                    bGetC.setEnabled(true);
                }
                else
                    tempErrors = getString(R.string.matr_errors_minus);
                break;
            case "B-A":
                if (aRows==bRows && aColumns==bColumns) {
                    tempAnswer = matrix.toString(matrix.subtraction(B, A));
                    C = new double[aRows][aColumns];
                    C = matrix.subtraction(B, A);
                    aGetC.setEnabled(true);
                    bGetC.setEnabled(true);
                }
                else
                    tempErrors = getString(R.string.matr_errors_minus);
                break;
            case "ƛA":
                if (lamb.getText().toString().length()==0) {
                    lamb.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    tempErrors = getString(R.string.matr_errors_lambda);
                    break;
                }
                if (lamb.getText().toString().length()!=0) {
                    l = Double.parseDouble(lamb.getText().toString());
                    tempAnswer = matrix.toString(matrix.constMultiply(l, A));
                    C = new double[aRows][aColumns];
                    C = matrix.constMultiply(l, A);
                    aGetC.setEnabled(true);
                    lamb.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                    break;
                }
            case "ƛB":
                if (lamb.getText().toString().length()==0) {
                    lamb.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    tempErrors = getString(R.string.matr_errors_lambda);
                    break;
                }
                if (lamb.getText().toString().length()!=0) {
                    l = Double.parseDouble(lamb.getText().toString());
                    tempAnswer = matrix.toString(matrix.constMultiply(l, B));
                    C = new double[bRows][bColumns];
                    C = matrix.constMultiply(l, B);
                    bGetC.setEnabled(true);
                    lamb.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
                    break;
                }
            case "A*B":
                if (aRows==bColumns) {
                    tempAnswer = matrix.toString(matrix.multiply(A, B));
                    C = new double[aRows][bColumns];
                    C = matrix.multiply(A, B);
                    aGetC.setEnabled(true);
                    bGetC.setEnabled(true);
                }
                else
                    tempErrors = getString(R.string.matr_errors_mult);
                break;
            case "B*A":
                if (bRows==aColumns) {
                    tempAnswer = matrix.toString(matrix.multiply(B, A));
                    C = new double[bRows][aColumns];
                    matrix.multiply(B, A);
                    aGetC.setEnabled(true);
                    bGetC.setEnabled(true);
                }
                else
                    tempErrors = getString(R.string.matr_errors_mult);
            case "Aᵀ":
                tempAnswer = matrix.toString(matrix.transpose(A));
                bTransp=true;
                break;
            case "Bᵀ":
                tempAnswer = matrix.toString(matrix.transpose(B));
                bTransp=true;
                break;
            case "|A|":
                if (aRows==aColumns) {
                    tempAnswer = Double.toString(matrix.determinant(A));
                    tempL = matrix.determinant(A);
                    lGetConst.setEnabled(true);
                    itsConst=true;
                }
                else
                    tempErrors = getString(R.string.matr_errors_det);
                break;
            case "|B|":
                if (bRows==bColumns) {
                    tempAnswer = Double.toString(matrix.determinant(B));
                    tempL = matrix.determinant(B);
                    lGetConst.setEnabled(true);
                    itsConst=true;
                }
                else
                    tempErrors = getString(R.string.matr_errors_det);
                break;
            case "A⁻ᶦ":
                if (aRows==aColumns) {
                    tempAnswer = matrix.toString(matrix.invert(A));
                    C = new double[aRows][aColumns];
                    C = matrix.invert(A);
                    aGetC.setEnabled(true);
                }
                else
                    tempErrors = getString(R.string.matr_errors_inv);
                break;
            case "B⁻ᶦ":
                if (bRows==bColumns) {
                    tempAnswer = matrix.toString(matrix.invert(B));
                    C = new double[bRows][bColumns];
                    C = matrix.invert(B);
                    bGetC.setEnabled(true);
                }
                else
                    tempErrors = getString(R.string.matr_errors_inv);
                break;
        }
        TextView expression = new TextView(getActivity());
        TextView result;
        TextView error;

        String fileStr="";

        //вывод результатов вычислений на экран

        if (!itsConst)
            expression.setText(getString(R.string.matr_expr_c) + actionString);
        else expression.setText(getString(R.string.matr_expr_const) + actionString);
        fileStr = expression.getText().toString() + "\n";
        expression.setId(idExp);
        expression.setTextColor(Color.parseColor("#03A9F4"));
        containerAnswers.addView(expression);
        idExp++;

        String decString="k";

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

        if(!tempAnswer.isEmpty()) {
            result = new TextView(getActivity());
            if (!(tempAnswer.contains("N") || tempAnswer.contains("I"))) {
                if (!itsConst)
                    result.setText(getString(R.string.answer_for_matrices) + "\n" + tempAnswer);
                else {
                    if (decTemp==-1)
                        result.setText(getString(R.string.matr_vect_answer) + Double.parseDouble(tempAnswer));
                    else result.setText(getString(R.string.matr_vect_answer) + df.format(Double.parseDouble(tempAnswer)));
                }
            }
            if (tempAnswer.contains("N") || tempAnswer.contains("I"))
                result.setText(getString(R.string.theres_no_inverse));
            fileStr = fileStr + result.getText().toString() + "\n";
            result.setId(idAnswer);
            result.setTextColor(Color.parseColor("#FF4040"));
            containerAnswers.addView(result);
            idAnswer++;
            copyStr = tempAnswer;
            copyBut.setEnabled(true);
        }

        if(!tempErrors.isEmpty()) {
            error = new TextView(getActivity());
            error.setText(getString(R.string.error) + tempErrors);
            fileStr = fileStr + error.getText().toString() + "\n";
            error.setId(idErrors);
            error.setTextColor(Color.parseColor("#FF9800"));
            containerAnswers.addView(error);
            idErrors++;
            copyBut.setEnabled(false);
        }

        scrollDialogDown();

        //запись данных в текстовый файл "history"
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getContext().openFileOutput("history", Context.MODE_PRIVATE | Context.MODE_APPEND)));
            writer.append(fileStr);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}


