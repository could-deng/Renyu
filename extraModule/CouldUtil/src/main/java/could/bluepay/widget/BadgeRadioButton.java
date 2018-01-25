package could.bluepay.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * RadioGroup包裹的RadioButton
 */

public class BadgeRadioButton extends FrameLayout{

    private RadioButton tabButton;
    private TextView badgeText;
    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean mChecked;
    private int checkedColor;
    private int uncheckedColor;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(BadgeRadioButton buttonView, boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
        this.onCheckedChangeListener =listener;
    }
    public BadgeRadioButton(@NonNull Context context) {
        this(context,null);
    }

    public BadgeRadioButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BadgeRadioButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.BadgeRadioButton);
//        TypedArray a= context.getTheme().obtainStyledAttributes(attrs,R.styleable.BadgeRadioButton,0,0);
        Drawable drawableTop = a.getDrawable(R.styleable.BadgeRadioButton_drawable_top);
        checkedColor = a.getColor(R.styleable.BadgeRadioButton_text_color_checked,Color.BLACK);
        uncheckedColor = a.getColor(R.styleable.BadgeRadioButton_text_color_unchecked,Color.BLACK);

        String bottomText = a.getString(R.styleable.BadgeRadioButton_drawable_text);

        a.recycle();

        if(drawableTop!=null){
//            drawableTop.setBounds(0,0,drawableTop.getIntrinsicWidth(),drawableTop.getIntrinsicHeight());
            drawableTop.setBounds(0,0,dip2px(30),dip2px(30));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tabButton.setCompoundDrawablesRelative(null,drawableTop,null,null);
            }else{
                tabButton.setCompoundDrawables(null,drawableTop,null,null);
            }
        }
        tabButton.setText(bottomText);
        tabButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        tabButton.setTextColor(uncheckedColor);
        tabButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setmChecked(isChecked);
            }
        });
//        tabButton.

    }

    public boolean ismChecked() {
        return tabButton.isChecked();
    }

    public void setmChecked(boolean isChecked){
        if(this.mChecked != isChecked){
            this.mChecked = isChecked;
            if(onCheckedChangeListener!=null){
                onCheckedChangeListener.onCheckedChanged(this,mChecked);
            }
            if(tabButton!=null){
                tabButton.setTextColor(isChecked?checkedColor:uncheckedColor);
                tabButton.setChecked(isChecked);
            }
        }
    }

    private void initViews(Context context) {
//        badgeText = new TextView(context);
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0,dip2px(32),0,dip2px(5));
//        badgeText.setLayoutParams(layoutParams);
//        badgeText.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
//        badgeText.setTypeface(Typeface.DEFAULT_BOLD);//字体
//        badgeText.setPadding(dip2px(1),dip2px(1),dip2px(1),dip2px(1));
//        badgeText.setGravity(Gravity.CENTER);//TextView里面
//        badgeText.setVisibility(View.VISIBLE);



//        LayoutParams layoutParams1 = new LayoutParams(dip2px(6),dip2px(6));//图片大小
//        layoutParams1.setMargins(0,0,0,0);
//        tabButton.setLayoutParams(layoutParams1);

        tabButton = new RadioButton(context);
        tabButton.setButtonDrawable(new StateListDrawable());//设置drawable为集合
        LayoutParams layoutParams2 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        layoutParams2.setMargins(dip2px(3), dip2px(3), dip2px(3), dip2px(3));
        tabButton.setGravity(Gravity.CENTER);
        addView(tabButton,layoutParams2);
    }




    public int dip2px(int dip){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip*density +0.5f);
    }

}
