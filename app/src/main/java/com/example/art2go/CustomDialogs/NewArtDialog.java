package com.example.art2go.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.art2go.Model.Art;
import com.example.art2go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class NewArtDialog {

    public static NewArtDialog mDialog;
    public static NewArtDialog getInstance(){
        if(mDialog==null)
            mDialog = new NewArtDialog();
        return mDialog;
    }

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    ImageView art_image, back;
    TextView art_name, art_description, art_author;

    public void showDialog(Context context, Art art){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_art_layout);

        back =dialog.findViewById(R.id.back);
        art_image = dialog.findViewById(R.id.art_image);
        art_name = dialog.findViewById(R.id.txt_art_name);
        art_description = dialog.findViewById(R.id.txt_art_description);
        art_author = dialog.findViewById(R.id.txt_art_author);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();             }
        });
        if (art.getImagePath() != null) {
            String mDrawableName = art.getImagePath() + ".jpg";
            try {
                storageReference.child("Images").child(mDrawableName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(art_image);
                        //Picasso.get().load(uri).into(imageSculpture);
                    }
                });
            } catch (Exception ex) {
                Log.e("CustomInfoWindowAdapter", ex.getMessage());
                //ivArtImage.setImageResource();

            }
        }
        art_name.setText(art.getName());
        art_author.setText(art.getAuthor());
        art_description.setText(art.getDescription());

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
