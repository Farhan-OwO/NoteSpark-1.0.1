package com.example.notespark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Firebasemodel {
    private String title;
    private String body;
    private String truncatedBody;
    private int colorCode;

    public Firebasemodel() {
        this.colorCode = randomColor(); // Generate a color when creating a new note
    }
    public Firebasemodel(String title, String body) {
        this.title= title;
        this.body = body;
        this.colorCode = randomColor();
        setBody(body);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        this.truncatedBody = truncateBody(body); // Truncate when setting the body
    }
    public String getTruncatedBody(){
        return truncatedBody;
    }
    public int getColorCode() {
        return colorCode;
    }

    private String truncateBody(String body) {
        int randomLength = getRandomLength();
        if (body.length() > randomLength) {
            return body.substring(0, randomLength) + "...";
        } else {
            return body;
        }
    }
    private int randomColor(){
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.color_one);
        colorcode.add(R.color.color_two);
        colorcode.add(R.color.color_three);
        colorcode.add(R.color.color_four);
        colorcode.add(R.color.color_five);
        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);

    }
    private int getRandomLength() {
        Random random = new Random();
        return random.nextInt(111) + 50; // 111 = 160 - 50 + 1
    }
}