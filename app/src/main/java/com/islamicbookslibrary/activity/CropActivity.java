package com.islamicbookslibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.edmodo.cropper.CropImageView;
import com.islamicbookslibrary.BaseActivity;
import com.islamicbookslibrary.R;
import com.islamicbookslibrary.util.Constants;
import com.islamicbookslibrary.util.Util;

public class CropActivity extends BaseActivity {

    private CropImageView cropImageView;
    private String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);


    }

    @Override
    public void initVariable() {

    }

    @Override
    public void findViews() {

        cropImageView = (CropImageView) links(R.id.CropImageView);


        links(R.id.btn_Done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = Util.saveImage(fileUrl, cropImageView.getCroppedImage());
                Intent i = new Intent();
                i.putExtra("uri", uri);
                setResult(Constants.CROP_IMAGE, i);
                finish();
            }
        });
    }

    @Override
    public void postInitView() {
        if (getIntent().getExtras() != null) {
            fileUrl = getIntent().getStringExtra("FilePath");
            cropImageView.setImageBitmap(Util.getUriToBitmap(fileUrl));
        }

        cropImageView.setGuidelines(1);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAspectRatio(2, 1);
    }

    @Override
    public void addAdapter() {

    }

    @Override
    public void loadData() {

    }
}
