package Genuine.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import Genuine.com.activity.SignInActivity;
import Genuine.com.activity.SignUpActivity;

public class SignChoiceActivity extends AppCompatActivity {
     CardView particulier;
     CardView pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_choice);
        pro= (CardView)findViewById(R.id.pro);
particulier =(CardView) findViewById(R.id.particulier);

pro.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
Intent intent = new Intent(SignChoiceActivity.this, SignUpActivity.class);
    startActivity(intent);
    }
});
particulier.setOnClickListener((new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(SignChoiceActivity.this, SignUpActivity.class);
        startActivity(intent);


    }
}));
    }
}