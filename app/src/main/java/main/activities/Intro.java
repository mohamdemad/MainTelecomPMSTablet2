package main.activities;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;

import main.fragments.Intro_fragment_1;
import main.fragments.Intro_fragment_2;
import main.fragments.Intro_fragment_3;
import main.fragments.Intro_fragment_4;
import main.fragments.Intro_fragment_5;
import main.fragments.Intro_fragment_6;


//////// from http://www.androprogrammer.com/2014/02/add-gesture-in-image-switcher-to-swipe.html//////////
public class Intro extends AppIntro2 {


    @Override
    public void init(Bundle savedInstanceState) {


        addSlide(new Intro_fragment_1());
        addSlide(new Intro_fragment_2());
        addSlide(new Intro_fragment_3());
        addSlide(new Intro_fragment_4());
        addSlide(new Intro_fragment_5());
        addSlide(new Intro_fragment_6());



        setProgressButtonEnabled(false);




    }



    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}