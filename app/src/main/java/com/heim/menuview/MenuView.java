package com.heim.menuview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by zxp on 2016/7/18 0018.
 */
public class MenuView extends ViewGroup implements View.OnClickListener {

    private static final int POS_LEFT_TOP     = 0;
    private static final int POS_LEFT_BOTTOM  = 1;
    private static final int POS_RIGHT_TOP    = 2;
    private static final int POS_RIGHT_BOTTOM = 3;


    private static final int HORIZONTAL=0;
    private static final int VERTICAL=1;

    //记录子控件是否是打开状态,默认为不打开
    private boolean isOpen;

    //如果不设置位置,默认为右下
    private Position mPosition = Position.RIGHT_BOTTOM;
    private int       mRadius;
    private ImageView mMenu;
    //默认打开方向水平
    private int mOritation=HORIZONTAL;

    private OnMenuItemClickListener mListener;
    //开始角度
    private int startAngle;
    //结束角度
    private int endAngle;
    //动画时间
    private int duration;

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public void setEndAngle(int endAngle) {
        this.endAngle = endAngle;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void onClick(View v) {
        //菜单按钮的动画
       rotateMenu();

        //item的动画
        menuItemAnimation();

    }

    private void menuItemAnimation() {
        for (int i = 0; i < getChildCount() - 1; i++) {
            final View childView = getChildAt(i+1);

            //子控件开始动画之前设置为显示
            childView.setVisibility(VISIBLE);

            AnimationSet set = new AnimationSet(true);
            TranslateAnimation translateAnimation = null;
            int distance = mRadius * (i+1);
            int flag=1;
            switch (mOritation) {
                case HORIZONTAL:
                    if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
                        flag = -1;
                    }
                    if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                        flag = 1;
                    }

                    if (isOpen) {
                        //打开
                        translateAnimation = new TranslateAnimation(0, flag * distance, 0, 0);
                        childView.setClickable(true);
                        childView.setFocusable(true);
                    }else{
                        //没打开
                        translateAnimation = new TranslateAnimation(flag * distance,0, 0, 0);
                        childView.setClickable(false);
                        childView.setFocusable(false);
                    }
                    break;
                case VERTICAL:
                    if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                        flag = -1;
                    }
                    if (mPosition == Position.RIGHT_BOTTOM || mPosition == Position.LEFT_BOTTOM) {
                        flag = 1;
                    }

                    if (isOpen) {
                        translateAnimation = new TranslateAnimation(0, 0, 0, flag * distance);
                        childView.setClickable(true);
                        childView.setFocusable(true);
                    }else{
                        translateAnimation = new TranslateAnimation(0, 0,flag * distance,0);
                        childView.setClickable(false);
                        childView.setFocusable(false);
                    }
                    break;
            }

