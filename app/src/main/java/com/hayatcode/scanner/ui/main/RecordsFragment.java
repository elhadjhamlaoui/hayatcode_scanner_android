package com.hayatcode.scanner.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hayatcode.scanner.R;
import com.hayatcode.scanner.Static;
import com.hayatcode.scanner.adapter.RecordsAdapter;
import com.hayatcode.scanner.data.UserLocalStore;
import com.hayatcode.scanner.model.Record;
import com.hayatcode.scanner.model.User;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    RecyclerView recyclerView;

    RecordsAdapter recordsAdapter;
    ArrayList<Record> records;
    User user;
    Button BT_add;

    ImageView IV_empty;

    public static RecordsFragment newInstance(int index) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        user = Static.client;

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
        View root = inflater.inflate(R.layout.fragment_records, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        BT_add = root.findViewById(R.id.add);
        IV_empty = root.findViewById(R.id.empty);

        records = new ArrayList<>();
        recordsAdapter = new RecordsAdapter(getActivity(), records);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(recordsAdapter);

        addItems();


        return root;
    }


    private void addItems() {

        records.clear();

        for (Record record : user.getRecords()) {
            if (record.getPrivacy() <= Static.privacy) {
                records.add(record);
            }
        }


        if (records.isEmpty())
            IV_empty.setVisibility(View.VISIBLE);
        else
            IV_empty.setVisibility(View.INVISIBLE);

        recordsAdapter.notifyDataSetChanged();
    }

}