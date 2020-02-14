package com.jusco;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Reports extends AppCompatActivity {

    private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,Color.MAGENTA, Color.CYAN };

    private static double[] VALUES = new double[] { 10, 11, 12, 13 };

    private static String[] NAME_LIST = new String[] { "A", "B", "C", "D" };

//    private CategorySeries mSeries = new CategorySeries("");
//
//    private DefaultRenderer mRenderer = new DefaultRenderer();
//
//    private GraphicalView mChartView;



    TextView lbl1;
    TextView lbl2;
    TextView lbl3;
    TextView lbl4;
    TextView lbl5;
    TextView lbl6;
    TextView lbl7;
    TextView lbl8;

    TextView lbl9;
    TextView lbl10;
    TextView lbl11;
    TextView lbl12;
    TextView lbl13;
    TextView lbl14;
    TextView lbl15;
    TextView lbl16;

    TextView lbl17;
    TextView lbl18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
//        mRenderer.setApplyBackgroundColor(true);
//        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
//        mRenderer.setChartTitleTextSize(20);
//        mRenderer.setLabelsTextSize(15);
//        mRenderer.setLegendTextSize(15);
//        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
//        mRenderer.setZoomButtonsVisible(true);
//        mRenderer.setStartAngle(90);
//        for (int i = 0; i < VALUES.length; i++) {
//            mSeries.add(NAME_LIST[i] + " " + VALUES[i], VALUES[i]);
//            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
//            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
//            mRenderer.addSeriesRenderer(renderer);
//        }
//
//        if (mChartView != null) {
//            mChartView.repaint();
//        }
        lbl1=(TextView)findViewById(R.id.lbl1);
        lbl2=(TextView)findViewById(R.id.lbl2);

        lbl3=(TextView)findViewById(R.id.lbl3);
        lbl4=(TextView)findViewById(R.id.lbl4);

        lbl5=(TextView)findViewById(R.id.lbl5);
        lbl6=(TextView)findViewById(R.id.lbl6);

        lbl7=(TextView)findViewById(R.id.lbl7);
        lbl8=(TextView)findViewById(R.id.lbl8);


        lbl9=(TextView)findViewById(R.id.lbl9);
        lbl10=(TextView)findViewById(R.id.lbl10);

        lbl11=(TextView)findViewById(R.id.lbl11);
        lbl12=(TextView)findViewById(R.id.lbl12);

        lbl13=(TextView)findViewById(R.id.lbl13);
        lbl14=(TextView)findViewById(R.id.lbl14);

        lbl15=(TextView)findViewById(R.id.lbl15);
        lbl16=(TextView)findViewById(R.id.lbl16);

        lbl17=(TextView)findViewById(R.id.lbl17);
        lbl18=(TextView)findViewById(R.id.lbl18);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String lbl_1 =(String) b.get("lbl1");
            lbl1.setText(lbl_1);
            String lbl_2 =(String) b.get("lbl2");
            lbl2.setText(lbl_2);
            String lbl_3 =(String) b.get("lbl3");
            lbl3.setText(lbl_3);
            String lbl_4 =(String) b.get("lbl4");
            lbl4.setText(lbl_4);

            String lbl_5 =(String) b.get("lbl5");
            lbl5.setText(lbl_5);
            String lbl_6 =(String) b.get("lbl6");
            lbl6.setText(lbl_6);
            String lbl_7 =(String) b.get("lbl7");
            lbl7.setText(lbl_7);
            String lbl_8 =(String) b.get("lbl8");
            lbl8.setText(lbl_8);
            String lbl_9 =(String) b.get("lbl9");
            lbl9.setText(lbl_9);
            String lbl_10 =(String) b.get("lbl10");
            lbl10.setText(lbl_10);
            String lbl_11 =(String) b.get("lbl11");
            lbl11.setText(lbl_11);
            String lbl_12 =(String) b.get("lbl12");
            lbl12.setText(lbl_12);

            String lbl_13 =(String) b.get("lbl13");
            lbl13.setText(lbl_13);
            String lbl_14 =(String) b.get("lbl14");
            lbl14.setText(lbl_14);
            String lbl_15 =(String) b.get("lbl15");
            lbl15.setText(lbl_15);
            String lbl_16 =(String) b.get("lbl16");
            lbl16.setText(lbl_16);

            String lbl_17 =(String) b.get("lbl17");
            lbl17.setText(lbl_17);
            String lbl_18 =(String) b.get("lbl18");
            lbl18.setText(lbl_18);
        }

//        lbl2.setText("DEF");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent repSel=new Intent(this,ReportsSelection.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(repSel);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mChartView == null) {
//            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
//            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
//            mRenderer.setClickEnabled(true);
//            mRenderer.setSelectableBuffer(10);
//
//            mChartView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
//
//                    if (seriesSelection == null) {
//                        Toast.makeText(AChartEnginePieChartActivity.this,"No chart element was clicked",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(AChartEnginePieChartActivity.this,"Chart element data point index "+ (seriesSelection.getPointIndex()+1) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
//                    if (seriesSelection == null) {
//                        Toast.makeText(AChartEnginePieChartActivity.this,"No chart element was long pressed", Toast.LENGTH_SHORT);
//                        return false;
//                    }
//                    else {
//                        Toast.makeText(AChartEnginePieChartActivity.this,"Chart element data point index "+ seriesSelection.getPointIndex()+ " was long pressed",Toast.LENGTH_SHORT);
//                        return true;
//                    }
//                }
//            });
//            layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//        }
//        else {
//            mChartView.repaint();
//        }
//    }
}
