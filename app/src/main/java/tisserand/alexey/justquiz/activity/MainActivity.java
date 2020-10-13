package tisserand.alexey.justquiz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.adapters.CategoryAdapter;
import tisserand.alexey.justquiz.constants.AppConstants;
import tisserand.alexey.justquiz.listeners.ListItemClickListener;
import tisserand.alexey.justquiz.models.quiz.CategoryModel;
import tisserand.alexey.justquiz.utilities.ActionBar;
import tisserand.alexey.justquiz.utilities.ActivityUtilities;


public class MainActivity extends ActionBar {

    private Activity activity;
    private Context context;
    private ImageButton btn_info;

    private ArrayList<CategoryModel> categoryList;
    private CategoryAdapter adapter = null;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        activity = MainActivity.this;
        context = getApplicationContext();



        recyclerView = findViewById(R.id.rvContent);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            recyclerView.setLayoutManager(new GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false));

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(context, categoryList);
        recyclerView.setAdapter(adapter);

        loadJson();
        initListener();
    }



    private void loadJson() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstants.CONTENT_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parseJson(sb.toString());
    }

    private void parseJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray(AppConstants.JSON_KEY_ITEMS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String categoryId = object.getString(AppConstants.JSON_KEY_CATEGORY_ID);
                String categoryName = object.getString(AppConstants.JSON_KEY_CATEGORY_NAME);

                categoryList.add(new CategoryModel(categoryId, categoryName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    public void AlertBuilder(int mess) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(activity);
        a_builder.setMessage(mess)
                .setCancelable(false)
                .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }

    private void initListener() {
        btn_info = findViewById(R.id.btn_info);
        btn_info.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertBuilder(R.string.info_categories);
                    }
                });

        adapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                CategoryModel model = categoryList.get(position);

                ActivityUtilities.getInstance().invokeCommonQuizActivity(activity, QuizActivity.class, model.getCategoryId(),  true);
            }
        });
    }
}