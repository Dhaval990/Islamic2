package com.islamicbookslibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.islamicbookslibrary.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by dhaval on 19/4/16.
 */
public class Util {


    /**
     * @param mContext
     * @param layout
     * @param fragment
     * @param isBackstack
     * @param TAG
     */
    public static void changeFragment(Context mContext, int layout, Fragment fragment, boolean isBackstack, String TAG) {
        ((BaseActivity) mContext).changeFragment(layout, fragment, isBackstack, TAG);
    }

    /**
     * @param view view
     * @param text message
     */
    public static void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * @param context
     * @param resourceId
     */
    public static Drawable getDrawable(Context context, int resourceId) {
        return ContextCompat.getDrawable(context, resourceId);
    }

    /**
     * @param context
     * @param colorCode
     * @return
     */
    public static int getColor(Context context, int colorCode) {
        return ContextCompat.getColor(context, colorCode);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Notifaction");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Logg.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == Constants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    // Before Deprecation of managed Query
    public static String getRealPathFromURI(Uri contentUri, Activity mContext) {

        String StringPath;

        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = mContext.managedQuery(contentUri, proj, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            StringPath = cursor.getString(column_index);

        } else {
            StringPath = null;
        }

        if (StringPath == null) {
            StringPath = contentUri.getPath();
        }

        if (StringPath.equals("")) {
            return null;
        } else {
            return StringPath;
        }
    }

    /***/
    public static Bitmap getBitmapDefault(String pathOfInputImage, int outwidth,
                                          int outheight) {
        try {
            int inWidth = 0;
            int inHeight = 0;
            // pathOfInputImage = Environment.getExternalStorageDirectory()
            // .toString() + "/ca7ch/" + pathOfInputImage;
            InputStream in = new FileInputStream(pathOfInputImage);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(pathOfInputImage);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / outwidth, inHeight
                    / outheight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(),
                    roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, outwidth, outheight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
                    (int) (roughBitmap.getWidth() * values[0]),
                    (int) (roughBitmap.getHeight() * values[4]), true);
            /*
             * Now just have to check orientation
			 */
            ExifInterface exif = new ExifInterface(pathOfInputImage);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            Logg.d("log_tag", "orientation: " + orientation);
            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Logg.d("Tag", "Angle: " + angle);

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            if (angle != 0) {
                Bitmap bitmap2 = Bitmap.createBitmap(resizedBitmap, 0, 0,
                        resizedBitmap.getWidth(), resizedBitmap.getHeight(),
                        mat, true);

                resizedBitmap.recycle();

                return bitmap2;
            }
            return resizedBitmap;

            // return resizedBitmap;
            // // save image
            // try {
            // FileOutputStream out = new FileOutputStream(pathOfOutputImage);
            // resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // } catch (Exception e) {
            // Loog.d(("Image", e.getMessage(), e);
            // }
        } catch (IOException e) {
            Logg.d("Image", e.getMessage());
            return null;
        }
    }

    public static Bitmap getUriToBitmap(String fileUrl) {
        File imgFile = new File(fileUrl);
        Bitmap myBitmap = null;
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }

    public static String saveImage(String fileUrl, Bitmap bitmap) {

        /*for (String s : Uri.parse(fileUrl).get()) {
            Loog.A("Sagment", s);
        }*/
        //File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Notifaction");
        File myDir = new File(fileUrl.substring(0, fileUrl.lastIndexOf('/')));
        myDir.mkdirs();
        String fname = Uri.parse(fileUrl).getLastPathSegment();
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Uri.parse(fileUrl).getLastPathSegment();
        File file = new File(myDir, date + fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(file.getPath()).getPath();
    }

    /**
     * @param res
     * @param resId
     * @param theme
     * @return
     */
    public Drawable getVectorImage(@NonNull Resources res, @DrawableRes int resId,
                                   @Nullable Resources.Theme theme) {
        Drawable drawabl = VectorDrawableCompat.create(res, resId, theme);
        return drawabl;
    }

    public void drawableLeft(TextView textView, int resourceId) {

        Drawable drawable = VectorDrawableCompat.create(textView.getResources(), resourceId, textView.getContext().getTheme());
        Drawable[] drawables = textView.getCompoundDrawables();
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, drawables[1], drawables[2], drawables[3]);
    }

    /**
     * @param tag
     * @return
     */
    public Fragment findFragmentByTag(Context context, String tag) {
        return ((BaseActivity) context).findFragmentByTag(tag);
    }
}