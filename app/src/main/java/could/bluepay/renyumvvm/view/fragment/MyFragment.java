package could.bluepay.renyumvvm.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.FragmentMyBinding;
import could.bluepay.renyumvvm.view.activity.UserSettingActivity;
import could.bluepay.renyumvvm.viewmodel.MyFragmentViewModel;

/**
 * MyFragment
 */

public class MyFragment extends BaseFragment<FragmentMyBinding,MyFragmentViewModel> {
    public static final String TAG = "MyFragment";

    private boolean isPrepared ;
    private View headerView;
    private MenuItem menuItem;

    public String setFragmentName(){
        return TAG;
    }
    @Override
    public MyFragmentViewModel setViewModel() {
        if(baseFragmentViewModel == null) {
            baseFragmentViewModel = new MyFragmentViewModel<>();
        }
        return baseFragmentViewModel;
    }

    @Override
    protected int getContent() {
        return R.layout.fragment_my;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        ((MainActivity)getActivity()).setMenuTabFirstClick(menuClickMethod);

        showContentView();
        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.ui_title_my));

        setViewModel().init(bindingView,MyFragment.this);
        bindingView.setViewModel(setViewModel());


        headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_header,null);
        bindingView.xrvMy.addHeaderView(headerView);

        setViewModel().setAdapterData();

        isPrepared = true;
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ((MainActivity)getActivity()).setMenuTabFirstClick(null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menuItem = menu.add(0,Menu.FIRST+5,5,"setting").setIcon(R.drawable.ic_setting);
        menuItem.setActionView(R.layout.menu_imageview);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        ((menuItem.getActionView().findViewById(R.id.iv_menu))).setClickable(true);
        ImageView settingIv = (ImageView)(menuItem.getActionView().findViewById(R.id.iv_menu));

        settingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSettingActivity.class);
                startActivity(intent);
            }
        });
    }

//    MainActivity.MenuClickMethod menuClickMethod = new MainActivity.MenuClickMethod(){
//        @Override
//        public void onMenuFirstClick() {
//            Intent intent = new Intent(getActivity(), UserSettingActivity.class);
//            startActivity(intent);
//        }
//    };



    @Override
    protected void loadData() {
        super.loadData();
        if(!isPrepared){
            return;
        }
    }





}
