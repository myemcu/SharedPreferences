package com.myemcu.app_16sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences loginPreferences,accessPreferences; // 定义登陆设定与访问设定
    private SharedPreferences.Editor loginEditor,accessEditor;    // 定义其编辑器对象
    private String userName,userPsd;                              // 定义保存的用户名和密码
    private boolean isSavePsd,isAutoLogin;                        // 定义是否保存密码与自动登陆
    
    private TextView userInfo;
    private Button login;
    private CheckBox remPsd,autoLg;
    private EditText name,psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 登陆设定
        loginPreferences  = getSharedPreferences("login", Context.MODE_PRIVATE); // 获取登陆设置
        // 访问设定
        accessPreferences = getSharedPreferences("access",Context.MODE_WORLD_READABLE); // 其它APP可读

        int cnt = accessPreferences.getInt("count",1); // 获取访问次数默认为1

        Toast.makeText(MainActivity.this,"欢迎，这是第"+cnt+"次访问!",Toast.LENGTH_SHORT).show();
        
        loginEditor  = loginPreferences.edit();     // 获取写入登陆信息的Editor对象
        
        accessEditor = accessPreferences.edit();    // 获取写入访问信息的Editor对象
        accessEditor.putInt("count",++cnt);         // 访问次数,每次加1
        accessEditor.commit();                      // 提交写入的数据
        
        userName = loginPreferences.getString("name",null); // 获取保存的用户信息
        userPsd  = loginPreferences.getString("psd",null);  // 获取保存的密码信息
        
        isSavePsd = loginPreferences.getBoolean("isSavePsd",false);     // 保存密码乎?
        isAutoLogin=loginPreferences.getBoolean("isAutoLogin",false);   // 自动登陆乎?
        
        if (isAutoLogin) {
           this.setContentView(R.layout.activity_welcome);  // 显示欢迎界面
           userInfo = (TextView) findViewById(R.id.userInfo);
           userInfo.setText("欢迎您"+userName+"，登陆成功!");
        }
        else {
            loadActivity();
        }
    }

    private void loadActivity() {

        this.setContentView(R.layout.activity_main);

        findViews();

        if (isSavePsd) {                // 如果获取的保存密码为true
            psd.setText(userPsd);       // 设置密码框的值为保存的值
            name.setText(userName);     // 显示用户名为保存的用户名
            remPsd.setChecked(true);    // 设置"保存密码"的复选框为选中的状态
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEditor.putString("name",name.getText().toString());    // 写入用户名
                loginEditor.putString("psd",psd.getText().toString());      // 写入密码
                loginEditor.putBoolean("isSavePsd",remPsd.isChecked());
                loginEditor.putBoolean("isAutoLogin",autoLg.isChecked());
                loginEditor.commit();
                MainActivity.this.setContentView(R.layout.activity_welcome);
                userInfo = (TextView) findViewById(R.id.userInfo);
                userInfo.setText("欢迎您："+name.getText().toString()+"，登陆成功!");
            }
        });
    }

    private void findViews() {
        login  = (Button)   findViewById(R.id.login);
        remPsd = (CheckBox) findViewById(R.id.remPsd);
        autoLg = (CheckBox) findViewById(R.id.autoLogin);
        name   = (EditText) findViewById(R.id.name);
        psd    = (EditText) findViewById(R.id.psd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 选单界面
        getMenuInflater().inflate(R.menu.activity_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 选项处理
        switch (item.getItemId()) {
            case R.id.menu_settings: loginEditor.putBoolean("isAutoLogin",false);
                                     loginEditor.commit();
                                     loadActivity();
                                     break;

            case R.id.exit: this.finish();
                            break;

            default:break;
        }
        return true;
    }
}
