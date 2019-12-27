package com.example.supercalculator.ui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supercalculator.MainActivity;
import com.example.supercalculator.R;
import com.google.android.material.navigation.NavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */

//раздел "Настройки"
public class Settings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button roundBtn, decBtn, vibrBtn, darkThBtn, resetSettBtn, reportBut, sendReportBut;
    ImageButton goBack, pickFileBut, unattachFilebut;
    SeekBar seekBar;
    ImageView imTheme, imDec, imVib, imRes, imRep, imageLoaded, imV5;
    TextView textView, tV2, tV3, tV4, tV5, tV6, tV40, tv9;
    Dialog dialog;
    String[] settings;
    Switch vibSwitch, darkthSwitch;
    LinearLayout settLL, reportLL, roundLL;
    EditText reportText;
    boolean smthChanged = false, decChanged = false, unattached = true;
    Intent mStartActivity;
    AlarmManager mgr;
    PendingIntent mPendingIntent;
    String src;
    Uri uri;
    File outputDir;
    int idPB = -1000131;
    FragmentActivity frAct;
    TimerTask timerTask;
    Timer timer;
    int counter=5, progress=1;
    ProgressBar pb;
    //Toolbar toolbar;

    private OnFragmentInteractionListener mListener;

    //отображение Toast'ов посекундно во время перезагрузки приложения
    public void showToasts() {
        timer = new Timer();
        timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        frAct.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                if (counter>0) {
                                    Toast.makeText(frAct, Integer.toString(counter), Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.VISIBLE);
                                    pb.setProgress(20*progress);
                                    progress++;
                                }
                                if (counter==0) {
                                    Drawable dr;
                                    Resources res = frAct.getResources();
                                    dr = res.getDrawable(R.drawable.ic_check_black_24dp);
                                    Toast.makeText(frAct, R.string.done_reloading, Toast.LENGTH_SHORT).show();
                                    pb.setForeground(dr);
                                    frAct.finish();
                                }
                                counter--;
                            }
                        });
                    }
                };
        timer.schedule(timerTask, 0, 1000);
    }

    //перезапуск приложения
    public void reload() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        pb = frAct.findViewById(R.id.pb);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), mPendingIntent);
                        showToasts();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.restarting_app).setPositiveButton("OK", dialogClickListener)
                .setCancelable(false).show();
    }

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
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

    public Menu menu;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        getActivity().getMenuInflater().inflate(R.menu.activity_main_drawer, this.menu);
    }

    //UI: установка необходимой цветовой гаммы
    //в соответствии с фоном приложения
    public void setThemeSettings(int color) {
        if (color == Color.WHITE) {
            settLL.setBackgroundColor(Color.parseColor("#252525"));
            roundLL.setBackgroundColor(Color.parseColor("#252525"));
            reportLL.setBackgroundColor(Color.parseColor("#252525"));
        }
        if (color == Color.BLACK) {
            settLL.setBackgroundColor(Color.WHITE);
            roundLL.setBackgroundColor(Color.WHITE);
            reportLL.setBackgroundColor(Color.WHITE);
        }
        darkThBtn.setTextColor(color);
        decBtn.setTextColor(color);
        vibrBtn.setTextColor(color);
        resetSettBtn.setTextColor(color);
        reportBut.setTextColor(color);
        imTheme.getDrawable().setTint(color);
        imDec.getDrawable().setTint(color);
        imVib.getDrawable().setTint(color);
        imRes.getDrawable().setTint(color);
        imRep.getDrawable().setTint(color);
        textView.setTextColor(color);
        roundBtn.setTextColor(color);
        tV40.setTextColor(color);
        goBack.getDrawable().setTint(color);
        tv9.setTextColor(color);
        imageLoaded.getDrawable().setTint(color);
        pickFileBut.getDrawable().setTint(color);
        sendReportBut.setTextColor(color);
        //navLL.setBackgroundColor(color);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_settings, container, false);

        dialog = new Dialog(getActivity());
        LayoutInflater inflater1 = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater1.inflate(R.layout.round_dialog, (ViewGroup) v.findViewById(R.id.round_LL));
        dialog.setContentView(layout);
        dialog.setCancelable(false);

        settings = new String[4];

        roundBtn = layout.findViewById(R.id.round_btn);
        roundLL = layout.findViewById(R.id.round_LL);
        seekBar = layout.findViewById(R.id.seekBar);
        textView = layout.findViewById(R.id.round_text);
        decBtn = v.findViewById(R.id.dec_btn);
        vibrBtn = v.findViewById(R.id.vibrBtn);
        vibSwitch = v.findViewById(R.id.vibSwitch);
        darkThBtn = v.findViewById(R.id.darkThBtn);
        darkthSwitch = v.findViewById(R.id.darkThSwitch);
        resetSettBtn = v.findViewById(R.id.resetSettBtn);
        reportBut = v.findViewById(R.id.reportBut);
        sendReportBut = v.findViewById(R.id.sendReportBut);
        goBack = v.findViewById(R.id.goBack);
        reportText = v.findViewById(R.id.reportText);
        settLL = v.findViewById(R.id.settLL);
        reportLL = v.findViewById(R.id.reportLL);
        imTheme = v.findViewById(R.id.imTheme);
        imDec = v.findViewById(R.id.imDec);
        imVib = v.findViewById(R.id.imVib);
        imRes = v.findViewById(R.id.imRes);
        imRep = v.findViewById(R.id.imRep);
        tv9 = v.findViewById(R.id.textView9);
        imageLoaded = v.findViewById(R.id.imageLoaded);
        imV5 = v.findViewById(R.id.imageView5);
        unattachFilebut = v.findViewById(R.id.unattachFileBut);

        tV2 = v.findViewById(R.id.tV2);
        tV3 = v.findViewById(R.id.tV3);
        tV4 = v.findViewById(R.id.tV4);
        tV5 = v.findViewById(R.id.tV5);
        tV6 = v.findViewById(R.id.tV6);
        tV40 = v.findViewById(R.id.textView40);

        pickFileBut = v.findViewById(R.id.pickFileBut);

        mStartActivity = new Intent(getActivity(), MainActivity.class);
        int mPendingIntentId = 123456;
        mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //чтение значения строк в текстовом файле "settings"
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                settings[temp] = line;
                if (temp == 1) {
                    if (settings[temp].contains("d")) {
                        settLL.setBackgroundColor(Color.parseColor("#252525"));
                        setThemeSettings(Color.WHITE);
                        darkthSwitch.setChecked(true);
                    }
                    if (settings[temp].contains("w")) {
                        settLL.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        setThemeSettings(Color.BLACK);
                        darkthSwitch.setChecked(false);
                    }
                }
                if (temp==3) {
                    if (line.contains("t"))
                        vibSwitch.setChecked(true);
                    if (line.contains("f"))
                        vibSwitch.setChecked(false);
                }
                temp++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textView.setText(settings[2]);

        //обработка нажатий на различные кнопки в разделе
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.round_btn:
                        dialog.dismiss();
                        settings[2] = textView.getText().toString();
                        if(decChanged) {
                            Toast.makeText(getActivity(), R.string.tst_app_will_reboot, Toast.LENGTH_LONG).show();
                            decChanged = false;
                        }
                        break;
                    case R.id.dec_btn:
                        dialog.show();
                        break;
                    case R.id.vibrBtn:
                        vibSwitch.setChecked(!vibSwitch.isChecked());
                        if (vibSwitch.isChecked()) {
                            settings[3] = "true";
                            Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vib.vibrate(VibrationEffect.createOneShot(100, 1));
                            } else {
                                //deprecated in API 26
                                vib.vibrate(20);
                            }
                            smthChanged = true;
                            Toast.makeText(getActivity(), R.string.tst_app_will_reboot, Toast.LENGTH_LONG).show();
                            break;
                        }
                        else {
                            settings[3] = "false";
                            Toast.makeText(getActivity(), R.string.tst_app_will_reboot, Toast.LENGTH_LONG).show();
                            smthChanged = true;
                            break;
                        }
                    case R.id.vibSwitch:
                        if (vibSwitch.isChecked()) {
                            settings[3] = "true";
                            Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vib.vibrate(VibrationEffect.createOneShot(100, 1));
                            } else {
                                vib.vibrate(20);
                            }
                            smthChanged = true;
                            Toast.makeText(getActivity(), R.string.tst_app_will_reboot, Toast.LENGTH_LONG).show();
                            break;
                        }
                        else {
                            settings[3] = "false";
                            Toast.makeText(getActivity(), R.string.tst_app_will_reboot, Toast.LENGTH_LONG).show();
                            smthChanged = true;
                            break;
                        }
                    case R.id.darkThBtn:
                    case R.id.darkThSwitch:
                        if (view.getId()==R.id.darkThBtn)
                            darkthSwitch.setChecked(!darkthSwitch.isChecked());
                        if (darkthSwitch.isChecked()) {
                            settings[1] = "dark";
                            setThemeSettings(Color.WHITE);
                            Toast.makeText(getActivity(), R.string.tst_app_will_reboot, Toast.LENGTH_LONG).show();
                            smthChanged = true;
                            break;
                        }
                        else {
                            settings[1] = "white";
                            setThemeSettings(Color.BLACK);
                            Toast.makeText(getActivity(), R.string.tst_app_will_reboot, Toast.LENGTH_LONG).show();
                            smthChanged = true;
                            break;
                        }
                    case R.id.resetSettBtn:
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        settings[0] = "rus";
                                        settings[1] = "dark";
                                        settings[2] = "-1";
                                        settings[3] = "true";
                                        reload();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(R.string.sett_r_u_sure_reset).setPositiveButton(R.string.exit_yes, dialogClickListener)
                                .setNegativeButton(R.string.exit_no, dialogClickListener).show();
                        break;
                    case R.id.reportBut:
                        reportLL.setVisibility(View.VISIBLE);
                        break;
                    case R.id.sendReportBut:
                        if (reportText.getText().toString().isEmpty())
                            Toast.makeText(getActivity(), R.string.tst_enter_report_first, Toast.LENGTH_LONG).show();
                        else {
                            if(!unattached) {
                                outputDir = new File(src);
                                StrictMode.VmPolicy.Builder builder3 = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder3.build());
                                Intent email = new Intent();
                                email.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                email.setAction(Intent.ACTION_SEND);
                                email.setType("image/*");
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dimasic9420@gmail.com"});
                                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.issue));
                                email.putExtra(Intent.EXTRA_TEXT, reportText.getText().toString());
                                email.putExtra(Intent.EXTRA_STREAM, uri); // Uri.fromFile(outputDir));
                                unattachFilebut.setVisibility(View.GONE);
                                imV5.setVisibility(View.GONE);
                                tv9.setVisibility(View.GONE);
                                imageLoaded.setVisibility(View.GONE);
                                unattached = true;
                                getContext().startActivity(Intent.createChooser(email, getString(R.string.about_choose_app)));
                            }
                            else {
                                Intent email;
                                email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dimasic9420@gmail.com"});
                                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.issue));
                                email.putExtra(Intent.EXTRA_TEXT, reportText.getText().toString());
                                email.setType("message/rfc822");
                                getContext().startActivity(Intent.createChooser(email, getString(R.string.about_choose_app)));
                            }
                            reportText.setText("");
                            reportLL.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.goBack:
                        reportText.setText("");
                        unattachFilebut.setVisibility(View.GONE);
                        imV5.setVisibility(View.GONE);
                        tv9.setVisibility(View.GONE);
                        imageLoaded.setVisibility(View.GONE);
                        unattached = true;
                        reportLL.setVisibility(View.GONE);
                        break;
                    case R.id.pickFileBut:
                        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFile.setType("image/*");
                        chooseFile = Intent.createChooser(chooseFile, getString(R.string.choose_file));
                        startActivityForResult(chooseFile, 1);
                        break;
                    case R.id.unattachFileBut:
                        unattachFilebut.setVisibility(View.GONE);
                        imV5.setVisibility(View.GONE);
                        tv9.setVisibility(View.GONE);
                        imageLoaded.setVisibility(View.GONE);
                        unattached = true;
                        break;
                }
            }
        };
        roundBtn.setOnClickListener(clickListener);
        decBtn.setOnClickListener(clickListener);
        darkthSwitch.setOnClickListener(clickListener);
        darkThBtn.setOnClickListener(clickListener);
        vibSwitch.setOnClickListener(clickListener);
        vibrBtn.setOnClickListener(clickListener);
        resetSettBtn.setOnClickListener(clickListener);
        reportBut.setOnClickListener(clickListener);
        sendReportBut.setOnClickListener(clickListener);
        goBack.setOnClickListener(clickListener);
        pickFileBut.setOnClickListener(clickListener);
        unattachFilebut.setOnClickListener(clickListener);


        final int step = 1;
        final int max = 15;
        final int min = -1;

        frAct = getActivity();

        seekBar.setMax( (max - min) / step );

        seekBar.setProgress((Integer.parseInt(settings[2])-min)/step);

        //работа с Seekbar
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser)
                    {
                        textView.setText(Integer.toString(min + (progress * step)));
                        settings[2] = textView.getText().toString();
                        smthChanged = true;
                        decChanged = true;
                    }
                }
        );


        return v;
    }

    //запись настроек в файл "settings"
    public void onDestroy() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getContext().openFileOutput("settings", Context.MODE_PRIVATE)));
            for (int i=0; i<4; i++)
                writer.write(settings[i] + "\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (smthChanged) {
            reload();
        }
        super.onDestroy();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //работа с UI
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1) {
            super.onActivityResult(requestCode, resultCode, data);
            uri = data.getData();
            src = uri.getPath();
            Toast.makeText(getActivity(), getString(R.string.file_with_path) + "\n" + src + "\n" + getString(R.string.was_selected), Toast.LENGTH_SHORT).show();
            unattachFilebut.setVisibility(View.VISIBLE);
            imV5.setVisibility(View.VISIBLE);
            tv9.setVisibility(View.VISIBLE);
            imageLoaded.setVisibility(View.VISIBLE);
            unattached = false;
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
