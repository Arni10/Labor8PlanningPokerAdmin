package com.example.labor8planningpokeradmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static String PATH = "Questions/";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String key;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Questions> questions;
    private Button addButton;
    private EditText problemEditText;
    private FragmentTransaction fragmentTransaction;

    public HomeFragment(String key) {
        this.key = key;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(PATH+key);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questions = new ArrayList<Questions>();
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions.clear();
                for (DataSnapshot problemSnapshot : dataSnapshot.getChildren()){
                    Questions question = problemSnapshot.getValue(Questions.class);
                    questions.add(question);
                }
                mAdapter = new QuestionsAdapter(questions, getContext(),fragmentTransaction);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);
        fragmentTransaction = getFragmentManager().beginTransaction();
        recyclerView = view.findViewById(R.id.questionsRecyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new QuestionsAdapter(questions, view.getContext(),fragmentTransaction);
        recyclerView.setAdapter(mAdapter);

        problemEditText = view.findViewById(R.id.problemEditText);
        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = problemEditText.getText().toString();
                if (TextUtils.isEmpty(string)){
                    Toast.makeText(view.getContext(),"Some empty fields!",Toast.LENGTH_SHORT).show();
                }else {
                    problemEditText.setText("");
                    Questions question = new Questions(string,key,false);
                    reference.child(string).setValue(question).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(),"Question added!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

}
