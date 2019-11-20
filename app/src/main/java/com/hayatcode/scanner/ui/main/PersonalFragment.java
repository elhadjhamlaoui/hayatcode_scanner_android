package com.hayatcode.scanner.ui.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.hayatcode.scanner.R;
import com.hayatcode.scanner.Static;
import com.hayatcode.scanner.model.User;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class PersonalFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    EditText ET_firstName, ET_lastName, ET_email;
    TextView TV_birthdate, TV_gender;
    ImageView IV_userPhoto;
    User user;
    Calendar calendar;
    ProgressDialog imageUploadDialog;



    public static PersonalFragment newInstance(int index) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal, container, false);


        ET_email = root.findViewById(R.id.email);
        ET_firstName = root.findViewById(R.id.firstname);
        ET_lastName = root.findViewById(R.id.lastname);
        TV_birthdate = root.findViewById(R.id.birthdate);
        TV_gender = root.findViewById(R.id.sex);
        IV_userPhoto = root.findViewById(R.id.account_picture);




        user = Static.client;



        if (user.getPhoto() != null && !user.getPhoto().isEmpty())
            Glide.with(this).load(user.getPhoto()).into(IV_userPhoto);




        calendar = Calendar.getInstance();


        imageUploadDialog = new ProgressDialog(getActivity());
        imageUploadDialog.setTitle(R.string.uploading_photo);

        //Glide.with(this).load(user.getPhoto()).into(IV_userPhoto);


        TV_gender.setText(user.getGender());
        TV_birthdate.setText(user.getBirthDate());

        ET_firstName.setHint(user.getFirstName());
        ET_lastName.setHint(user.getFamilyName());



        ET_firstName.setHint(user.getFirstName());
        ET_lastName.setHint(user.getFamilyName());
        ET_email.setHint(user.getEmail());




        return root;
    }

}