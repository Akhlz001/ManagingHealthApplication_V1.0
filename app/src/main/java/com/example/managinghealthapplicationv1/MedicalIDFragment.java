package com.example.managinghealthapplicationv1;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MedicalIDFragment extends Fragment {
    TextView a, b, c, d, e, f, g, h;
    private CircleImageView userProfileImage;
    DatabaseReference RootKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicalid, container, false);





        a = view.findViewById(R.id.rname);
        b = view.findViewById(R.id.rage);
        c = view.findViewById(R.id.rheight);
        d = view.findViewById(R.id.rweight);
        e = view.findViewById(R.id.rbloodtype);
        f = view.findViewById(R.id.rcondition);
        g = view.findViewById(R.id.rreaction);
        h = view.findViewById(R.id.rmedication);




        RootKey = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Medical Profile");
        RootKey .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {

                    String name = dataSnapshot.child("name").getValue().toString();
                    String age = dataSnapshot.child("age").getValue().toString();
                    String height = dataSnapshot.child("height").getValue().toString();
                    String weight = dataSnapshot.child("weight").getValue().toString();
                    String bloodtype = dataSnapshot.child("bloodtype").getValue().toString();
                    String medcondition = dataSnapshot.child("medcondition").getValue().toString();
                    String medreaction = dataSnapshot.child("medreaction").getValue().toString();
                    String medmedication = dataSnapshot.child("medmedication").getValue().toString();

                    a.setText(name);
                    b.setText(age);
                    c.setText(height);
                    d.setText(weight);
                    e.setText(bloodtype);
                    f.setText(medcondition);
                    g.setText(medreaction);
                    h.setText(medmedication);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        userProfileImage = view.findViewById(R.id.medical_image);

        RootKey = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        RootKey .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(retrieveProfileImage).into(userProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        return view;
    }

}

