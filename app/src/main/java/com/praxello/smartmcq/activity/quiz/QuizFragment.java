package com.praxello.smartmcq.activity.quiz;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.praxello.smartmcq.AllKeys;
import com.praxello.smartmcq.CommonMethods;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.PreViewActivity;
import com.praxello.smartmcq.fragment.ShowResultFragment;
import com.praxello.smartmcq.model.allquestion.QuizBO;
import com.praxello.smartmcq.services.ApiRequestHelper;
import com.praxello.smartmcq.services.SmartQuiz;
import com.praxello.smartmcq.model.allquestion.QuestionBO;
import com.praxello.smartmcq.model.quiz.UserData;
import java.util.HashMap;
import java.util.Map;
import butterknife.ButterKnife;

public class QuizFragment extends Fragment implements View.OnClickListener {

    TextView tvQuestion, tvQuestionNo;
    ImageView ivOption1;
    TextView tvOption1;
    RelativeLayout rlOption1;
    ImageView ivOption2;
    TextView tvOption2;
    RelativeLayout rlOption2;
    ImageView ivOption3;
    TextView tvOption3;
    RelativeLayout rlOption3;
    ImageView ivOption4;
    TextView tvOption4;
    RelativeLayout rlOption4;
    TextView tvTimer;
    TextView tvNext;
    ImageView ivAnswer;
    TextView tvAnswer;
    CardView cardView1;
    CardView cardView2;
    CardView cardView3;
    CardView cardView4;
    ImageView ivBackgroundImage;
    TextView tvSelectAnAnswer;
    private int selectedAnswerIndex = -1;
    //public Question question;
    private QuizBO quizBO;
    public QuestionBO question;
    CountDownTimer cFirstTimer = null;
    boolean isQuestionAnswered = false;
    SmartQuiz smartQuiz;
    private final static String TAG = "QuizFragment";
    long secInMilliSeconds = 0;
    SensorManager mySensorManager;
    Sensor myProximitySensor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            question = arguments.getParcelable("question");
            quizBO = arguments.getParcelable("quiz_bo");
            //Log.e(TAG, "onCreate: QuizFragment"+question );
            //Log.e(TAG, "onCreate: quizBO "+quizBO.getQuestionTimeout() );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz1, container, false);
        ButterKnife.bind(getContext(), view);
        smartQuiz = (SmartQuiz) getActivity().getApplication();

        mySensorManager = (SensorManager) smartQuiz.getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(
                Sensor.TYPE_PROXIMITY);

