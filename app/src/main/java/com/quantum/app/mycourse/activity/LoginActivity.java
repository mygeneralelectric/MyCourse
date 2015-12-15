package com.quantum.app.mycourse.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.app.mycourse.R;
import com.quantum.app.mycourse.ui.LoginButton;
import com.quantum.app.mycourse.util.HttpUtil;
import com.quantum.app.mycourse.util.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText getCode;
    private EditText getPasswd;
    private Button loginButton;
    private LoginButton myLoginButton;
    private CheckBox rememberPassword;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public String studentCode = "获取学号失败";
    public String studentPassword = "获取密码失败";

    public boolean loginResult = false;
    private boolean isRemember;

    private HttpUtil httpUtil;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(2000);
        getWindow().setExitTransition(explode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupWindowAnimations();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.login_activity_title));
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        getCode = (EditText) findViewById(R.id.student_code);
        getPasswd = (EditText) findViewById(R.id.student_password);
        rememberPassword = (CheckBox) findViewById(R.id.remember_password);
        rememberPassword.setChecked(true);
        loginButton = (Button) findViewById(R.id.login_button);
//        myLoginButton = (LoginButton) findViewById(R.id.my_login_button);
//        myLoginButton.setOnSendClickListener(new LoginButton.OnSendClickListener() {
//            @Override
//            public void onSendClickListener(View v) {
//                studentCode = getCode.getText().toString();
//                studentPassword = getPasswd.getText().toString();
//                if (TextUtils.isEmpty(studentCode) || TextUtils.isEmpty(studentPassword)) {
//                    myLoginButton.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake_error));
//                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//                    vibrator.vibrate(500);
//                }
//
//                myLoginButton.setCurrentState(LoginButton.STATE_DONE);
//            }
//        });
        isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String sCode = pref.getString("student_code", "");
            String pCode = pref.getString("student_password", "");
            getCode.setText(sCode);
            getPasswd.setText(pCode);
            rememberPassword.setChecked(true);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPasswd.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_in_done));
                studentCode = getCode.getText().toString();
                studentPassword = getPasswd.getText().toString();
                if (TextUtils.isEmpty(studentCode) || TextUtils.isEmpty(studentPassword)) {
                    if (TextUtils.isEmpty(studentCode)) {
                        getCode.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake_error));
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }
                    if (TextUtils.isEmpty(studentPassword)) {
                        getPasswd.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake_error));
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("正在加载中. . . . . .");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpUtil = new HttpUtil();
                            if (httpUtil.login(studentCode, studentPassword)) {
                                String dayCourse = httpUtil.dayCourse;
                                String weekCourse = httpUtil.weekCourse;
                                String mScore = httpUtil.mScore;
                                double mGradePoint = httpUtil.mGradePoint;
                                editor = pref.edit();
                                if (rememberPassword.isChecked()) {
                                    editor.putBoolean("has_login", true);
                                    editor.putBoolean("remember_password", true);
                                    editor.putString("student_code", studentCode);
                                    editor.putString("student_password", studentPassword);
                                    editor.putString("day_course", dayCourse);
                                    editor.putString("week_course", weekCourse);
                                    editor.putString("m_score", mScore);
                                    editor.putString("m_grade_point", String.valueOf(mGradePoint));
                                } else {
                                    editor.clear();
                                }
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        loginButton.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake_error));
                                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                        vibrator.vibrate(500);
                                        Toast.makeText(LoginActivity.this, "学号或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
