package com.heim.menuview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener {

    private MenuView mMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mMenuView = (MenuView) findViewById(R.id.menuView);

        mMenuView.setOnMenuItemClickListener(this);

        mMenuView.setStartAngle(0);
        mMenuView.setEndAngle(360);
        mMenuView.setDuration(300);
    }

    @Override
    public void onMenuItemClick(View view, int position) {
        Toast.makeText(this, "点击的位置=====:" + position, Toast.LENGTH_SHORT).show();
    }
}
