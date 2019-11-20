package com.hayatcode.scanner.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hayatcode.scanner.R;
import com.hayatcode.scanner.Static;
import com.hayatcode.scanner.adapter.InfosAdapter;
import com.hayatcode.scanner.model.MedicalInfo;
import com.hayatcode.scanner.model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class EmergencyFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    RecyclerView recyclerViewMedications, recyclerViewAllergies, recyclerViewDiseases;

    InfosAdapter MedsAdapter, AllergiesAdapter, DiseasesAdapter;

    TextView TV_allgergies, TV_medications, TV_diseases;
    ImageView IV_empty;

    TextView TV_blood;
    Button BT_blood;
    ArrayList<MedicalInfo> allgergies, diseases, medications;
    User user;

    public static EmergencyFragment newInstance(int index) {
        EmergencyFragment fragment = new EmergencyFragment();
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
        View root = inflater.inflate(R.layout.fragment_emergency, container, false);

        recyclerViewAllergies = root.findViewById(R.id.recyclerViewAllergies);
        recyclerViewMedications = root.findViewById(R.id.recyclerViewMedications);
        recyclerViewDiseases = root.findViewById(R.id.recyclerViewDiseases);

        TV_blood = root.findViewById(R.id.blood_type);

        TV_allgergies = root.findViewById(R.id.allergies_label);
        TV_diseases = root.findViewById(R.id.diseases_label);
        TV_medications = root.findViewById(R.id.med_label);
        IV_empty = root.findViewById(R.id.empty);

        allgergies = new ArrayList<>();
        diseases = new ArrayList<>();
        medications = new ArrayList<>();

        MedsAdapter = new InfosAdapter(getActivity(), medications);
        AllergiesAdapter = new InfosAdapter(getActivity(), allgergies);
        DiseasesAdapter = new InfosAdapter(getActivity(), diseases);

        recyclerViewAllergies.setAdapter(AllergiesAdapter);
        recyclerViewMedications.setAdapter(MedsAdapter);
        recyclerViewDiseases.setAdapter(DiseasesAdapter);

        recyclerViewAllergies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMedications.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDiseases.setLayoutManager(new LinearLayoutManager(getActivity()));


        String blood = user.getBlood().isEmpty() ? "O positive" : user.getBlood();

        TV_blood.setText(blood);

        addItems();


        return root;
    }


    private void addItems() {

        HashMap<String, MedicalInfo> medicalInfos = user.getMedInfos();

        diseases.clear();
        medications.clear();
        allgergies.clear();

        for (MedicalInfo medicalInfo : medicalInfos.values()) {
            if (medicalInfo.getPrivacy() <= Static.privacy) {
                if (medicalInfo.getType().equals("disease")) {
                    diseases.add(medicalInfo);
                } else if (medicalInfo.getType().equals("medication")) {
                    medications.add(medicalInfo);
                } else {
                    allgergies.add(medicalInfo);

                }
            }

        }

        if (!diseases.isEmpty())
            TV_diseases.setVisibility(View.VISIBLE);
        if (!allgergies.isEmpty())
            TV_allgergies.setVisibility(View.VISIBLE);
        if (!medications.isEmpty())
            TV_medications.setVisibility(View.VISIBLE);

        if (medicalInfos.isEmpty())
            IV_empty.setVisibility(View.VISIBLE);
        else
            IV_empty.setVisibility(View.INVISIBLE);


        MedsAdapter.notifyDataSetChanged();
        AllergiesAdapter.notifyDataSetChanged();
        DiseasesAdapter.notifyDataSetChanged();
    }


}