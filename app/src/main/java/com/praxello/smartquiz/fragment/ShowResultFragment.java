package com.praxello.smartquiz.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.activity.DashBoardActivity;
import com.praxello.smartquiz.activity.QuizTopicsActvity;
import com.praxello.smartquiz.activity.quiz.QuizActivity;
import com.praxello.smartquiz.widget.ColorArcProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowResultFragment extends Fragment {

    View view;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.progressBar)
    ColorArcProgressBar circleProgressView;
    @BindView(R.id.btnhome)
    AppCompatButton btnHome;
    @BindView(R.id.btnnextsession)
    AppCompatButton btnNextSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_show_result, container, false);
        ButterKnife.bind(this,view);

        tvResult.setText(((QuizActivity) getContext()).totalScore + "/" + ((QuizActivity) getContext()).quizBO.getQuestions().size());

        float finalPerc = (((QuizActivity) getContext()).totalScore * 100f) /((QuizActivity) getContext()).quizBO.getQuestions().size() ;
        circleProgressView.setCurrentValues(finalPerc);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DashBoardActivity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
            }
        });

        btnNextSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), QuizTopicsActvity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
            }
        });
        return view;
    }

}
