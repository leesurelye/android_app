package guo.john.com.socketsenddata;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by yue on 2020/5/26.
 * @author guo.john.com
 */
public class Camera  extends AppCompatActivity {

        public static final int TAKE_PHOTO = 1;
        private ImageView picture;
        private Uri imageUri;
        private Context mContext;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_camera);
            Button takePhoto = (Button) findViewById(R.id.btn_take_photo);
            picture = (ImageView) findViewById(R.id.picture);
            mContext = Camera.this;


            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 创建一个File对象，用于保存摄像头拍下的图片，这里把图片命名为output_image.jpg
                    // 并将它存放在手机SD卡的应用关联缓存目录下
                    File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

                    // 对照片的更换设置
                    try {
                        // 如果上一次的照片存在，就删除
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        // 创建一个新的文件
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 如果Android版本大于等于7.0
                    if (Build.VERSION.SDK_INT >= 24) {
                        // 将File对象转换成一个封装过的Uri对象
                        imageUri = FileProvider.getUriForFile(Camera.this, "com.example.lenovo.cameraalbumtest.fileprovider", outputImage);
                    } else {
                        // 将File对象转换为Uri对象，这个Uri标识着output_image.jpg这张图片的本地真实路径
                        imageUri = Uri.fromFile(outputImage);
                    }

                    // 动态申请权限
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 100);
                    } else {
                        // 启动相机程序
                        startCamera();
                    }

                }
            });

        }

        private void startCamera() {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            // 指定图片的输出地址为imageUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }


        // 使用startActivityForResult()方法开启Intent的回调
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    if (requestCode == RESULT_OK) {
                        try {
                            // 将图片解析成Bitmap对象
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            // 将图片显示出来
                            picture.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
            }
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case 100:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // 启动相机程序
                        startCamera();
                    } else {
                        Toast.makeText(mContext, "没有权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }
}
