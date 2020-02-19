package com.praxello.smartquiz.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.activity.MyScoreActivity;
import com.praxello.smartquiz.adapter.ProgressAdapter;
import com.praxello.smartquiz.model.scorecard.AttemptsBO;
import com.praxello.smartquiz.model.scorecard.AvailableBO;
import com.praxello.smartquiz.model.scorecard.ScoreCardResponse;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;
import java.util.ArrayList;
import java.util.HashMap;
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
