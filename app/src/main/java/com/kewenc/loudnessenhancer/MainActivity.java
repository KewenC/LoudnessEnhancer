package com.kewenc.loudnessenhancer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private LoudnessEnhancer loudnessEnhancer;
    private ToggleButton btn;
    private TextView tv_value;
    private SeekBar sb;
    private int targetGain = 0;
    private Equalizer equalizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            equalizer = new Equalizer(Integer.MAX_VALUE, 0);
            equalizer.setEnabled(true);
        }

        try {
            loudnessEnhancer = new LoudnessEnhancer(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn = findViewById(R.id.btn);
        btn.setChecked(true);
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setLoudEnable(b);
            }
        });

        tv_value = findViewById(R.id.tv_value);
        sb = findViewById(R.id.sb);
        sb.setProgress(0);
        sb.setMax(10000);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                targetGain = i;
                tv_value.setText("增益值:"+ targetGain);
                setTargetGain(targetGain);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setLoudEnable(btn.isChecked());
    }

    private void setTargetGain(int value) {
        if (loudnessEnhancer != null) {
            loudnessEnhancer.setTargetGain(value);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean setLoudEnable(boolean isEnable) {
        if (loudnessEnhancer != null) {
            loudnessEnhancer.setEnabled(isEnable);
            loudnessEnhancer.release();
            loudnessEnhancer = null;
        }
        if (isEnable) {
            loudnessEnhancer = new LoudnessEnhancer(0);
            loudnessEnhancer.setEnabled(true);
            return setLoudBase(true);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean setLoudBase(boolean isEnable) {
        if (loudnessEnhancer != null) {
            loudnessEnhancer.setEnabled(isEnable);
            loudnessEnhancer.release();
            loudnessEnhancer = null;
        }
        if (isEnable) {
            loudnessEnhancer = new LoudnessEnhancer(0);
            loudnessEnhancer.setEnabled(true);

            loudnessEnhancer.setEnabled(false);
            loudnessEnhancer.release();
            loudnessEnhancer = null;

            loudnessEnhancer = new LoudnessEnhancer(0);
            loudnessEnhancer.setEnabled(true);

            loudnessEnhancer.setTargetGain(targetGain);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loudnessEnhancer !=null) {
            loudnessEnhancer.setEnabled(false);
            loudnessEnhancer.release();
            loudnessEnhancer = null;
        }
        if (equalizer !=null) {
            equalizer.setEnabled(false);
            equalizer.release();
            equalizer = null;
        }
    }
}

