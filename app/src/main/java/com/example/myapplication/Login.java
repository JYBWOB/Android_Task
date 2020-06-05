package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.DataBase.SQLManager;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    SQLManager sqlManager = new SQLManager(Login.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String username = sqlManager.getAutoLogin();
        if(!username.equals("N")) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(loginListener);

        Button register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
    }

    private OnClickListener loginListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String userName = ((TextView)findViewById(R.id.username)).getText().toString();
            String password = ((TextView)findViewById(R.id.password)).getText().toString();
            CheckBox remPass = (CheckBox)findViewById(R.id.remPassword);
            CheckBox autoLogin = (CheckBox)findViewById(R.id.autoLogin);

            StringBuffer message = new StringBuffer();
            // 账号输入状态
            if(userName.equals("") || password.equals("")) {
                message.append("请输入账号和密码\n");

                // 对话框显示
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(Login.this);
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }
                );
                normalDialog.setIcon(R.drawable.nku);
                normalDialog.setTitle("提示");
                normalDialog.setMessage(message.toString());
                normalDialog.show();
                return;
            }
            if(sqlManager.isExist(userName, password)) {
                if(autoLogin.isChecked()) {
                    sqlManager.setAutoLogin(userName);
                }
                else {
                    sqlManager.delAutoLogin();
                }
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("username", userName);
                startActivity(intent);
            }
            else {
                // 对话框显示
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(Login.this);
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }
                );
                normalDialog.setIcon(R.drawable.nku);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("账号或密码输入错误");
                normalDialog.show();
            }
        }
    };


    private void showRegisterDialog() {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(Login.this);
        final View dialogView = LayoutInflater.from(Login.this)
                .inflate(R.layout.register,null);
        customizeDialog.setTitle("注册对话框");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText username_text =
                                (EditText) dialogView.findViewById(R.id.regUserName);
                        EditText password_text =
                                (EditText) dialogView.findViewById(R.id.regPassWord);
                        final String username = username_text.getText().toString();
                        final String password = password_text.getText().toString();

                        sqlManager.addUser(username, password);

                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(Login.this);
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }
                        );
                        normalDialog.setIcon(R.drawable.nku);
                        normalDialog.setTitle("提示");
                        normalDialog.setMessage("注册成功！");
                        normalDialog.show();
                    }
                });
        customizeDialog.show();
    }
}
