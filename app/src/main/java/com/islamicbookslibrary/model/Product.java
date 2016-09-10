package com.islamicbookslibrary.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Product {

    public Map<String, Boolean> stars = new HashMap<>();
    public Map<String, Boolean> reads = new HashMap<>();
    private String proTitle;
    private String proDescription;
    private String proImage;
    private String proBookPath;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public String getProTitle() {
        return proTitle;
    }

    public void setProTitle(String proTitle) {
        this.proTitle = proTitle;
    }

    public String getProDescription() {
        return proDescription;
    }

    public void setProDescription(String proDescription) {
        this.proDescription = proDescription;
    }

    public String getProImage() {
        return proImage;
    }

    public void setProImage(String proImage) {
        this.proImage = proImage;
    }

    public String getProBookPath() {
        return proBookPath;
    }

    public void setProBookPath(String proBookPath) {
        this.proBookPath = proBookPath;
    }


}
