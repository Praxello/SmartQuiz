package com.praxello.smartmcq.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.DashBoardActivity;
import com.praxello.smartmcq.activity.QuizTopicsActvity;
import com.praxello.smartmcq.activity.quiz.QuizActivity;
import com.praxello.smartmcq.model.allquestion.QuizBO;
import com.praxello.smartmcq.widget.ColorArcProgressBar;
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
    @BindView(R.id.ivImageView)
    ImageView imageView;
    QuizBO quizBO;
    private static String TAG="ShowResultFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_show_result, container, false);
        ButterKnife.bind(this,view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            quizBO = arguments.getParcelable("quiz_bo");
            //Log.e(TAG, "onCreate: QuizFragment"+question );
            Log.e(TAG, "onCreate: quizBO "+quizBO.getQuestionTimeout());
        }

        tvResult.setText(((QuizActivity) getContext()).totalScore + "/" + ((QuizActivity) getContext()).quizBO.getQuestions().size());

        try{
            float finalPerc = (((QuizActivity) getContext()).totalScore * 100f) /((QuizActivity) getContext()).quizBO.getQuestions().size() ;
            circleProgressView.setCurrentValues(finalPerc);

            if(finalPerc>=quizBO.getPassingScore()){
                Glide.with(this).
                        load(R.drawable.passed).
                        diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(imageView);
            }else{
                Glide.with(this).
                        load(R.drawable.fail).
                        diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(imageView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
            }
        });

        btnNextSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), QuizTopicsActvity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
            }
        });
        return view;
    }

}
