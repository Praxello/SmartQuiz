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

        //load available data...
        loadData();

        return view;
    }

    private void initViews(){
        rvProgress.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadData(){
        smartQuiz.getApiRequestHelper().getscorecard("1",new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                ScoreCardResponse scoreCardResponse=(ScoreCardResponse) object;

                Log.e(TAG, "onSuccess: "+scoreCardResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+scoreCardResponse.getMessage());
                Log.e(TAG, "onSuccess: "+scoreCardResponse);

                if(scoreCardResponse.getResponsecode()==200){
                    Toast.makeText(getContext(), scoreCardResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if(scoreCardResponse.getAvailable()!=null){

                        allAttemptsDataMap=new HashMap<>();
                        ProgressFragment.attemptsBOArrayList=scoreCardResponse.getAttempts();
                        if(scoreCardResponse.getAttempts()!=null){
                            for(AttemptsBO temp:ProgressFragment.attemptsBOArrayList){
                                allAttemptsDataMap.put(temp.categoryTitle,temp.total);
                            }
                        }
                        //allAttemptsDataMap=  scoreCardResponse.getAttempts();

                        availableBOArrayList=scoreCardResponse.getAvailable();
                        ProgressAdapter progressAdapter=new ProgressAdapter(getContext(),scoreCardResponse.getAvailable());
                        rvProgress.setAdapter(progressAdapter);
                    }
                }else{
                    Toast.makeText(getContext(), scoreCardResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(getContext(), apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
