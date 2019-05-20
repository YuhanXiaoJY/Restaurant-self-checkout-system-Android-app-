package com.example.javaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    private Dish[] dishes = {
            new Dish("心相印", R.drawable.dish_1), new Dish("长脚蟹", R.drawable.dish_2),
            new Dish("三文鱼千层", R.drawable.dish_3), new Dish("希鲮鱼", R.drawable.dish_3),
            new Dish("玉子三文鱼", R.drawable.dish_5), new Dish("黄金三文鱼", R.drawable.dish_6),
            new Dish("蟹肉芒果千层", R.drawable.dish_7), new Dish("荣螺", R.drawable.dish_8),
            new Dish("北极贝", R.drawable.dish_9), new Dish("樱花卷", R.drawable.dish_10),
            new Dish("加州卷", R.drawable.dish_11), new Dish("蟹柳扇贝卷", R.drawable.dish_12),
            new Dish("玻璃虾刺身", R.drawable.dish_13), new Dish("希鲮鱼刺身", R.drawable.dish_14),
            new Dish("北极贝王刺身", R.drawable.dish_15), new Dish("飞鱼子手卷", R.drawable.dish_16)
    };
    private List<Dish> dishList = new ArrayList<>();
    private DishAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //CardView and RecyclerView
        initDishes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);   //2 grids
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DishAdapter(dishList);
        recyclerView.setAdapter(adapter);

        //swipeRefresh
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDishes();
            }
        });
    }

    private void refreshDishes()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initDishes();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initDishes()
    {
        dishList.clear();
        for(int i = 0; i < 14; i++)     //select 20 images at random to display.
        {
            Random random = new Random();
            int index = random.nextInt(dishes.length);
            dishList.add(dishes[index]);
        }
    }

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
                startQrCode();
                break;
            case R.id.homepage:
                Toast.makeText(this, "This is homepage!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.shopping:
                Intent intent_shopping = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(intent_shopping);
                break;
            case R.id.comment:
                Intent intent_comment = new Intent(MainActivity.this, CommentActivity.class);
                intent_comment.putExtra("Account", account);
                startActivity(intent_comment);
                break;
            case R.id.toolbar_log_out:
                Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent3);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            Toast.makeText(MainActivity.this, scanResult, Toast.LENGTH_LONG).show();
//            Intent intent =new Intent(Intent.ACTION_VIEW);
//            Uri uri = Uri.parse(scanResult);
//            intent.setData(uri);
//            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(MainActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(MainActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
            }
            // 申请权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }

        // 二维码扫码
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

}
