package com.ihome.android.ihome;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Logs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        LinearLayout ll = (LinearLayout) findViewById(R.id.logsLayout);

        for (int i = 0; i < 50; i++)
        {
            final TextView tv = new TextView(this);
            ShapeDrawable sd = new ShapeDrawable();

            // Specify the shape of ShapeDrawable
            sd.setShape(new RectShape());

            // Specify the border color of shape
            sd.getPaint().setColor(Color.BLACK);

            // Set the border width
            sd.getPaint().setStrokeWidth(5f);

            // Specify the style is a Stroke
            sd.getPaint().setStyle(Paint.Style.STROKE);

            tv.setBackground(sd);
            tv.setText("testView\nfasdfasdf\nfasdfasdf\nasdfasdfsd\nasdfasdf");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,0,20);
            tv.setLayoutParams(params);
            tv.setHeight(90);
            tv.setTextSize(20);
            tv.setPadding(10,0,0,0);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int height = tv.getHeight();
                    Log.d("testing", Integer.toString(height));
                    if (height == 90)
                    {
                        Helper.expand(tv, 250, 360);
                    }
                    else
                    {
                        Helper.collapse(tv, 250, 90);
                    }
                }
            });
            ll.addView(tv);
        }

    }
}
