package com.eliot.notebook.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eliot.notebook.R;

public class UserInformationActivity extends AppCompatActivity {
    Button buttonSaveInformation;
    EditText editTextUserName, editTextUserDesc;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        buttonSaveInformation = findViewById(R.id.button_user_information_save_information);
        editTextUserName = findViewById(R.id.edit_text_user_information_user_name);
        editTextUserDesc = findViewById(R.id.edit_text_user_information_user_desc);
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        buttonSaveInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString();
                String userDesc = editTextUserDesc.getText().toString();
                if (userName != null && !userName.equals("") && userDesc != null && !userDesc.equals(""))
                {
                    editor.putString("user_name", userName);
                    editor.putString("user_desc", userDesc);
                    editor.commit();
                }
            }
        });
    }
}