            translateAnimation.setDuration(duration);
            translateAnimation.setFillAfter(true);
            //动画监听
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //如果是关闭状态,就让子控件隐藏
                    if (!isOpen) {
                        childView.setVisibility(GONE);

                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            RotateAnimation rotateAnimation = new RotateAnimation(startAngle, endAngle, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(duration);
            rotateAnimation.setFillAfter(true);

            set.addAnimation(rotateAnimation);
            set.addAnimation(translateAnimation);
            set.setStartOffset(20*i);
            childView.startAnimation(set);

            final int pos=i+1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //接口回调
                    if (mListener != null) {
                        mListener.onMenuItemClick(v,pos);
                    }
                    clickItemAnimation(pos-1);
                    toggle();
                }
            });

        }

        //切换状态
        toggle();

    }

    //点击菜单条目的动画
    private void clickItemAnimation(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (i == pos) {
                //点计的按钮,放大动画
                childView.startAnimation(AnimationUtil.bigScaleAnimaiton(300));
            }else{
                //没点到的,缩小动画
                childView.startAnimation(AnimationUtil.smallScaleAnimaiton(300));
            }
            childView.setVisibility(GONE);
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mListener = listener;
    }

    //切换子控件的显示状态
    private void toggle() {
        isOpen = !isOpen;
    }

    //菜单按钮的旋转动画
    private void rotateMenu() {
        RotateAnimation menuAnimation = new RotateAnimation(startAngle, endAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        menuAnimation.setDuration(duration);
        menuAnimation.setFillAfter(true);
        mMenu.startAnimation(menuAnimation);
    }

    private enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM,
    }

    public MenuView(Context context) {
        this(context, null, 0);
    }

    public MenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //如果不设置半径,默认半径为100dp
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuView);

        int pos = typedArray.getInt(R.styleable.MenuView_position, 3);
        switch (pos) {
            case POS_LEFT_TOP:  //0 左上
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:  //1 左下
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:  //2 右上
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:  //3 右下
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        //打开的方向
        int direction = typedArray.getInt(R.styleable.MenuView_oritation, 0);

        switch (direction) {
            case 0:
                mOritation = HORIZONTAL;
                break;
            case 1:
                mOritation = VERTICAL;
                break;
        }

        mRadius = (int) typedArray.getDimension(R.styleable.MenuView_radius, TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量menuview里所有的子控件的大小
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed) {
            //菜单按钮
            mMenu = (ImageView) getChildAt(0);

            //给菜单按钮设置点击事件
            mMenu.setOnClickListener(this);
            //布局菜单按钮
            layoutMenu();

            //布局item
            layoutMenuItem();
        }
    }

    private void layoutMenuItem() {
        int l = 0;
        int t = 0;

        switch (mOritation) {
            //打开方式为水平的布局
            case HORIZONTAL:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i + 1);
                    //子控件默认为不显示
                    childView.setVisibility(GONE);
                    int childWidth = childView.getMeasuredWidth();
                    int childHeight = childView.getMeasuredHeight();

                    if (mPosition == Position.LEFT_TOP) {
                        l = mRadius * (i + 1);
                        t = 0;
                    }
                    if (mPosition == Position.LEFT_BOTTOM) {
                        l = mRadius * (i + 1);
                        t = getMeasuredHeight() - childHeight;
                    }

                    if (mPosition == Position.RIGHT_TOP) {
                        l = getMeasuredWidth() - mRadius * (i + 1)-childWidth;
                        t = 0;
                    }
                    if (mPosition == Position.RIGHT_BOTTOM) {
                        l = getMeasuredWidth() - mRadius * (i + 1) - childWidth;
                        t = getMeasuredHeight() - childHeight;
                    }

                    int r = l + childWidth;
                    int b = t + childHeight;

                    childView.layout(l, t, r, b);
                }
                break;
            case VERTICAL:

                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i + 1);
                    childView.setVisibility(GONE);
                    int childWidth = childView.getMeasuredWidth();
                    int childHeight = childView.getMeasuredHeight();

                    if (mPosition == Position.LEFT_TOP) {
                        l = 0;
                        t = mRadius * (i + 1);
                    }
                    if (mPosition == Position.LEFT_BOTTOM) {
                        l=0;
                        t = getMeasuredHeight() - childHeight - mRadius * (i + 1);
                    }

                    if (mPosition == Position.RIGHT_TOP) {
                        l = getMeasuredWidth()-childWidth;
                        t = mRadius * (i + 1);
                    }
                    if (mPosition == Position.RIGHT_BOTTOM) {
                        l = getMeasuredWidth()-childWidth;
                        t = getMeasuredHeight() - childHeight-mRadius * (i + 1);
                    }

                    int r = l + childWidth;
                    int b = t + childHeight;

                    childView.layout(l, t, r, b);
                }
                break;
        }
    }

    private void layoutMenu() {
        int l=0;
        int t=0;
        //菜单按钮的大小
        int menuWidth = mMenu.getMeasuredWidth();
        int menuHeight = mMenu.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP: //位置在左上
                l=0;
                t=0;
                break;
            case LEFT_BOTTOM://位置在左下
                l=0;
                t = getMeasuredHeight() - menuHeight;
                break;
            case RIGHT_TOP://位置在右上
                l = getMeasuredWidth() - menuWidth;
                t=0;
                break;
            case RIGHT_BOTTOM://位置在右下
                l = getMeasuredWidth() - menuWidth;
                t = getMeasuredHeight() - menuHeight;
                break;
        }

        int r = l + menuWidth;
        int b = t + menuHeight;

        mMenu.layout(l, t, r, b);
    }
}
