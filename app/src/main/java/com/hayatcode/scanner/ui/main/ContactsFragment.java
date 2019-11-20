package com.hayatcode.scanner.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hayatcode.scanner.R;
import com.hayatcode.scanner.Static;
import com.hayatcode.scanner.adapter.ContactsAdapter;
import com.hayatcode.scanner.model.Contact;
import com.hayatcode.scanner.model.User;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    ArrayList<Contact> contacts = new ArrayList<>();
    ImageView IV_empty;

    User user;

    public static ContactsFragment newInstance(int index) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = Static.client;

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
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewDideases);
        IV_empty = root.findViewById(R.id.empty);

        user = Static.client;


        contacts.clear();
        for (Contact contact : user.getContacts()) {
            if (contact.getPrivacy() <= Static.privacy) {
                contacts.add(contact);
            }
        }

        contactsAdapter = new ContactsAdapter(getActivity(), contacts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contactsAdapter);

        if (contacts.isEmpty())
            IV_empty.setVisibility(View.VISIBLE);
        else
            IV_empty.setVisibility(View.INVISIBLE);

        return root;
    }


}