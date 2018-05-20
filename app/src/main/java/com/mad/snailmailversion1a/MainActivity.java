package com.mad.snailmailversion1a;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    public static class SnailMailViewHolder extends RecyclerView.ViewHolder {
        TextView mailTitleTv;
        TextView mailSenderNameTv;

        public SnailMailViewHolder(View v) {
            super(v);
            mailTitleTv = (TextView) itemView.findViewById(R.id.mail_title);
            mailSenderNameTv = (TextView) itemView.findViewById(R.id.sender_name);
        }
    }

    private static final String USER_FILTER = "user";
    private static final String USER_MAIL_FILTER = "mail";
    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private String mUsername;

    // UI elements
    private ProgressBar mProgressBar;
    private RecyclerView mMailRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    //private StorageReference mStorageRef;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<SnailMail, SnailMailViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = ANONYMOUS;
        //mStorageRef = FirebaseStorage.getInstance().getReference();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return; // check this
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            // TODO: add photoUrl
        }


        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mMailRecyclerView = (RecyclerView) findViewById(R.id.mailRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        //mLinearLayoutManager.setStackFromEnd(true);
        mMailRecyclerView.setLayoutManager(mLinearLayoutManager);
        
        // ***** RecyclerView START *****

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<SnailMail> parser = new SnapshotParser<SnailMail>() {
            @Override
            public SnailMail parseSnapshot(DataSnapshot dataSnapshot) {
                SnailMail snailMail = dataSnapshot.getValue(SnailMail.class);
                if (snailMail != null) {
                    snailMail.setId(dataSnapshot.getKey());
                }
                return snailMail;
            }
        };

        // get all mail for specific user (consider using account over getUid)
        DatabaseReference userMailQuery = mFirebaseDatabaseReference.child(USER_FILTER).child(mFirebaseUser.getUid()).child(USER_MAIL_FILTER);
        Log.d(TAG, "onCreate: query: " + userMailQuery.toString() + ", parser: " + parser.toString());
        FirebaseRecyclerOptions<SnailMail> options =
                new FirebaseRecyclerOptions.Builder<SnailMail>()
                        .setQuery(userMailQuery, parser)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<SnailMail, SnailMailViewHolder>(options) {
            @Override
            public SnailMailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SnailMailViewHolder(inflater.inflate(R.layout.snail_mail_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final SnailMailViewHolder viewHolder,
                                            int position,
                                            SnailMail snailMail) {
                viewHolder.mailTitleTv.setText(snailMail.getTitle());
                viewHolder.mailSenderNameTv.setText(snailMail.getSenderName());
                Log.d(TAG, "onBindViewHolder: title: " + snailMail.getTitle() + ", sender name: " + snailMail.getSenderName());
            }
        };

        mMailRecyclerView.setAdapter(mFirebaseAdapter);
        
        // ***** RecyclerView END *****

        FloatingActionButton composeMailFAB = (FloatingActionButton) findViewById(R.id.fab);
        composeMailFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
