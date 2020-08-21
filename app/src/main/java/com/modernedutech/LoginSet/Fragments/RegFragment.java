package com.modernedutech.LoginSet.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.modernedutech.MainActivity;
import com.modernedutech.R;

public class RegFragment extends Fragment {

    EditText email, pass;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg, container, false);

        email = view.findViewById(R.id.emailRegi);
        pass = view.findViewById(R.id.passRegi);
        auth = FirebaseAuth.getInstance();
        return view;
    }
    public void reg(View view) {
        final String text_email = email.getText().toString();
        String text_pass = pass.getText().toString();

        if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_pass)){
            Toast.makeText(getContext(), "Please Fill Info", Toast.LENGTH_SHORT).show();
        }else if (text_pass.length() < 6){
            Toast.makeText(getContext(), "Password Must Be 6 Characters", Toast.LENGTH_SHORT).show();
        }else {
            auth.createUserWithEmailAndPassword(text_email, text_pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

}
