package com.example.aashankpratap.memoapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

import MemoDatabase.DBHelperActivity;

public class MemoUI extends AppCompatActivity {

    ListView listView;
    EditText memoETCreateTag, memoETCreateContent, memoETSearchTag ;

    ArrayList<String> arrayList ;
    ArrayAdapter arrayAdapter;
    DBHelperActivity myDB ;

    CoordinatorLayout coordinatorLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_ui);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new DBHelperActivity(this);
        arrayList = new ArrayList<String>();
        arrayList = myDB.getAllMemo();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listView = (ListView) findViewById(R.id.memo_tags);
        listView.setAdapter(arrayAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder memoList = new AlertDialog.Builder(MemoUI.this);

                memoList.setTitle(R.string.create_memo_title);
                memoList.setMessage(R.string.create_memo_description);

                LayoutInflater inflater = getLayoutInflater();
                View dialogCreateView = inflater.inflate(R.layout.custom_list_view01,null);

                memoList.setView(dialogCreateView);

                memoETCreateTag = (EditText) dialogCreateView.findViewById(R.id.memoTag);
                memoETCreateContent = (EditText) dialogCreateView.findViewById(R.id.memoContent);

                memoList.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        String Tag = memoETCreateTag.getText().toString();
                        String Content = memoETCreateContent.getText().toString();
                        myDB.insertMemo(Tag, Content);
                        refreshMemoListView();
                    }
                });
                memoList.setNegativeButton(R.string.cancel, null);
                memoList.create().show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMemoListView();
    }

    private void refreshMemoListView() {
        arrayList = myDB.getAllMemo();
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayList);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memo_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_action) {

            AlertDialog.Builder memoSearch = new AlertDialog.Builder(this);
            memoSearch.setTitle(R.string.search_memo_title);
            memoSearch.setMessage(R.string.search_memo_description);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogSearchView = inflater.inflate(R.layout.custom_list_view02, null);
            memoSearch.setView(dialogSearchView);
            memoETSearchTag = (EditText) dialogSearchView.findViewById(R.id.memoSearchTag);

            memoSearch.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            String Tag = memoETSearchTag.getText().toString();
                            Cursor res = myDB.getData(Tag);
                            if (res != null && res.moveToFirst()) {
                                String Content = res.getString(res
                                        .getColumnIndex(DBHelperActivity.MEMO_COLUMN_CONTENT));
                                Snackbar.make(coordinatorLayout,"Searched Tag : "+Tag+" contains content : "+Content,Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            } else {
                                Snackbar.make(coordinatorLayout,"Searched Tag does not exists!!",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }

                            if (!res.isClosed()) {
                                res.close();
                            }
                        }
                    });
            memoSearch.setNegativeButton(R.string.cancel, null);
            memoSearch.create().show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
