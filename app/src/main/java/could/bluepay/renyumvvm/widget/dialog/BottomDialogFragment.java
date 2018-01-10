package could.bluepay.renyumvvm.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import could.bluepay.renyumvvm.R;

/**
 * Created by bluepay on 2017/12/19.
 */

public class BottomDialogFragment extends DialogFragment {

    private static final String KEY_LAYOUT_RES = "bottom_layout_res";
    private static final String KEY_HEIGHT = "bottom_height";
    private static final String KEY_DIM = "bottom_dim";
    private static final String KEY_CANCEL_OUTSIDE = "bottom_cancel_outside";



    public static final String TAG = "BottomDialogFragment";

    public static final float DEFAULT_DIM = 0.2f;


    private int layoutRes = -1;
    private int mHeight = -1;
    private float mDimAmount = 0;
    private boolean cancel_outside = true;
    private FragmentManager manager;
    private ViewListener viewListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);//theme+style

        if(savedInstanceState!=null){
            layoutRes = savedInstanceState.getInt(KEY_LAYOUT_RES);
            mHeight = savedInstanceState.getInt(KEY_HEIGHT);
            mDimAmount = savedInstanceState.getFloat(KEY_DIM);
            cancel_outside = savedInstanceState.getBoolean(KEY_CANCEL_OUTSIDE);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public int getLayoutRes(){
        return layoutRes;
    }
    public void bindView(View v){
        if(viewListener!=null){
            viewListener.bindView(v);
        }

    }
    public float getDimAmount(){
        if(mDimAmount == 0f){
            return DEFAULT_DIM;
        }
        return mDimAmount;
    }

    public int getHeight(){
        return mHeight;
    }
    //region====供activity调用,工厂模式==========

    public BottomDialogFragment setFragmentManager(FragmentManager manager){
        this.manager = manager;
        return this;
    }
    public BottomDialogFragment setLayoutRes(int layoutRes){
        this.layoutRes = layoutRes;
        return this;
    }
    public BottomDialogFragment setHeight(int height){
        this.mHeight = height;
        return this;
    }
    public BottomDialogFragment setDimAcount(float dimAcount){
        mDimAmount = dimAcount;
        return this;
    }
    public BottomDialogFragment setViewListener(ViewListener viewListener){
        this.viewListener = viewListener;
        return this;
    }


    public void show(){
        if(manager != null) {
            show(manager, TAG);
        }
    }

    public static BottomDialogFragment create(FragmentManager fragmentManager){
        BottomDialogFragment dialog = new BottomDialogFragment();
        dialog.setFragmentManager(fragmentManager);
        return dialog;
    }

    //endregion====供activity调用,工厂模式==========


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        View v = inflater.inflate(getLayoutRes(),null);
        bindView(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LAYOUT_RES,layoutRes);
        outState.putInt(KEY_HEIGHT,mHeight);
        outState.putFloat(KEY_DIM,mDimAmount);
        outState.putBoolean(KEY_CANCEL_OUTSIDE,cancel_outside);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = getDimAmount();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if(getHeight()>0){
            params.height = getHeight();
        }else{
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    public interface ViewListener{
        void bindView(View v);
    }

}
