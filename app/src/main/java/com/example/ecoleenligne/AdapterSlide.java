package com.example.ecoleenligne;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class AdapterSlide extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public AdapterSlide(Context context){
        this.context=context;
    }
    //Arrays
    public int[] slide_image = {R.drawable.logo, R.drawable.meeting, R.drawable.fiche, R.drawable.quiz};

    public String[] slide_heading={"Bienvenue sur Ecole des Anges","Comprends tous tes cours en"+" vidéo!","Gagne du temps avec nos fiches!","Vérifie tes connaissance avec "+"nos quiz!"};
    public String[] slide_desc={"la nouvelle solution pour réviser tes cours, tous les jours","Des cours vidéo avec des animations pour tout comprendre", "Des fiches de cours et de révision à ta "+" dipo dasn toutes les matières", "Des millers de quiz et exo pour devenir"+" incollable"};

    @Override
    public int getCount() {
        return slide_desc.length;
    }

    @Override
    public boolean isViewFromObject( View view,  Object object) {
        return view == (RelativeLayout)object;
    }


    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view= layoutInflater.inflate(R.layout.slide_layout, container, false);
        ImageView slideimageview= view.findViewById(R.id.slide_image);
        TextView slideheading = view.findViewById(R.id.slide_heading);
        TextView slidedesc = view.findViewById(R.id.slide_desc);

        slideimageview.setImageResource(slide_image[position]);
        slideheading.setText(slide_heading[position]);
        slidedesc.setText( slide_desc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
