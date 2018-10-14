package tstapp.manee.com.tstapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tstapp.manee.com.tstapp.storage.DBHelper;

public class UsersDetails extends AppCompatActivity {

    private Intent usersIntent;
    private DBHelper dbHelper;
    private TextView name, username, email, address, phone, website, company_name, catchPhrase, company_bs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_details);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address_details);
        phone = findViewById(R.id.phone);
        website = findViewById(R.id.website);
        company_name = findViewById(R.id.company_name);
        catchPhrase = findViewById(R.id.company_catchPhrase);
        company_bs = findViewById(R.id.company_bs);

        usersIntent = getIntent();
        Log.d("Extras", usersIntent.getStringExtra("_ID"));
        getUserData(usersIntent.getStringExtra("_ID"));
    }

    /*
     *   Fetch Note record from SQLite database
     */
    private void getUserData(String userRcdID) {
        // Open database and get data from there
        dbHelper = new DBHelper(getApplicationContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DBHelper.UsersTables._ID,
                DBHelper.UsersTables.COLUMN_NAME_USERS
        };

        Cursor cursor = db.query(
                DBHelper.UsersTables.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        String userData = null;

        while(cursor.moveToNext()) {
            Log.d("_ID", cursor.getString(0));
            Log.d("USER_DATA", cursor.getString(1));

            userData = cursor.getString(1);
        }

        cursor.close();

        showUserDetails(userData);
    }

    private void showUserDetails(String data) {

        try {

            int userID = Integer.parseInt(usersIntent.getStringExtra("_ID"));
            userID = userID - 1;
            JSONArray users = new JSONArray(data);
            JSONObject user = users.getJSONObject(userID);

            name.setText(user.getString("name"));
            username.setText("User name: " + user.getString("username"));
            email.setText("Email: " + user.getString("email"));
            phone.setText("Phone: " + user.getString("phone"));

            JSONObject addressObj = new JSONObject(user.getString("address"));
            address.setText(addressObj.get("street") + " " + addressObj.get("city") + " ");

            website.setText("Website: " + user.getString("website"));

            JSONObject company = new JSONObject(user.getString("company"));
            company_name.setText(company.getString("name"));
            catchPhrase.setText("Catch Phrase: " + company.getString("catchPhrase"));
            company_bs.setText("BS: " + company.getString("bs"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
