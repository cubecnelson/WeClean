package com.ligootech.weclean.Registration;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.animation.ValueAnimator;


import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.ligootech.weclean.R;
import com.ligootech.weclean.Registration.Fragment.DecisionFragment;
import com.ligootech.weclean.Registration.Fragment.LoginFragment;
import com.ligootech.weclean.Registration.Fragment.RegisterFragment;

import java.io.IOException;

public class RegistrationActivity extends Activity implements TextureView.SurfaceTextureListener, View.OnClickListener {

    private MediaPlayer mMediaPlayer;

    private TextureView mPreview;

    private ImageView mBrand;

    private String TAG;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TAG = RegistrationActivity.class.getName();

        mBrand = (ImageView) findViewById(R.id.brand);

        mPreview = (TextureView) findViewById(R.id.preview);
        mPreview.setSurfaceTextureListener(this);

        Fragment newFragment = new DecisionFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.front_layer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Surface surface = new Surface(surfaceTexture);

            try {
                AssetFileDescriptor afd = getAssets().openFd("clothing.mp4");
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer
                        .setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mMediaPlayer.setSurface(surface);
                mMediaPlayer.setLooping(true);

            // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
            // creating MediaPlayer
            mMediaPlayer.prepareAsync();

            // Play video when the media source is ready for playback.
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (IllegalArgumentException e) {
            //Log.d(TAG, e.getMessage());
        } catch (SecurityException e) {
            //Log.d(TAG, e.getMessage());
        } catch (IllegalStateException e) {
            //Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            //Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    private void animateUpBrand(){
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(mBrand, "y", 900, 300);
        fadeAnim.setDuration(500);
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {

            }
        });
        fadeAnim.start();
    }

    private void animateDownBrand(){
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(mBrand, "y", 300, 900);
        fadeAnim.setDuration(500);
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {

            }
        });
        fadeAnim.start();
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    @Override
    public void onClick(View v) {

        Fragment newFragment;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch(v.getId()){
            case R.id.login_back_button:
                animateDownBrand();
                newFragment = new DecisionFragment();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.front_layer, newFragment);
                transaction.commit();
                break;
            case R.id.register_back_button:
                animateDownBrand();
                newFragment = new DecisionFragment();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.front_layer, newFragment);
                transaction.commit();
                break;
            case R.id.register_button:
                animateUpBrand();
                newFragment = new RegisterFragment();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.front_layer, newFragment);
                transaction.commit();
                break;
            case R.id.login_button:
                animateUpBrand();
                newFragment = new LoginFragment();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.front_layer, newFragment);
                transaction.commit();
                break;
        }
    }
}
