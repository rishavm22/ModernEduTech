package com.modernedutech.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.modernedutech.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ProfileEditActivity extends AppCompatActivity {

    ImageView pImage;
    EditText name,institute,mobile;
    RadioGroup toCheck;
    CheckBox toCheckPrincipal;

    FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;

    private Uri ImgUri;
    public static final int ImageRequest = 1;
    ProgressDialog dialog;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        pImage = findViewById(R.id.pImage);
        name = findViewById(R.id.pName);
        institute = findViewById(R.id.pInstituteName);
        mobile = findViewById(R.id.pMobile);
        toCheck =findViewById(R.id.toCheckType);
        toCheckPrincipal =findViewById(R.id.toCheckPrincipal);

        dialog = new ProgressDialog( this);
        dialog.setMessage("Image Uploading ...");

        user = FirebaseAuth.getInstance().getCurrentUser();
        sharedPreferences = getSharedPreferences("com.modernedutech."+user.getUid(), Context.MODE_PRIVATE);

        storageReference = FirebaseStorage.getInstance().getReference("Uploads").child(user.getUid()).child("Profile");

    }

    public void submit(View view) {



        String name_text,institute_text,mobile_num;
        name_text = name.getText().toString();
        institute_text = institute.getText().toString();
        mobile_num = mobile.getText().toString();
        if (name_text.isEmpty() && institute_text.isEmpty() && mobile_num.isEmpty()){

        }else{
            reference = FirebaseDatabase.getInstance().getReference("Users").child("Info").child(user.getUid());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Id", user.getUid());
            hashMap.put("username", name_text);
            hashMap.put("phoneNo", mobile_num);
            hashMap.put("status","offline");
            reference.setValue(hashMap);

            if(toCheckPrincipal.isChecked()){
                reference = FirebaseDatabase.getInstance().getReference("Users").child("Info").child(user.getUid());
                reference.child("Designation").setValue("Principal");
            }

        }
        Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
        name.setText("");
        institute.setText("");
        mobile.setText("");
        toCheckPrincipal.setChecked(false);

    }

    public void setProfilePic(View view) {
        openImage();
    }
    private void openImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, ImageRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageRequest && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            ImgUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),ImgUri);

                pImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(){

        dialog.show();

        if(ImgUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+ImgUri.getLastPathSegment());
            fileReference.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri download = uri ;
                            sharedPreferences = getSharedPreferences(
                                    "com.modernedutech."+user.getUid()
                                    , Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("pImage",download.toString());
                            editor.apply();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child("Info").child(user.getUid());
                            reference.child("imageUrl").setValue(download.toString());
                            dialog.dismiss();
                        }
                    });

                }

            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText( getApplicationContext(), "Upload Failed",Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    public void uploadPic(View view) {
        uploadImage();
    }
}