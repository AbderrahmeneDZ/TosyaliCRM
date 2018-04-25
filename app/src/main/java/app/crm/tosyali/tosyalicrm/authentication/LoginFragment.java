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
import static app.crm.tosyali.tosyalicrm.tools.InputValidation.isValidEmail;
import static app.crm.tosyali.tosyalicrm.tools.InputValidation.isValidPassword;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    public LoginFragment() {

    }

    private TextInputLayout emailInput, passwordInput;
    private EditText emailEdit, passwordEdit;
    private Button loginButton;
    private TextView registerText;

    private DialogProgress dialogProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        bindViews(view);

        uiEvents();

        return view;
    }

    private void bindViews(View view) {

        emailInput = (TextInputLayout) view.findViewById(R.id.layout_input_login_frag_username);
        passwordInput = (TextInputLayout) view.findViewById(R.id.layout_input_login_frag_password);

        emailEdit = (EditText) view.findViewById(R.id.edit_login_frag_username);
        passwordEdit = (EditText) view.findViewById(R.id.edit_login_frag_password);

        loginButton = (Button) view.findViewById(R.id.button_login);

        registerText = (TextView) view.findViewById(R.id.text_login_frag_switch_to_register);

        dialogProgress = new DialogProgress(getContext());

        enableButton();
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProgress.initDialog();
                loginUser(emailEdit.getText().toString(),passwordEdit.getText().toString());
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFragment(1);
            }
        });

    }

    private void enableButton(){
        boolean validEmail = isValidEmail(emailEdit.getText().toString()),
                validPassword = isValidPassword(passwordEdit.getText().toString());
        loginButton.setClickable( validEmail && validPassword );
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialogProgress.dismissDialog();
                        if (task.isSuccessful()) {
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            UpdateUI(null);
                        }
                    }
                });
    }

}
