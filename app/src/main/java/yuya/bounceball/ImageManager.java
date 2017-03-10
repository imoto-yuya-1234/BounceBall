package yuya.bounceball;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yuya on 2017/03/09.
 */

public enum ImageManager {
    INSTANCE;
    private ArrayList<Mat> mat = new ArrayList<Mat>();
    private int id;

    public void clearId() {
        this.id = 0;
    }

    public void setMat(final Mat mat) {
        try {
            this.mat.get(this.id);
            this.mat.set(this.id, mat);
        } catch (Exception e) {
            this.mat.add(this.id, mat);
        }
        this.id++;
    }

    public void saveImage() {
        try {
            // 日付を取得
            Date mDate = new Date();
            SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");

            for (int i = 0; i < this.mat.size(); i++) {
                Bitmap bmp = Bitmap.createBitmap(this.mat.get(i).width(), this.mat.get(i).height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(this.mat.get(i), bmp);

                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                // 保存処理開始
                //FileOutputStream fos = new FileOutputStream(new File(root, fileName.format(mDate) + "_bmp" +  i + ".jpg"));
                FileOutputStream fos = new FileOutputStream(new File(root, "Image" +  i + ".jpg"));

                // jpegで保存
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (Exception e) {
            Log.e("Error", "" + e.toString());
        }
    }
}
