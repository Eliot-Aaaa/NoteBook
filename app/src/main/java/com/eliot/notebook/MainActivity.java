package com.eliot.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;

    SharedPreferences sharedPreferences;
    TextView textViewUserName, textViewUserDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.layout_main_drawer);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_fragment_memo, R.id.navigation_fragment_diary, R.id.navigation_fragment_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navigation_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), Context.MODE_PRIVATE);

        //加载导航栏的头部布局，获取控件
        View view = navigationView.inflateHeaderView(R.layout.navigation_header);
        textViewUserName = view.findViewById(R.id.text_view_navigation_header_user_name);
        textViewUserDesc = view.findViewById(R.id.text_view_navigation_header_user_desc);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.navigation_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //使用SharedPreferences存储的配置设置应用
        String userDescStr = sharedPreferences.getString("user_desc", "Happy Every Day!");
        String userNameStr = sharedPreferences.getString("user_name", "Eliot");

        textViewUserName.setText(userNameStr);
        textViewUserDesc.setText(userDescStr);
    }
}
