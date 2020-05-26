package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PresentationActivity extends AppCompatActivity {

    private ViewPager mSliderViewPager;
    private LinearLayout mDotLayout;
    private TextView[]mDots;
    private  AdapterSlide sliderAdapter;
    private Button mNextBtn, mBackBtn;
    private int mCurrentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        mNextBtn= findViewById(R.id.buttonNext);

        mSliderViewPager= (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout=findViewById(R.id.dotsLayout);
        sliderAdapter = new AdapterSlide(this);
        mSliderViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSliderViewPager.addOnPageChangeListener(viewListener);

    }



    public void addDotsIndicator(int position){
        mDots= new TextView[4];
        mDotLayout.removeAllViews();
        for(int i= 0; i< mDots.length; i++){
            mDots[i]= new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.monsoon));
            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            mCurrentPage=position;
            if(position==0)
            {
                mNextBtn.setVisibility(View.GONE);
            }
         else if(position==mDots.length -1){
                mNextBtn.setVisibility(View.VISIBLE);
                mNextBtn.setEnabled(true);
                mNextBtn.setText("Finish");
                mNextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
         else
            {
                mNextBtn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
