package com.example.javaproject;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingCartActivity extends AppCompatActivity {
    static {
        if(!OpenCVLoader.initDebug())
        {
            Log.d("opencv","初始化失败");
        }
    }

    private DrawerLayout mDrawerLayout;
    private DishAdapter adapter;
    private List<Dish> DishList = new ArrayList<>();
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    private Uri imageUri;
    private static final String TAG = "ShoppingCartActivity";
    private Button Pay;
    private TextView textView;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Intent intent = getIntent();
        account = intent.getStringExtra("Account");

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navigation bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_name);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();           // Do nothing here. We can add sth later.
                return true;
            }
        });

        Button takePhoto = (Button) findViewById(R.id.take_photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);

        //card view
        picture = (ImageView) findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a File object to save the photos
                String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/JavaProject/photo_image";
                if(!new File(dir).exists()) {
                    if(!new File(dir).mkdirs())
                        Log.d(TAG, "dir: cannot create dir");
                    Log.d(TAG, "dir: "+dir);
                }
                //get the state of external storage
                String state = Environment.getExternalStorageState();
                //if the state != MOUNTED, we cannot read or write any file
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                //create image file
                File outputImage = new File(dir + "/output_image.jpg");
                Log.d(TAG, "path: "+ dir+"/output_image.jpg");
                try{
                    if(outputImage.exists())
                        outputImage.delete();
                    outputImage.createNewFile();
                }
                catch (IOException e)
                {
                    Log.d(TAG, "dir: Fail to create image file!");
                    e.printStackTrace();
                }

                //if VERSION >= Android7.0, using the real path of any file will be regarded as unsafe
                if(Build.VERSION.SDK_INT >= 24)
                    imageUri = FileProvider.getUriForFile(ShoppingCartActivity.this, "com.example.cameraalbum.fileprovider", outputImage);
                else
                    imageUri = Uri.fromFile(outputImage);

                //start the camera program
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        //choose from album
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ShoppingCartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(ShoppingCartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                else
                    openAlbum();
            }
        });

        //pay
        Pay = (Button) findViewById(R.id.pay);
        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //知道要跳转应用的包名
                ShoppingCartActivity.this.startActivity(ShoppingCartActivity.this.getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
            }
        });
    }

    //create the top menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Yummy is so handful! Come to download it!");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                break;
            case R.id.scan:
                Toast.makeText(this, "You clicked scan", Toast.LENGTH_SHORT).show();
                break;
            case R.id.homepage:
                Intent intent = new Intent(ShoppingCartActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.shopping:
                Toast.makeText(this, "This is shopping cart", Toast.LENGTH_SHORT).show();
                break;
            case R.id.comment:
                Intent intent_comment = new Intent(ShoppingCartActivity.this, CommentActivity.class);
                intent_comment.putExtra("Account", account);
                startActivity(intent_comment);
                break;
            case R.id.toolbar_log_out:
                Intent intent3 = new Intent(ShoppingCartActivity.this, LoginActivity.class);
                startActivity(intent3);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch(requestCode)
        {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openAlbum();
                else
                    Toast.makeText(this, "The permission to writing external storage has been denied", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "onActivityResult: enter onActivityResult!");
        switch(requestCode)
        {
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK)
                {
                    try
                    {
                        Log.d(TAG, "onActivityResult: set image successfully!");
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }
                    catch(FileNotFoundException e)
                    {
                        Log.d(TAG, "onActivityResult: cannot display image!");
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK)
                {
                    //judge the Android version
                    if(Build.VERSION.SDK_INT >= 19)
                        handleImageOnKitKat(data);  //Android version >= 4.4
                    else
                        handleImageBeforeKitKat(data);  //Android version < 4.4
                }
                break;
            default:
                Log.d(TAG, "onActivityResult: requestCode != OK");
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data)
    {
        String imagePath = null;
        Uri uri = data.getData();
        //if the uri is of type "document", process it by document id.
        if(DocumentsContract.isDocumentUri(this, uri))
        {
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id = docId.split(":")[1];    //get the id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        else if("content".equalsIgnoreCase(uri.getScheme()))
            imagePath = getImagePath(uri, null);
        else if("file".equalsIgnoreCase(uri.getScheme()))
            imagePath = uri.getPath();

        displayImage(imagePath);
        Recognize recognize = new Recognize();
        int[] plate_num = recognize.RecognizeRun(imagePath);
        int prices[] = {8, 10, 12, 16, 18, 20, 24};
        int price  = 0;
        for(int i = 0; i< 7; i++)
            price += prices[i] * plate_num[i];
        String pay_str = String.format("Pay now! ￥%d.0", price);
        Pay.setText(pay_str);

        String plate_info = String.format("Blue(￥8): %d       Green(￥10): %d       Purple(￥12): %d\nYellow(￥16): %d       Orange(￥18): %d       Red(￥20): %d       Coffee(￥24): %d",
                plate_num[0], plate_num[1], plate_num[2], plate_num[3], plate_num[4], plate_num[5], plate_num[6]);
        textView = (TextView) findViewById(R.id.plate_intro1);
        textView.setText(plate_info);
    }

    private void handleImageBeforeKitKat(Intent data)
    {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath)
    {
        if(imagePath != null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }
        else
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
    }
}
