package app.crm.tosyali.tosyalicrm.authentication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.crm.tosyali.tosyalicrm.R;
import app.crm.tosyali.tosyalicrm.tools.DialogProgress;

import static app.crm.tosyali.tosyalicrm.authentication.AuthFormsFragment.switchToFragment;
import static app.crm.tosyali.tosyalicrm.tools.AuthFunctions.UpdateUI;
import static app.crm.tosyali.tosyalicrm.tools.InputValidation.arePasswordsMatch;
import static app.crm.tosyali.tosyalicrm.tools.InputValidation.isValidEmail;
import static app.crm.tosyali.tosyalicrm.tools.InputValidation.isValidPassword;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;

    public RegisterFragment() {

    }

    private TextInputLayout emailInput, passwordInput, confirmPasswordInput;
    private EditText emailEdit, passwordEdit, confirmPasswordEdit;
    private Button registerButton;
    private TextView loginText;

    private DialogProgress dialogProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        bindViews(view);
        uiEvents();

        return view;
    }

    private void bindViews(View view) {

        emailInput = (TextInputLayout) view.findViewById(R.id.layout_input_register_frag_username);
        passwordInput = (TextInputLayout) view.findViewById(R.id.layout_input_register_frag_password);
        confirmPasswordInput = (TextInputLayout) view.findViewById(R.id.layout_input_register_frag_conf_password);

        emailEdit = (EditText) view.findViewById(R.id.edit_register_frag_username);
        passwordEdit = (EditText) view.findViewById(R.id.edit_register_frag_password);
        confirmPasswordEdit = (EditText) view.findViewById(R.id.edit_register_frag_conf_password);

        registerButton = (Button) view.findViewById(R.id.button_register);

        loginText = (TextView) view.findViewById(R.id.text_register_frag_switch_to_login);

        enableButton();

        dialogProgress = new DialogProgress(getContext());

    }

    private void uiEvents() {
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailInput.setError(getString(R.string.invalid_email_message));
                emailInput.setErrorEnabled(!isValidEmail(charSequence.toString()));
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInput.setError(getString(R.string.invalid_password_message));
                passwordInput.setErrorEnabled(!isValidPassword(charSequence.toString()));
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        confirmPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPasswordInput.setError(getString(R.string.invalid_confirm_password_message));
                confirmPasswordInput.setErrorEnabled(!arePasswordsMatch(passwordEdit.getText().toString(),charSequence.toString()));
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProgress.initDialog();
                createNewUser(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFragment(0);
            }
        });
    }

    private void enableButton(){
        boolean validEmail = isValidEmail(emailEdit.getText().toString()),
                validPassword = isValidPassword(passwordEdit.getText().toString()),
                passwordsMatch = arePasswordsMatch(passwordEdit.getText().toString(), confirmPasswordEdit.getText().toString());
        registerButton.setClickable( validEmail && validPassword && passwordsMatch);
    }

    private void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        dialogProgress.dismissDialog();
                        if (task.isSuccessful()) {
                            Log.i("Register","isSuccessful ? "+task.isSuccessful());
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        } else {
                            Log.i("Register","isSuccessful ? "+task.isSuccessful());
                            Log.i("RegisterFragment", task.getException().getMessage());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            UpdateUI(null);
                        }
                    }
                });
    }

}
