package com.android.settings.displayenhancer;

/**
 * Created by shradha on 12/7/16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class ColorCalibration extends DialogPreference implements SeekBar.OnSeekBarChangeListener, OnClickListener {

    private static final String androidNameSpace = "http://schemas.android.com/apk/res/android";
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private TextView summaryRed, summaryGreen, summaryBlue;
    private Context mContext;
    private int mDefault, mMax, mRedValue, mGreenValue, mBlueValue = 0;
    private LinearLayout layout;

    // Constructor
    public ColorCalibration(Context context, AttributeSet attrs) {

        super(context, attrs);
        mContext = context;

        // Get default and max seek bar values
        mDefault = attrs.getAttributeIntValue(androidNameSpace, "defaultValue", 0);
        mMax = attrs.getAttributeIntValue(androidNameSpace, "max", 100);
    }

    // DialogPreference methods
    @Override
    protected View onCreateDialogView() {

        layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);

        if (shouldPersist())
            mRedValue = getPersistedInt(mDefault);

        LayoutInflater mInflater;
        View convertView;
        mInflater = LayoutInflater.from(mContext);
        convertView = mInflater.inflate(R.layout.color_calibration, null);

        initializeValues(convertView);

        layout.addView(convertView);
        return layout;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        redSeekBar.setMax(mMax);
        greenSeekBar.setMax(mMax);
        blueSeekBar.setMax(mMax);
        redSeekBar.setProgress(mRedValue);
        greenSeekBar.setProgress(mGreenValue);
        blueSeekBar.setProgress(mBlueValue);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        super.onSetInitialValue(restore, defaultValue);
        if (restore) {
            mRedValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
            mGreenValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
            mBlueValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
        } else {
            mRedValue = (Integer) defaultValue;
            mGreenValue = (Integer) defaultValue;
            mBlueValue = (Integer) defaultValue;
        }
    }

    // OnSeekBarChangeListener methods
    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        String t = String.valueOf(value);
        switch (seek.getId()){
            case R.id.redSeekBar :
                summaryRed.setText(t.concat("%"));
                break;
            case R.id.greenSeekBar :
                summaryGreen.setText(t.concat("%"));
                break;
            case  R.id.blueSeekBar :
                summaryBlue.setText(t.concat("%"));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seek) {
    }

    public void setMax(int max) {
        mMax = max;
    }

    public int getMax() {
        return mMax;
    }

    public void setProgress(int progress) {
        mRedValue = progress;
        if (redSeekBar != null) {
            redSeekBar.setProgress(progress);
        }
        if (greenSeekBar != null) {
            greenSeekBar.setProgress(progress);
        }
        if (blueSeekBar != null) {
            blueSeekBar.setProgress(progress);
        }
    }

    public int getProgress() {
        return mRedValue;
    }

    // Set the positive button listener and onClick action :
    @Override
    public void showDialog(Bundle state) {

        super.showDialog(state);

        Button positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (shouldPersist()) {
            mRedValue = redSeekBar.getProgress();
            persistInt(redSeekBar.getProgress());
            callChangeListener(Integer.valueOf(redSeekBar.getProgress()));
        }

        ((AlertDialog) getDialog()).dismiss();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setNeutralButton(mContext.getString(R.string.reset), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        switch (which) {
            case DialogInterface.BUTTON_NEUTRAL:
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_POSITIVE:
                break;
        }
    }

    private void initializeValues(View convertView) {
        summaryRed = (TextView) convertView.findViewById(R.id.summaryRed);
        summaryGreen = (TextView) convertView.findViewById(R.id.summaryGreen);
        summaryBlue  = (TextView) convertView.findViewById(R.id.summaryBlue);

        redSeekBar = (SeekBar) convertView.findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) convertView.findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) convertView.findViewById(R.id.blueSeekBar);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        redSeekBar.setMax(mMax);
        greenSeekBar.setMax(mMax);
        blueSeekBar.setMax(mMax);
        redSeekBar.setProgress(mRedValue);
        greenSeekBar.setProgress(mRedValue);
        blueSeekBar.setProgress(mRedValue);
    }
}