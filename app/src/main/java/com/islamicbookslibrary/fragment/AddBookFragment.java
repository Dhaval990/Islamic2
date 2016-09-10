package com.islamicbookslibrary.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.islamicbookslibrary.BaseFragment;
import com.islamicbookslibrary.R;
import com.islamicbookslibrary.activity.CropActivity;
import com.islamicbookslibrary.model.Product;
import com.islamicbookslibrary.util.Constants;
import com.islamicbookslibrary.util.Logg;
import com.islamicbookslibrary.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = AddBookFragment.class.getSimpleName();


    private static final String REQUIRED = "Required";
    public Bitmap bitmap;
    // [END declare_database_ref]
    boolean isImageUpdate = false;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private EditText mTitleField;
    private EditText mDescription;
    private ImageView imgBook;
    private Uri path;

    public AddBookFragment() {
        setContentView(R.layout.fragment_add_book);
    }

    @Override
    public void initVariable() {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }

    @Override
    public void findViews() {

        mTitleField = (EditText) links(R.id.field_title);
        mDescription = (EditText) links(R.id.field_body);
        imgBook = (ImageView) links(R.id.img_book);

    }



    private void submitPost() {
        showProgressDialog();
        final String title = mTitleField.getText().toString();
        final String description = mDescription.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            hideProgressDialog();
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(description)) {
            mDescription.setError(REQUIRED);
            hideProgressDialog();
            return;
        }


        Product product = new Product();
        product.setProTitle(title);
        product.setProDescription(description);

        mDatabase.child("/products/").push().setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void postInitView() {
        imgBook.setOnClickListener(this);
    }

    @Override
    public void loadData() {
    }

    // [START post_stars_transaction]

    @Override
    public void addAdapter() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // Query for get the data
    private Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("products");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_book:
                onGallaryOptionClicked();
                break;


        }
    }

    private void onGallaryOptionClicked() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Constants.CAPTURE_GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAPTURE_GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && (data.getData() != null)) {
                Uri fileUri = data.getData();
                String PathGallery = Util.getRealPathFromURI(fileUri, getActivity());
                Logg.d("", "Gallery Path: " + PathGallery);
                if (!TextUtils.isEmpty(PathGallery)) {
                    bitmap = Util.getBitmapDefault(PathGallery, 1000, 1000);
                    if (bitmap != null) {
                        Util.saveImage(PathGallery, bitmap);
                        navigateToCropActivity(PathGallery);
                    }
                }
            }
        } else if (requestCode == Constants.CROP_IMAGE) {
            if (data != null) {
                isImageUpdate = true;
                //file:///storage/emulated/0/a1681a38-cedf-4dcf-b12f-d4046cfca472.jpg
                path = Uri.parse("file://" + data.getStringExtra("uri"));


                Glide.with(mContext).load(path).error(ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher)).into(imgBook);
                //img_user.setImageBitmap(Util.getUriToBitmap(data.getStringExtra("uri")));
            }
        }
    }

    private void navigateToCropActivity(String path) {
        // create new Intent
        Intent intent = new Intent(getActivity(), CropActivity.class);
        // create a file to save image
        intent.putExtra("FilePath", path);

        // start the image Capture Intent
        startActivityForResult(intent, Constants.CROP_IMAGE);
    }
}
