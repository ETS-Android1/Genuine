package Genuine.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        View rootview = null;
        Button Send_feedback = rootview.findViewById(R.id.post_message);
        EditText name = rootview.findViewById(R.id.your_name);
        EditText email = rootview.findViewById(R.id.your_email);
        EditText subject = rootview.findViewById(R.id.your_subject);
        EditText message = rootview.findViewById(R.id.your_message);

        Send_feedback.setOnClickListener(view -> {
            String b_name = name.getText().toString();
            //name.setText(null);
            String b_email = email.getText().toString();
            //email.setText(null);
            email.clearFocus();
            String b_subject = subject.getText().toString();
            //subject.setText(null);
            subject.clearFocus();
            String b_message = message.getText().toString();
            //message.setText(null);
            message.clearFocus();
            if (TextUtils.isEmpty(b_name)){
                name.setError("Enter Your Name");
                name.requestFocus();
                return;
            }

            if (!isValidEmail(b_email)) {
                email.setError("Invalid Email");
                return;
            }

            if (TextUtils.isEmpty(b_subject)){
                subject.setError("Enter Subject");
                subject.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(b_message)){
                message.setError("Enter Your Message");
                message.requestFocus();
                return;
            }

            Intent emailing = new Intent(Intent.ACTION_SEND);
            emailing.putExtra(Intent.EXTRA_TEXT,b_name);
            emailing.putExtra(Intent.EXTRA_EMAIL, new String[]{"@"});
            emailing.putExtra(Intent.EXTRA_SUBJECT, b_subject);
            emailing.putExtra(Intent.EXTRA_TEXT,
                    "Name: "+b_name+'\n'+"Email: "+b_email+'\n'+"Message: "+'\n'+b_message);


            emailing.setType("email/rfc822");
            startActivity(Intent.createChooser(emailing, "Choose an Email client : "));

            name.setText(null);
            email.setText(null);
            subject.setText(null);
            message.setText(null);
            name.setError(null);
            email.setError(null);
            subject.setError(null);
            message.setError(null);


        });
      


    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return ((Matcher) matcher).matches();
    }


    public void onResume () {
        super.onResume();
    }

    public void onStart(){
        super.onStart();
    }

    public void onStop(){
        super.onStop();
    }


}