        if (myProximitySensor == null) {
            Toast.makeText(smartQuiz, "No Proximity Sensor!", Toast.LENGTH_SHORT).show();
        } else {
            mySensorManager.registerListener(proximitySensorEventListener,
                    myProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        //basic intilaisation...
        initView(view);

        return view;
    }

    SensorEventListener proximitySensorEventListener
            = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0) {
                    Toast.makeText(smartQuiz, "Near", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(smartQuiz, "Away", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    private void initView(View view) {

        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvQuestionNo = view.findViewById(R.id.tv_question_no);
        tvTimer = view.findViewById(R.id.tvTimer);
        tvNext = view.findViewById(R.id.tvNext);
        tvAnswer = view.findViewById(R.id.tvAnswer);
        tvSelectAnAnswer = view.findViewById(R.id.tv_select_an_answer);

        ivOption1 = view.findViewById(R.id.ivOption1);
        ivOption2 = view.findViewById(R.id.ivOption2);
        ivOption3 = view.findViewById(R.id.ivOption3);
        ivOption4 = view.findViewById(R.id.ivOption4);
        ivAnswer = view.findViewById(R.id.ivAnswer);
        ivBackgroundImage = view.findViewById(R.id.ivbackgroundimg);
        ivBackgroundImage.setOnClickListener(this);

        tvOption1 = view.findViewById(R.id.tvOption1);
        tvOption2 = view.findViewById(R.id.tvOption2);
        tvOption3 = view.findViewById(R.id.tvOption3);
        tvOption4 = view.findViewById(R.id.tvOption4);

        rlOption1 = view.findViewById(R.id.rlOption1);
        rlOption2 = view.findViewById(R.id.rlOption2);
        rlOption3 = view.findViewById(R.id.rlOption3);
        rlOption4 = view.findViewById(R.id.rlOption4);

        cardView1 = view.findViewById(R.id.cardview1);
        cardView2 = view.findViewById(R.id.cardview2);
        cardView3 = view.findViewById(R.id.cardview3);
        cardView4 = view.findViewById(R.id.cardview4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tvQuestion.setText(question.getQuestion());
        tvOption1.setText(question.getOption1());
        tvOption2.setText(question.getOption2());
        tvOption3.setText(question.getOption3());
        tvOption4.setText(question.getOption4());
        tvAnswer.setText(question.getAnswerDetails());

        if (((QuizActivity) getContext()).quizBO.getQuestions().size() - 1 == ((QuizActivity) getContext()).currentQuesPos) {
            tvNext.setText("Submit");
        }

            /*if (quizBOArrayList.size() - 1 == ((QuizActivity) getContext()).currentQuesPos) {
                tvNext.setText("Submit");
            }
*/

        if (question.getQuestionType() == 1) {
            if (question.getMediaUrl() != null || !question.getMediaUrl().equals("")) {
                Glide.with(getContext()).load(question.getMediaUrl()).into(ivBackgroundImage);
            }
            tvSelectAnAnswer.setText("Que:- " + question.getQuestion());
            tvQuestionNo.setVisibility(View.GONE);
            tvQuestion.setVisibility(View.GONE);
        } else if (question.getQuestionType() == 2) {
            if (question.getMediaUrl() != null || !question.getMediaUrl().equals("")) {
                Glide.with(getContext()).load("https://img.youtube.com/vi/" + (question.getMediaUrl()) + "/0.jpg").into(ivBackgroundImage);
            }
            tvSelectAnAnswer.setText("Que:- " + question.getQuestion());
            tvQuestionNo.setVisibility(View.GONE);
            tvQuestion.setVisibility(View.GONE);
        }

        rlOption1.setOnClickListener(view1 -> {
            isQuestionAnswered = true;

            if (question.getAnswer() == 1) {
                //ivOption1.setVisibility(View.VISIBLE);
                cardView1.setVisibility(View.VISIBLE);
                cardView1.setBackgroundColor(getResources().getColor(R.color.green));
                cardView2.setVisibility(View.GONE);
                cardView3.setVisibility(View.GONE);
                cardView4.setVisibility(View.GONE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                selectedAnswerIndex = 1;
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });

        rlOption2.setOnClickListener(view1 -> {
            isQuestionAnswered = true;
            if (question.getAnswer() == 2) {
                //ivOption2.setVisibility(View.VISIBLE);
                cardView2.setVisibility(View.VISIBLE);
                cardView2.setBackgroundColor(getResources().getColor(R.color.green));
                cardView1.setVisibility(View.GONE);
                cardView3.setVisibility(View.GONE);
                cardView4.setVisibility(View.GONE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                selectedAnswerIndex = 2;
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });

        rlOption3.setOnClickListener(view1 -> {
            isQuestionAnswered = true;
            if (question.getAnswer() == 3) {
                // ivOption3.setVisibility(View.VISIBLE);
                cardView3.setVisibility(View.VISIBLE);
                cardView3.setBackgroundColor(getResources().getColor(R.color.green));
                cardView1.setVisibility(View.GONE);
                cardView2.setVisibility(View.GONE);
                cardView4.setVisibility(View.GONE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                selectedAnswerIndex = 3;
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });

        rlOption4.setOnClickListener(view1 -> {
            isQuestionAnswered = true;
            if (question.getAnswer() == 4) {
                //ivOption4.setVisibility(View.VISIBLE);
                cardView4.setVisibility(View.VISIBLE);
                cardView4.setBackgroundColor(getResources().getColor(R.color.green));
                cardView1.setVisibility(View.GONE);
                cardView2.setVisibility(View.GONE);
                cardView3.setVisibility(View.GONE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                selectedAnswerIndex = 4;
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });
        Log.e(TAG, "onViewCreated: quiz question timout" + quizBO.getQuestionTimeout());
        if (quizBO.getQuestionTimeout() != 0) {
            startFirstTimer(quizBO.getQuestionTimeout());
        }/*else{
            startFirstTimer(quizBO.getQuizTimeout());
        }*/


        tvNext.setOnClickListener(view1 -> {
            if (!isQuestionAnswered) {
                Toast.makeText(getContext(), "Please answer the question", Toast.LENGTH_SHORT).show();
                return;
            }
            if (((QuizActivity) getContext()).quizBO.getQuestions().size() - 1 == ((QuizActivity) getContext()).currentQuesPos) {
               /* Map<String, String> map = new HashMap<>();
                map.put("userid", CommonMethods.getPrefrence(getContext(), AllKeys.USER_ID));
                map.put("score", ""+((QuizActivity) getContext()).totalScore);

                Log.e(TAG, "onViewCreated: "+map );
                savequiz(map);*/
                if(getArguments().getString("type").equals("preview")){
                    Toast.makeText(smartQuiz, "These was only a demo quiz.", Toast.LENGTH_SHORT).show();
                }else{
                   savequiz("1", "" + ((QuizActivity) getContext()).totalScore, "" + question.quizId);
                }

            } else {
                ((QuizActivity) getContext()).currentQuesPos++;
                ((QuizActivity) getContext()).loadQuizFragment();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivbackgroundimg:
                if (question.getQuestionType() == 1 && !question.getMediaUrl().equals("")) {
                    Intent intent = new Intent(getContext(), PreViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("image_url", question.mediaUrl);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                } else if (question.getQuestionType() == 2 && !question.getMediaUrl().equals("")) {
                    startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + question.getMediaUrl())), 100);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            Log.e(TAG, "onActivityResult: start timer");
        }
    }

    private void savequiz(String userid, String score, String quizid) {
        if (CommonMethods.isNetworkAvailable(getContext())) {
            //CustomProgressDialog pd = new CustomProgressDialog(mContext);
            //pd.show();
            Map<String, String> params = new HashMap<>();
            params.put("userid", userid);
            params.put("score", score);
            params.put("quizid", quizid);
            smartQuiz.getApiRequestHelper().savequiz(params, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    //if (pd.isShowing()) pd.dismiss();/
                    UserData userData = (UserData) object;
                    Log.e("in", "success");

                    Log.e(TAG, "onSuccess: " + userData.getMessage());
                    Log.e(TAG, "onSuccess: " + userData.getResponsecode());
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {

                            Toast.makeText(smartQuiz, "Answers submitted", Toast.LENGTH_SHORT).show();
                           /* new MaterialDialog.Builder(getContext())
                                    .title("Your score")
                                    .content(((QuizActivity) getContext()).totalScore + "/" + ((QuizActivity) getContext()).quizBO.getQuestions().size())
                                    .positiveText("Ok")
                                    .cancelable(false)
                                    .onPositive((dialog, which) -> ((QuizActivity) getContext()).finish())
                                    .show();*/
                            loadQuizFragment();
                        } else {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Toast.makeText(getContext(), userData.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    // Log.e("in", "error " + apiResponse);

                    Toast.makeText(getContext(), apiResponse, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }


    public void loadQuizFragment() {
        Log.e(TAG, "loadQuizFragment: ");
        ShowResultFragment showResultFragment= new ShowResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("quiz_bo", quizBO);
        showResultFragment.setArguments(bundle);
        loadFragment(showResultFragment);
        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
        ((AppCompatActivity)getContext()).getSupportFragmentManager().popBackStack("", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    private void playAudio(boolean isCorrect) {
        MediaPlayer mp = MediaPlayer.create(getContext(), isCorrect ? R.raw.correct : R.raw.wrong);
        mp.start();
    }

   /* private void startFirstTimer(long sec) {
        long calculatedSec = sec * 1000 + 1000;
        cFirstTimer = new CountDownTimer(calculatedSec, 1000) {
            @Override
            public void onTick(long l) {
                //Log.e("l", "" + l);
                long seconds = l / 1000;
                //Log.e("seconds", "" + seconds);
                long minutes = seconds / 60;
                seconds %= 60;
                tvTimer.setText(String.format("%02d", seconds,"%02d",minutes));
                //tvTimer.setText(String.format("",minutes,":",seconds));
            }

            @Override
            public void onFinish() {
                isQuestionAnswered = true;
                showCorrectAnswer();
                playAudio(false);
                disableClicks();
            }
        };
        cFirstTimer.start();
    }*/

    private void startFirstTimer(long sec) {
        secInMilliSeconds = sec * 1000;
        cFirstTimer = new CountDownTimer(secInMilliSeconds, 1000) {
            @Override
            public void onTick(long l) {
                secInMilliSeconds = l;
                int minutes = (int) secInMilliSeconds / 60000;
                int seconds = (int) secInMilliSeconds % 60000 / 1000;
                String timeLeftText;
                timeLeftText = "" + minutes;
                timeLeftText += ":";
                if (seconds < 10) timeLeftText += "0";
                timeLeftText += seconds;
                tvTimer.setText(timeLeftText);
            }

            @Override
            public void onFinish() {
                isQuestionAnswered = true;
                showCorrectAnswer();
                playAudio(false);
                disableClicks();
            }
        };
        cFirstTimer.start();
    }

    private void showCorrectAnswer() {

        String temp = "";
        switch (selectedAnswerIndex) {
            case 1:
                temp = tvOption1.getText().toString();
                break;

            case 2:
                temp = tvOption2.getText().toString();
                break;

            case 3:
                temp = tvOption3.getText().toString();
                break;

            case 4:
                temp = tvOption4.getText().toString();
                break;
        }

        switch (question.getAnswer()) {

            case 1:
                if (selectedAnswerIndex == 1) {
                    cardView1.setVisibility(View.VISIBLE);
                    cardView1.setBackgroundColor(getResources().getColor(R.color.green));
                    cardView2.setVisibility(View.GONE);
                    cardView3.setVisibility(View.GONE);
                    cardView4.setVisibility(View.GONE);
                } else {
                    cardView1.setVisibility(View.VISIBLE);
                    cardView1.setBackgroundColor(getResources().getColor(R.color.green));
                    if (!temp.equals("")) {
                        cardView4.setVisibility(View.VISIBLE);
                        cardView4.setBackgroundColor(getResources().getColor(R.color.red));
                        tvOption4.setText(temp);
                    } else {
                        cardView4.setVisibility(View.GONE);
                    }
                    cardView3.setVisibility(View.GONE);
                    cardView2.setVisibility(View.GONE);
                }
                break;

            case 2:
                if (selectedAnswerIndex == 2) {
                    // ivOption2.setVisibility(View.VISIBLE);
                    cardView2.setVisibility(View.VISIBLE);
                    cardView2.setBackgroundColor(getResources().getColor(R.color.green));
                    cardView1.setVisibility(View.GONE);
                    cardView3.setVisibility(View.GONE);
                    cardView4.setVisibility(View.GONE);
                } else {
                    cardView2.setVisibility(View.VISIBLE);
                    cardView2.setBackgroundColor(getResources().getColor(R.color.green));
                    if (!temp.equals("")) {
                        cardView4.setVisibility(View.VISIBLE);
                        cardView4.setBackgroundColor(getResources().getColor(R.color.red));
                        tvOption4.setText(temp);
                    } else {
                        cardView4.setVisibility(View.GONE);
                    }
                    cardView3.setVisibility(View.GONE);
                    cardView1.setVisibility(View.GONE);
                }
                break;

            case 3:
                if (selectedAnswerIndex == 3) {
                    //ivOption3.setVisibility(View.VISIBLE);
                    cardView3.setVisibility(View.VISIBLE);
                    cardView3.setBackgroundColor(getResources().getColor(R.color.green));
                    cardView1.setVisibility(View.GONE);
                    cardView2.setVisibility(View.GONE);
                    cardView4.setVisibility(View.GONE);
                } else {
                    cardView3.setVisibility(View.VISIBLE);
                    cardView3.setBackgroundColor(getResources().getColor(R.color.green));
                    if (!temp.equals("")) {
                        cardView4.setVisibility(View.VISIBLE);
                        cardView4.setBackgroundColor(getResources().getColor(R.color.red));
                        tvOption4.setText(temp);
                    } else {
                        cardView4.setVisibility(View.GONE);
                    }
                    cardView2.setVisibility(View.GONE);
                    cardView1.setVisibility(View.GONE);
                }
                break;

            case 4:
                if (selectedAnswerIndex == 4) {
                    // ivOption4.setVisibility(View.VISIBLE);
                    cardView4.setVisibility(View.VISIBLE);
                    cardView4.setBackgroundColor(getResources().getColor(R.color.green));
                    cardView1.setVisibility(View.GONE);
                    cardView2.setVisibility(View.GONE);
                    cardView3.setVisibility(View.GONE);
                } else {
                    cardView4.setVisibility(View.VISIBLE);
                    cardView4.setBackgroundColor(getResources().getColor(R.color.green));
                    if (!temp.equals("")) {
                        cardView2.setVisibility(View.VISIBLE);
                        cardView2.setBackgroundColor(getResources().getColor(R.color.red));
                        tvOption2.setText(temp);
                    } else {
                        cardView2.setVisibility(View.GONE);
                    }
                    cardView3.setVisibility(View.GONE);
                    cardView1.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void disableClicks() {
        if (quizBO.getQuestionTimeout() != 0) {
            cFirstTimer.cancel();
        }
        rlOption1.setFocusable(false);
        rlOption2.setFocusable(false);
        rlOption3.setFocusable(false);
        rlOption4.setFocusable(false);
        rlOption1.setClickable(false);
        rlOption2.setClickable(false);
        rlOption3.setClickable(false);
        rlOption4.setClickable(false);
        ivAnswer.setVisibility(View.VISIBLE);
        tvAnswer.setVisibility(View.VISIBLE);
        tvTimer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        if (cFirstTimer != null) {
            cFirstTimer.cancel();
        }
        super.onDestroy();
    }


}