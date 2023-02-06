package com.example.fetchandroidapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.ArrayList;

import android.os.StrictMode;

import java.lang.*;





public class MainActivity extends AppCompatActivity {

    //Create Buttons
    private Button listIDBtn;
    private Button nameBtn;
    private Button originalTextBtn;
    private TextView textBox;

    //Create ArrayLists to be used later
    private ArrayList<String> originalList;
    private ArrayList<String> listID;

    private ArrayList<String> nameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is something I had to add because the app would shut down without it.
        //It also helped a lot with debugging.
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        listIDBtn = findViewById(R.id.listIDBtn);
        nameBtn = findViewById(R.id.nameBtn);
        originalTextBtn = findViewById(R.id.originalTextBtn);
        textBox = findViewById(R.id.textBox);
        textBox.setMovementMethod(new ScrollingMovementMethod());

        //Code to grab html from website
        URL url = null;

        try {
            url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //This called all the methods I mad in order to sort the list of items.
        try {
            htmlToOriginalList(getHTML(url));
            originalListToListID(originalList);
            listIDToName(originalList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Listeners for the buttons
        //I put it in a try-catch loop because it was not guaranteed to work unless the lists above were not empty.
        try {
            listIDBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("checkstyle:FinalParameters")
                @Override
                public void onClick(final View view) {
                    String display = "";
                    for (int i = 0; i < listID.size(); i++) {
                        display += listID.get(i) + "\n";
                    }
                    textBox.setText(display);
                }
            });

            nameBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("checkstyle:FinalParameters")
                @Override
                public void onClick(final View view) {
                    String display = "";
                    for (int i = 0; i < nameList.size(); i++) {
                        display += nameList.get(i) + "\n";
                    }
                    textBox.setText(display);
                }
            });

            originalTextBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("checkstyle:FinalParameters")
                @Override
                public void onClick(final View view) {
                    String display = "";
                    for (int i = 0; i < originalList.size(); i++) {
                        display += originalList.get(i) + "\n";
                    }
                    textBox.setText(display);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }





    }


    //Method to grab the html from the website and from that I could grab the list of items.
    public static String getHTML(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        //Had to use a user-agent to retrieve web content
        urlConnection.setRequestProperty("User-Agent",
                "Mozilla/5.0 " + "(Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String input = "";
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            //This was used to delete the double quotes, so that my string manipulation would be easier later.
            String result = line.replaceAll("\"", "");
            input += result + "\n";
        }

        return input;
    }

    //Converted the original input list (minus the double quotes) to a nice ArrayList, in the original order.
    private void htmlToOriginalList(String input) {
        ArrayList<String> arrList = new ArrayList<>();
        String[] inputArray = input.split("\n");
        for (int i = 1; i < inputArray.length - 1; i++) {
            arrList.add(inputArray[i]);
        }
        originalList = arrList;
    }

    //Converted the original ordered list to the same list, but ordered by ID.
    private void originalListToListID(ArrayList<String> input) {
        ArrayList<String> arrList1 = new ArrayList<>();
        ArrayList<String> arrList2 = new ArrayList<>();
        ArrayList<String> arrList3 = new ArrayList<>();
        ArrayList<String> arrList4 = new ArrayList<>();

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

        ArrayList<String> arrList = new ArrayList<>();

        for (int i = 0; i < arrList1.size(); i++) {
            arrList.add(arrList1.get(i));
        }
        for (int i = 0; i < arrList2.size(); i++) {
            arrList.add(arrList2.get(i));
        }
        for (int i = 0; i < arrList3.size(); i++) {
            arrList.add(arrList3.get(i));
        }
        for (int i = 0; i < arrList4.size(); i++) {
            arrList.add(arrList4.get(i));
        }

        listID = arrList;
    }

    //Converted the original list to be ordered by name.
    //I was originally going to go from ID to name, but it was easier to go order by ID and name at the same time.
    //Although it makes the function longer, it was easier on my end to read when I created it.
    private void listIDToName(ArrayList<String> input) {
        String[] list1 = new String[input.size()];
        String[] list2 = new String[input.size()];
        String[] list3 = new String[input.size()];
        String[] list4 = new String[input.size()];

        for (int i = 0; i < input.size(); i++) {
            String[] line = input.get(i).split(" ");
            char num = line[3].charAt(0);

            if (line[5].contains("Item") == false) {
                continue;
            }

            if (num == '1') {
                int position = Integer.parseInt(line[1].substring(0, line[1].length() - 1));
                list1[position] = input.get(i);
            }
            if (num == '2') {
                int position = Integer.parseInt(line[1].substring(0, line[1].length() - 1));
                list2[position] = input.get(i);
            }
            if (num == '3') {
                int position = Integer.parseInt(line[1].substring(0, line[1].length() - 1));
                list3[position] = input.get(i);
            }
            if (num == '4') {
                int position = Integer.parseInt(line[1].substring(0, line[1].length() - 1));
                list4[position] = input.get(i);
            }
        }

        ArrayList<String> arrList = new ArrayList<>();

        for (int i = 0; i < list1.length; i++) {
            if (list1[i] != null) {
                arrList.add(list1[i]);
            }
        }
        for (int i = 0; i < list2.length; i++) {
            if (list2[i] != null) {
                arrList.add(list2[i]);
            }
        }
        for (int i = 0; i < list3.length; i++) {
            if (list3[i] != null) {
                arrList.add(list3[i]);
            }
        }
        for (int i = 0; i < list4.length; i++) {
            if (list4[i] != null) {
                arrList.add(list4[i]);
            }
        }

        nameList = arrList;
    }

}



