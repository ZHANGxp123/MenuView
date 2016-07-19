package com.heim.menuview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener {

    private MenuView mMenuView;
    private MenuView mMenuView1;
    private MenuView mMenuView2;
    private MenuView mMenuView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mMenuView = (MenuView) findViewById(R.id.menuView);
        mMenuView1 = (MenuView) findViewById(R.id.menuView1);
        mMenuView2 = (MenuView) findViewById(R.id.menuView3);
        mMenuView3 = (MenuView) findViewById(R.id.menuView4);

        mMenuView.setOnMenuItemClickListener(this);

        mMenuView.setStartAngle(0);
        mMenuView.setEndAngle(360);
        mMenuView.setDuration(300);

        mMenuView1.setStartAngle(0);
        mMenuView1.setEndAngle(360);
        mMenuView1.setDuration(300);

        mMenuView2.setStartAngle(0);
        mMenuView2.setEndAngle(360);
        mMenuView2.setDuration(300);

        mMenuView3.setStartAngle(0);
        mMenuView3.setEndAngle(360);
        mMenuView3.setDuration(300);

    }

    @Override
    public void onMenuItemClick(View view, int position) {
        Toast.makeText(this, "点击的位置=====:" + position, Toast.LENGTH_SHORT).show();
    }
}
