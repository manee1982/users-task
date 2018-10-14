package tstapp.manee.com.tstapp.utils;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tstapp.manee.com.tstapp.R;

/**
 * Created by manee on 17/08/17.
 */

public class UsersArrayAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;
    private TextView usersID;
    private TextView name;
    private TextView username;

    public UsersArrayAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
//        Log.d("User Count->getCount()", "" + User._noteID.size());
        return User._noteID.size();
    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View customListView = inflater.inflate(R.layout.custom_list, viewGroup, false);

        if(User._noteID != null) {

            usersID = customListView.findViewById(R.id.userNumber);
            usersID.setText(User._noteID.get(i));

            name = customListView.findViewById(R.id.noteTitle);
            name.setText(User.name.get(i));

            username = customListView.findViewById(R.id.noteDateTime);
            username.setText(User.username.get(i));
        }
        return customListView;
    }
}