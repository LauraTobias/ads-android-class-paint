package com.example.paint;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class MainActivity extends AppCompatActivity {

    SimplePaint simplePaint;
    ImageView imageViewColorPicker;
    ImageView imageViewBack;
    ImageView imageViewClear;
    ImageView imageViewSquare;
    ImageView imageViewCircle;
    ImageView imageViewLine;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simplePaint = findViewById(R.id.simplePaint);
        imageViewLine = findViewById(R.id.line);
        imageViewSquare = findViewById(R.id.square);
        imageViewCircle = findViewById(R.id.circle);
        imageViewClear = findViewById(R.id.clear);
        imageViewBack = findViewById(R.id.back);
        imageViewColorPicker = findViewById(R.id.colorPicker);

        View.OnClickListener onClickListener = view -> {
            switch (view.getId())
            {
                case R.id.line: simplePaint.setStyleType(StyleType.line); break;
                case R.id.square: simplePaint.setStyleType(StyleType.square); break;
                case R.id.circle: simplePaint.setStyleType(StyleType.circle); break;
                case R.id.clear: simplePaint.removeDraw(); break;
                case R.id.back: simplePaint.backDraw(); break;
                case R.id.colorPicker: colorPickerSelectColor(); break;
            }
        };

        imageViewColorPicker.setOnClickListener(onClickListener);
        imageViewBack.setOnClickListener(onClickListener);
        imageViewClear.setOnClickListener(onClickListener);
        imageViewSquare.setOnClickListener(onClickListener);
        imageViewCircle.setOnClickListener(onClickListener);
        imageViewLine.setOnClickListener(onClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void colorPickerSelectColor() {
        new ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm),
                        (ColorEnvelopeListener) (envelope, fromUser) -> setColor(envelope))
                .setNegativeButton(getString(R.string.cancel),
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setColor(ColorEnvelope envelope) {
        simplePaint.setColor(Color.valueOf(envelope.getColor()));
        imageViewColorPicker.setColorFilter(Color.valueOf(envelope.getColor()).toArgb());
        imageViewColorPicker.setBackgroundColor(Color.valueOf(envelope.getColor()).toArgb());
    }
}