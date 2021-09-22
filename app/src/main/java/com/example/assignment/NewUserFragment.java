package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUserFragment extends Fragment {


    EditText email;
    TextInputLayout email_layout;
    EditText password;
    TextInputLayout password_layout;
    EditText confpassword;
    TextInputLayout confpassword_layout;
    Button SignUp;
    ProgressBar progress;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_new_user, container, false);

        mAuth = FirebaseAuth.getInstance();

        email = view.findViewById(R.id.editTextEmail);
        password = view.findViewById(R.id.editTextTextPassword);
        confpassword = view.findViewById(R.id.editTextTextPassword2);
        SignUp = view.findViewById(R.id.button);
        progress = view.findViewById(R.id.progressbar);
        email_layout = view.findViewById(R.id.signup_email_edittext);
        password_layout = view.findViewById(R.id.signup_passowrd_edittext);
        confpassword_layout = view.findViewById(R.id.signup_confirm_password_edittext);


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                handleSignup();
            }
        });

        return  view;
    }

    private void handleSignup() {
        if(!isValidEmail()){
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(),"please enter a valid email",Toast.LENGTH_SHORT).show();
            email_layout.setError("InValid");
        }
        else if(!isValidPassword()){
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(),"Password length must be greater than 5",Toast.LENGTH_SHORT).show();
        }
        else if(!isValidConfPassword()){
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(),"password ans confirm-password doen't match",Toast.LENGTH_SHORT).show();
            confpassword_layout.setError("Error");
        }

        else{
            String e  =email.getText().toString();
            String p = password.getText().toString();
            mAuth.createUserWithEmailAndPassword(e,p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getActivity(),"Signup Successfull",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),DashBoardActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),""+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();
    }

    public boolean isValidPassword() {
        if(TextUtils.isEmpty(password.getText().toString()) || password.getText().toString().length()<6){
            password_layout.setError("Invalid data");
            return false;
        }
        else
            return true;
    }

    public boolean isValidConfPassword() {
        if(password.getText().toString().equals(confpassword.getText().toString()))
            return true;
        else
            return false;
    }
}