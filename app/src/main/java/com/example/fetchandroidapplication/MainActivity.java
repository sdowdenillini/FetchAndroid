package com.example.fetchandroidapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.ArrayList;

import android.os.StrictMode;




public class MainActivity extends AppCompatActivity {

    private Button listIDBtn;
    private Button nameBtn;
    private Button filterBlankBtn;
    private Button originalTextBtn;
    private TextView textBox;

    private ArrayList<String> originalList = null;
    private ArrayList<String> listID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        listIDBtn = findViewById(R.id.listIDBtn);
        nameBtn = findViewById(R.id.nameBtn);
        filterBlankBtn = findViewById(R.id.filterBlankBtn);
        originalTextBtn = findViewById(R.id.originalTextBtn);
        textBox = findViewById(R.id.textBox);
        textBox.setMovementMethod(new ScrollingMovementMethod());


        URL url = null;

        try {
            url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            textBox.setText(getHTML(url));
            htmlToOriginalList(getHTML(url));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            originalListToListID(originalList);
            for (int i = 0; i < listID.size(); i++) {
                System.out.println(listID.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static String getHTML(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent",
                "Mozilla/5.0 " + "(Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String input = "";
        String line = null;

        while ((line = in.readLine()) != null) {
            String result = line.replaceAll("\"", "");
            input += result + "\n";
        }

        return input;
    }

    private void htmlToOriginalList(String input) {
        ArrayList<String> arrList = new ArrayList<String>();
        String[] inputArray = input.split("\n");
        for (int i = 1; i < inputArray.length - 1; i++) {
            arrList.add(inputArray[i]);
        }
        originalList = arrList;
    }

    private void originalListToListID(ArrayList<String> input) {
        ArrayList<String> arrList1 = new ArrayList<String>();
        ArrayList<String> arrList2 = new ArrayList<String>();
        ArrayList<String> arrList3 = new ArrayList<String>();
        ArrayList<String> arrList4 = new ArrayList<String>();

        for (int i = 0; i < input.size(); i++) {
            String[] line = input.get(i).split(" ");
            char num = line[3].charAt(0);

            if (num == '1') {
                arrList1.add(input.get(i));
            } else if (num == '2') {
                arrList2.add(input.get(i));
            } else if (num == '3') {
                arrList3.add(input.get(i));
            } else {
                arrList4.add(input.get(i));
            }
        }

        listID.addAll(arrList1);
        listID.addAll(arrList2);
        listID.addAll(arrList3);
        listID.addAll(arrList4);
    }

}



