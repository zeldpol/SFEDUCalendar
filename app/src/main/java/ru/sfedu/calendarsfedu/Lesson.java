package ru.sfedu.calendarsfedu;




import android.text.format.Time;

import java.util.ArrayList;
import java.util.List;


public class Lesson {
    String name;
    String auditorium;
    String teacher;
    String begin;
    String end;
    Lesson(String name, String auditorium, String teacher, String begin, String end) {
        this.name = name;
        this.auditorium = auditorium;
        this.teacher = teacher;
        this.begin = begin;
        this.end = end;
    }
}
