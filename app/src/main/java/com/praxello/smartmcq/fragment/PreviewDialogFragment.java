package com.praxello.smartmcq.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.ViewQuestionActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PreviewDialogFragment extends DialogFragment {

    @BindView(R.id.ivImageView)
    ImageView imageView;
    View v;
    PhotoViewAttacher mAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.activity_pre_view, container, false);
        ButterKnife.bind(this,v);
        // Do all the stuff to initialize your custom view

        if(ViewQuestionActivity.questionID!=0){
            Glide.with(this).
                     load("http://103.127.146.5/~tailor/smartquiz/questionpics/"+ ViewQuestionActivity.questionID+".jpg").
                     diskCacheStrategy(DiskCacheStrategy.NONE).
                     signature(new ObjectKey(System.currentTimeMillis())).
                     skipMemoryCache(true).
                     into(imageView);
        }

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(imageView);
        mAttacher.update();

        return v;
    }
}