package com.example.bsy.initialquiz_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText answer_EditText;
    TextView hint_TextView;
    Button confirm_EditText;

    private ArrayList<Quiz> quizs = new ArrayList<Quiz>() ;
    private Quiz quiz = new Quiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answer_EditText = (EditText) findViewById(R.id.answer_EditText);
        hint_TextView = (TextView) findViewById(R.id.hint_TextView);
        confirm_EditText = (Button) findViewById(R.id.confirm_Button);

        quizs = quiz.getQuizs();

        confirm_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "정답!!!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 150);
                toast.show();

            }
        });

    }


}
