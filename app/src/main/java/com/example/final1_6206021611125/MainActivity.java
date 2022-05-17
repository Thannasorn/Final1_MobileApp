package com.example.final1_6206021611125;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout layout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout1 = (ConstraintLayout) findViewById(R.id.layout1);
        layout1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        GraphicsView graphicsView = new GraphicsView(this);
        setContentView( graphicsView);
        graphicsView.requestFocus();
    }
}