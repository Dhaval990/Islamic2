package com.islamicbookslibrary.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.islamicbookslibrary.BaseFragment;
import com.islamicbookslibrary.PostViewHolder;
import com.islamicbookslibrary.R;
import com.islamicbookslibrary.model.Product;
import com.islamicbookslibrary.util.Logg;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksListFragment extends BaseFragment {
    public static final String TAG = BooksListFragment.class.getSimpleName();


    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    // RecyclerView Component
    private FirebaseRecyclerAdapter<Product, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private boolean isDBExist;

    public BooksListFragment() {
        setContentView(R.layout.fragment_books_list);
    }

    @Override
    public void initVariable() {

        if (!isDBExist) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isDBExist = true;
        }

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

    }

    @Override
    public void findViews() {
        mRecycler = (RecyclerView) links(R.id.book_list);
    }

    @Override
    public void postInitView() {
// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client.connect();
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(mContext);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
    }

    @Override
    public void loadData() {
        showProgressDialog();

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Product, PostViewHolder>(Product.class, R.layout.row_user_list, PostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Product model, final int position) {
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                hideProgressDialog();

                // Set click listener for the whole post view
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        /*Intent intent = new Intent(UserListActivity.this, UserDetailActivity.class);
                        intent.putExtra(UserDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);*/
                    }
                });

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("products").child(postRef.getKey());
//                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
//                        onStarClicked(userPostRef);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_like_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_like_outline_24);
                }

                // Determine if the current user has liked this post and set UI accordingly
                if (model.reads.containsKey(getUid())) {
                    viewHolder.readView.setImageResource(R.drawable.ic_read_count);
                } else {
                    viewHolder.readView.setImageResource(R.drawable.ic_not_read);
                }
            }
        };
        mAdapter.getItemCount();

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void addAdapter() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Product p = mutableData.getValue(Product.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Logg.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    // Query for get the data
    private Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("products");
    }

}
