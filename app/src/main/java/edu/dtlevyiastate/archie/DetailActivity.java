package edu.dtlevyiastate.archie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DetailActivity extends ActionBarActivity {
    Firebase taskRef = new Firebase("https://archieproject.firebaseio.com/users/");
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    public static ArrayAdapter<CommentItem> userDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle(MainActivity.curProject + "/" + MainActivity.curModule + "/" + getIntent().getStringExtra("task").toString());

        TextView titleText = (TextView) findViewById(R.id.taskName);
        TextView descText = (TextView) findViewById(R.id.taskDesc);
        ImageView image = (ImageView) findViewById(R.id.statusImage);

        RowItem thisTask = MainActivity.getTaskListAdapter().getItem(this.getIntent().getIntExtra("pos", 0));

        titleText.setText(thisTask.getTitle());
        descText.setText(thisTask.getDesc());
        image.setImageResource(thisTask.getImageId());

        userDataAdapter =
                new CommentAdapter(
                        this, // The current context (this activity)
                        new ArrayList<CommentItem>());

        final ListView commentView = (ListView) findViewById(R.id.commentListView);

       taskRef =new Firebase("https://archieproject.firebaseio.com/users/"+ MainActivity.curUserId +
                "/projects/" + MainActivity.curProject +
                "/modules/" + MainActivity.curModule +
                "/tasks/" + this.getIntent().getStringExtra("task") +
                "/comments/");


        taskRef.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot snapshot)  {
                //Log.v(LOG_TAG, snapshot.getValue().toString());
                Log.v(LOG_TAG, "Checking authdata");
                AuthData authData = taskRef.getAuth();
                if (authData != null) {

                   Log.v(LOG_TAG, "Updating comment list");
                    if (snapshot != null) {
                        userDataAdapter.clear();
                        for (DataSnapshot comment: snapshot.getChildren() ) {
                            if (comment.child("Comment").getValue() != null) {
                                Log.v(LOG_TAG,comment.getValue().toString());
                                Log.v(LOG_TAG, comment.child("Comment").getValue().toString());
                                //Get Project Details from Firebase JSON
                                String com = comment.child("Comment").getValue().toString();
                                String posted = comment.child("Posted").getValue().toString();
                                String poster = " - " + comment.child("Poster").getValue().toString();
                                //Log.v(LOG_TAG )
                                //Create RowItem for ArrayList
                                CommentItem newComment = new CommentItem(com, posted, poster);

                                //Add RowItem to the adapter arraylist
                                userDataAdapter.add(newComment);
                            }

                        }
                        commentView.setAdapter(userDataAdapter);
                    }



                } else {
                    // no user authenticated with Firebase
                    Log.v(LOG_TAG, "no user auth");
                }


            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed - you are dumb: " + firebaseError.getMessage());
            }
        });



        ((EditText)findViewById(R.id.userComment)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.action_add_comment || actionId == EditorInfo.IME_ACTION_DONE) {
                        addComment();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void addComment() {
        new AlertDialog.Builder(this)
                .setMessage("Add this comment to the task?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText commentText = ((EditText) findViewById(R.id.userComment));
                        Date now = new Date();
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy '@' hh:mm:ss a");

                        //Get new user info
                        final String first = commentText.getText().toString().trim();
                        Map<String,Object> comment = new HashMap<String, Object>();
                        comment.put("Comment", first);
                        comment.put("Posted", dateFormatter.format(now));
                        comment.put("Poster", MainActivity.curName);

                        //Firebase taskRef =new Firebase("https://archieproject.firebaseio.com/users/"+ MainActivity.curUserId + "/projects/" + MainActivity.curProject + "/modules/" + MainActivity.curModule + "/tasks/" + this.getIntent().getStringExtra("task") + "/comments/");
                        taskRef.push().setValue(comment);
                        commentText.getEditableText().clear();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                //don't finish
                dialog.dismiss();

            }
        })
                .show();

    }

}
