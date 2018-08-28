package com.rentee.expandtextview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String mDemoStr = "我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长我很长";

    private ExpandRvAdapter mExpandRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvExpandList = findViewById(R.id.rv_expand_list);
        rvExpandList.setLayoutManager(new LinearLayoutManager(this));
        mExpandRvAdapter = new ExpandRvAdapter();
        rvExpandList.setAdapter(mExpandRvAdapter);
        initData();
    }

    private void initData() {
        List<ExpandModel> expandModels = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            ExpandModel model = new ExpandModel();
            model.setTxt(mDemoStr.substring(new Random().nextInt(mDemoStr.length())));
            expandModels.add(model);
        }
        mExpandRvAdapter.setData(expandModels);
    }
}
