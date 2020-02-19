package com.praxello.smartquiz.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.adapter.ScoreBoardAdapter;
import com.praxello.smartquiz.model.scorecard.ScoresBO;
import com.praxello.smartquiz.services.SmartQuiz;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ScoreBoardFragment extends Fragment {

    private View view;
    @BindView(R.id.rv_scoreboard)
    public RecyclerView rvScoreBoard;
    SmartQuiz smartQuiz;
    private static final String TAG="ScoreBoardFragment";
    ArrayList<ScoresBO> scoresBOArrayList;
    private Unbinder unbinder=null;

    public ScoreBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_score_board, container, false);
        smartQuiz = (SmartQuiz) getActivity().getApplication();
        unbinder= ButterKnife.bind(this,view);
        //basic intialisation..
        initViews();


        Bundle arguments = getArguments();
        if (arguments != null) {

            scoresBOArrayList = arguments.getParcelableArrayList("scores");
            if(scoresBOArrayList!=null){
                ScoreBoardAdapter scoreBoardAdapter=new ScoreBoardAdapter(getContext(),scoresBOArrayList);
                rvScoreBoard.setAdapter(scoreBoardAdapter);
            }
        }
        return view;
    }

    private void initViews(){
       // rvScoreBoard=view.findViewById(R.id.rv_scoreboard);
        rvScoreBoard.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
