package tisserand.alexey.justquiz.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.adapters.RecordAdapter;
import tisserand.alexey.justquiz.utilities.ActionBar;


public class RecordActivity extends ActionBar {
    private ImageButton btn_discharge;
    private RecordAdapter recordAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initToolbar();

        recordAdapter = new RecordAdapter (this);
        initRecord();
        addListenerOnButton();
        }


    private void initRecord(){

        ArrayList<String> arrayRecords = recordAdapter.getArrayRecord();
        //ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, arrayRecords);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, arrayRecords);
        ListView lv = findViewById(R.id.records);
        lv.setAdapter(mAdapter);
    }

    public void addListenerOnButton() {
        btn_discharge = findViewById(R.id.btn_discharge);
        btn_discharge.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertBuilder(R.string.record_clear, R.string.close_title);
                    }
                }
        );
    }
    public void AlertBuilder(int mess, final int title) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(RecordActivity.this);
        a_builder.setMessage(mess)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recordAdapter.clearRecord();
                        initRecord();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle(title);
        alert.show();
    }
}