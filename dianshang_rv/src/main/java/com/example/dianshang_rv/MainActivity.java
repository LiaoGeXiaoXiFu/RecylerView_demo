package com.example.dianshang_rv;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private  List<ReaBean.DataBean> list;
    private HomeAdapter homeAdapter;

    private Button addItem;
    private Button removeItem;
    private Button change_listView;
    private Button change_gridView;
    private Button change_waterfall;
    private String path="https://www.zhaoapi.cn/product/getCatagory";
    private boolean isFirstView = true;


    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(  ){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            List<ReaBean.DataBean> list = (List<ReaBean.DataBean>) msg.obj;
            homeAdapter = new HomeAdapter(MainActivity.this, list);
            recyclerView.setAdapter(homeAdapter);
            homeAdapter.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(MainActivity.this, "点击了Item" + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(View view, final int position) {
                    Toast.makeText(MainActivity.this, "长按了Item" + position, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("提示：");
                    builder.setMessage("你确定要丢弃美女吗?");
                    builder.setIcon(R.drawable.hhh);
                    //点击对话框以外的区域是否让对话框消失
                    builder.setCancelable(true);
                    //设置正面按钮
                    builder.setPositiveButton("不是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "你点击了不是", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    //设置反面按钮
                    builder.setNegativeButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "你点击了是的", Toast.LENGTH_SHORT).show();

                            homeAdapter.removeItem(position);
                            dialog.dismiss();
                        }
                    });
                    //设置中立按钮
                    builder.setNeutralButton("保密", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "你选择了保密", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    //显示对话框
                    dialog.show();
                }

                @Override
                public void onItemSubViewClick(View view, int position) {

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        initData();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_demo);
        //设置动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));


    }

    public void findViewById() {
        addItem = (Button) findViewById(R.id.addItem);
        removeItem = (Button) findViewById(R.id.removeItem);
        change_listView = (Button) findViewById(R.id.change_listView);
        change_gridView = (Button) findViewById(R.id.change_gridView);
        change_waterfall = (Button) findViewById(R.id.change_waterfall);
        //点击事件
        addItem.setOnClickListener(this);
        removeItem.setOnClickListener(this);
        change_listView.setOnClickListener(this);
        change_gridView.setOnClickListener(this);
        change_waterfall.setOnClickListener(this);
    }

    private void initData() {
        //请求网络
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url( path ).build();
        Call call = okHttpClient.newCall( request );
        call.enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("e","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                //解析
                Gson gson = new Gson();
                ReaBean reaBean = gson.fromJson( string, ReaBean.class );
                List<ReaBean.DataBean> data = reaBean.getData();
                //发送给handler
                Message obtain = Message.obtain();
                obtain.obj=data;
                handler.sendMessage( obtain );
            }
        } );
    }
    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addItem:
                homeAdapter.addItem(1);
                break;
            case R.id.removeItem:
                homeAdapter.removeItem(1);
                break;
            case R.id.change_listView:
                /**
                 * ListView的效果
                 */
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                if (isFirstView) {
                    isFirstView = false;
                    onClick(findViewById(R.id.recyclerView_demo));
                }
                break;
            case R.id.change_gridView:
                /**
                 * GridView的效果
                 */
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case R.id.change_waterfall:
                /**
                 * 瀑布流的效果
                 */
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                break;
            default:
                break;
        }
    }
}