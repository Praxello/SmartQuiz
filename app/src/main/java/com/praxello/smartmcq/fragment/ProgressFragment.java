package com.praxello.smartmcq.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.MyScoreActivity;
import com.praxello.smartmcq.adapter.ProgressAdapter;
import com.praxello.smartmcq.model.scorecard.AttemptsBO;
import com.praxello.smartmcq.model.scorecard.AvailableBO;
import com.praxello.smartmcq.services.SmartQuiz;
import java.util.ArrayList;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProgressFragment extends Fragment {

    private View view;
    @BindView(R.id.rv_progress)
    RecyclerView rvProgress;
    private Unbinder unbinder=null;
    SmartQuiz smartQuiz;
    private static final String TAG="ProgressFragment";
    ArrayList<AvailableBO> availableBOArrayList;
    public static ArrayList<AttemptsBO> attemptsBOArrayList;
    public static Map<String, Integer> allAttemptsDataMap;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_progress, container, false);
        smartQuiz = (SmartQuiz) getActivity().getApplication();
        unbinder=ButterKnife.bind(this,view);

        //basic intialisation..
        initViews();

        if(MyScoreActivity.availableBOArrayList!=null){
            ProgressAdapter progressAdapter=new ProgressAdapter(getContext(), MyScoreActivity.availableBOArrayList);
            rvProgress.setAdapter(progressAdapter);
        }


        return view;
    }

    private void initViews(){
        rvProgress.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
