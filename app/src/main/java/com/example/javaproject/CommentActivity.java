package com.example.javaproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        final String account = intent.getStringExtra("Account");

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

        initMsg();
        inputText = (EditText) findViewById(R.id.comment_input_text);
        send = (Button) findViewById(R.id.comment_send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content))
                {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);

                    SharedPreferences pref = getSharedPreferences("comment", MODE_PRIVATE);
                    String str = pref.getString("Remarks", "");
                    SharedPreferences.Editor editor = getSharedPreferences("comment", MODE_PRIVATE).edit();
                    String newStr = str + "_" + account + ": " + content;
                    editor.putString("Remarks", newStr);
                    editor.apply();
                    adapter.notifyItemInserted(msgList.size() - 1); //new msg
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");  //clear the input text
                }
            }
        });
    }

    private void initMsg()
    {
//        SharedPreferences.Editor editor = getSharedPreferences("comment", MODE_PRIVATE).edit();
//        editor.putString("Remarks", "Xiao: The app is very handful!_Lu: I love it!_Mai: I have spent every coin I have on dishes since I downloaded this app ...");
//        editor.apply();

        SharedPreferences pref = getSharedPreferences("comment", MODE_PRIVATE);
        String[] strs = pref.getString("Remarks", "").split("_");
        int len = strs.length;
        for(int i = 0; i< len; i++)
        {
            Msg msg = new Msg(strs[i], Msg.TYPE_RECEIVED);
            msgList.add(msg);
        }
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
                Intent intent = new Intent(CommentActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.shopping:
                Intent intent_shopping = new Intent(CommentActivity.this, ShoppingCartActivity.class);
                startActivity(intent_shopping);
                break;
            case R.id.comment:
                Toast.makeText(this, "You are making comments now!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_log_out:
                Intent intent3 = new Intent(CommentActivity.this, LoginActivity.class);
                startActivity(intent3);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
}
