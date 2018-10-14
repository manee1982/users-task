package tstapp.manee.com.tstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tstapp.manee.com.tstapp.storage.DBHelper;
import tstapp.manee.com.tstapp.utils.User;
import tstapp.manee.com.tstapp.utils.UsersArrayAdapter;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper = null;
    private ListView userListView;
    private UsersArrayAdapter usersArrayAdapter;
    private TextView _userID, wait;
    private MainActivity mainActivity = this;
    private String USERS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userListView = findViewById(R.id.users);
        wait = findViewById(R.id.wait);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                _userID = view.findViewById(R.id.userNumber);
//                Toast.makeText(getApplicationContext(), _userID.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UsersDetails.class);
                intent.putExtra("_ID", _userID.getText());
//                intent.putExtra("_DATA", USERS);
                startActivity(intent);
            }
        });

        if(getApplicationUsersFromDB() != null && !getApplicationUsersFromDB().isEmpty()) {
            String data = getApplicationUsersFromDB();
            Log.d("DATA_FROM_DB", data);

            this.USERS = data;
            populateListView(data);

        } else {
            getUsersDataFromApi(mainActivity);
        }
    }

    private void getUsersDataFromApi(final MainActivity mainActivity) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://jsonplaceholder.typicode.com/users";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DATA", response);

                        // Save response into DB
                        DBHelper myNoteDBHelper = new DBHelper(mainActivity);
                        // Gets the USERS repository in write mode
                        SQLiteDatabase db = myNoteDBHelper.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(DBHelper.UsersTables.COLUMN_NAME_USERS, response);

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = db.insert(DBHelper.UsersTables.TABLE_NAME, null, values);


                        userListView.setVisibility(View.VISIBLE);
                        wait.setVisibility(View.GONE);

                        mainActivity.populateListView(response);
                        mainActivity.USERS = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HTTP_REQUEST_FAILED", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /*
     *   Fetch Notes from SQLite database
     */
    private String getApplicationUsersFromDB() {
        // Open database and get USERS from there
        dbHelper = new DBHelper(getApplicationContext());
        // Gets the USERS repository in write mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DBHelper.UsersTables._ID,
                DBHelper.UsersTables.COLUMN_NAME_USERS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DBHelper.UsersTables._ID;

        Cursor cursor = db.query(
                DBHelper.UsersTables.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        String usersData = null;
        if(usersData != null) {
            usersData = null;
        }

        while(cursor.moveToNext()) {
//            Log.d("ID from database ", cursor.getString(0));
//            Log.d("Title from database ", cursor.getString(1));
//            Log.d("Date from database ", cursor.getString(2));
            usersData = cursor.getString(1);
        }

        cursor.close();

        userListView.setVisibility(View.VISIBLE);
        wait.setVisibility(View.GONE);

        return usersData;
    }

    private void populateListView(String data) {
        try {
            JSONArray users = new JSONArray(data);

            // Clear array lists
            if(User._noteID != null) {
                User._noteID.clear();
                User.name.clear();
                User.username.clear();
            }

            // loop through json array to get user USERS
            for (int i=0; i< users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                User._noteID.add(user.getString("id"));
                User.name.add(user.getString("name"));
                User.username.add(user.getString("username"));
            }

            /*
             *   Get application USERS and populate it
             *   to the listView
             */
            usersArrayAdapter = new UsersArrayAdapter(mainActivity);
            userListView.setAdapter(usersArrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
