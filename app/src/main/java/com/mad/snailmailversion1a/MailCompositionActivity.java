package com.mad.snailmailversion1a;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

public class MailCompositionActivity extends AppCompatActivity {


    private Button mSendMailButton;
    private FirebaseUser mFirebaseUser;

    private EditText titleET;
    private EditText messageET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_composition);

        titleET = (EditText)findViewById(R.id.composition_title);

        mSendMailButton = (Button) findViewById(R.id.send);
        mSendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnailMail snailMail = new SnailMail();
                snailMail.setId("testKey1");
                snailMail.setSenderName(mFirebaseUser.getDisplayName());
                snailMail.setTitle(titleET.getText().toString());
                snailMail.setMessage(messageET.getText().toString());
            }
        });
    }
}
