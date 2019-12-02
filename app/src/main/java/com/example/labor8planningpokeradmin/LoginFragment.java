package com.example.labor8planningpokeradmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private EditText name, id;
    private Button loginButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public LoginFragment() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login_fragment, container, false);
        name = view.findViewById(R.id.nameEditText);
        id = view.findViewById(R.id.idEditText);
        loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = name.getText().toString();
                final String Key = id.getText().toString();
                if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Key)) {
                    Toast.makeText(view.getContext(), "Some empty fields!", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = database.getReference("Groups").child(Key);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Groups Group = dataSnapshot.getValue(Groups.class);
                            if (Group != null){
                                if(Group.getAdmin().equals(Name)){
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frameLayout, new HomeFragment(Key));
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }else {
                                    Toast.makeText(getContext(), "Group already exists!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Groups group = new Groups(Key, Name);
                                reference.child(Key).setValue(group).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(), "Group added!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frameLayout, new HomeFragment(Key));
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        return view;
    }
}
