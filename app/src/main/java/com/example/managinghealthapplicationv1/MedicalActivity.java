package com.example.managinghealthapplicationv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicalActivity extends AppCompatActivity {

    private CircleImageView userProfileImage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private static final int GalleryPicker = 1;
    private StorageReference UserProfileImageRef;
    private ProgressDialog loadingBar;
    private Button SaveMedID;
    private EditText mname, mweight, mheight, mbloodtype, mcondition, mreaction, mmedication;
    private MedicalInfo medicalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        userProfileImage = findViewById(R.id.profile_image);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        loadingBar = new ProgressDialog(this);
        SaveMedID = findViewById(R.id.updateid);
        mname = findViewById(R.id.mname);
        mweight = findViewById(R.id.mweight);
        mheight = findViewById(R.id.mheight);
        mbloodtype = findViewById(R.id.mbloodtype);
        mcondition = findViewById(R.id.mcondition);
        mreaction = findViewById(R.id.mreaction);
        mmedication = findViewById(R.id.mmedication);
        medicalInfo = new MedicalInfo();


        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image")))
                        {

                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(retrieveProfileImage).into(userProfileImage);


                            userProfileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent galleryIntent = new Intent();
                                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    galleryIntent.setType("image/*");
                                    startActivityForResult(galleryIntent, GalleryPicker);
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(MedicalActivity.this, "Please set a profile image when filling in the Medical ID", Toast.LENGTH_SHORT).show();

                            userProfileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent galleryIntent = new Intent();
                                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    galleryIntent.setType("image/*");
                                    startActivityForResult(galleryIntent, GalleryPicker);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        SaveMedID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Float height = Float.parseFloat(mheight.getText().toString().trim());
                Float weight = Float.parseFloat(mweight.getText().toString().trim());

                medicalInfo.setName(mname.getText().toString().trim());
                medicalInfo.setBloodtype(mbloodtype.getText().toString().trim());
                medicalInfo.setMedcondition(mcondition.getText().toString().trim());
                medicalInfo.setMedreaction(mreaction.getText().toString().trim());
                medicalInfo.setMedmedication(mmedication.getText().toString().trim());
                medicalInfo.setHeight(height);
                medicalInfo.setWeight(weight);
                RootRef.child("Users").child(currentUserID).child("User Medical Profile").setValue(medicalInfo);
                Toast.makeText(MedicalActivity.this, "Medical ID information successfully updated", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPicker && resultCode == RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Medical ID Image");
                loadingBar.setMessage("Please wait, your Medical ID image is updating...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();


                StorageReference filepath = UserProfileImageRef.child(currentUserID + ".jpg");

                final UploadTask uploadTask = filepath.putFile(resultUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(MedicalActivity.this, "Medical ID Image Updated", Toast.LENGTH_SHORT).show();

                                final String downloadUrl = uri.toString();

                                RootRef.child("Users").child(currentUserID).child("image")
                                        .setValue(downloadUrl);
                                loadingBar.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(MedicalActivity.this, "An unknown error occurred, please check your internet connection", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        });
                    }
                });
            }
        }
    }
}
