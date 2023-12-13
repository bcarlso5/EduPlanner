package com.example.eduplanner.adapter;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is the main class to hold the firestore applications that will be used in a recyclerview
 * @param <VH> ViewHolder for the recyclerView
 */
public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements EventListener<QuerySnapshot> {

    private static final String TAG = "Firestore Adapter";

    private Query mQuery;
    private ListenerRegistration mRegistration;

    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    /**
     *  Constructor for the firestore to store all of the data
     * @param query
     */
    public FirestoreAdapter(Query query) {
        mQuery = query;
    }

    /**
     * Function to allow the firestore to respond to any updates within it
     */
    public void startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    /**
     * Function to no longer read updates from firestore
     */
    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        mSnapshots.clear();
        notifyDataSetChanged();
    }

    /**
     * Function to change the query
     * @param query query with any perameters needed changing
     */
    public void setQuery(Query query) {
        // Stop listening
        stopListening();

        // Clear existing data
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }

    /**
     *  Setting event function that will record values on the listener
     * @param documentSnapshots Any documents needed to see updates
     * @param e Any error returns
     */
    @Override
    public void onEvent(QuerySnapshot documentSnapshots,
                        FirebaseFirestoreException e) {
        // Handle errors
        if (e != null) {
            Log.w(TAG, "onEvent:error", e);
            return;
        }
        // Dispatch the event
        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
            // Snapshot of the changed document
            DocumentSnapshot snapshot = change.getDocument();
            switch (change.getType()) {
                case ADDED:
                    onDocumentAdded(change);
                    break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
        }
        onDataChanged();
    }

    /**
     * Add document function that will add it to the snapshot
     * @param change Value for the document
     */
    protected void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    /**
     * Function to adjust  changes to a certain document
     * @param change the document change
     */
    protected void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
            // Item changed but remained in same position
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        }
        else {
            // Item changed and changed position
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    /**
     * Function to remove any documents from the snapshot
     * @param change document to remove
     */
    protected void onDocumentRemoved(DocumentChange change) {
        mSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());
    }

    /**
     * Return size of the snapshot (values within the database)
     * @return
     */
    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    /**
     * Get the database values at a particular index
     * @param index The index of the database
     * @return
     */
    protected DocumentSnapshot getSnapshot(int index) {
        return mSnapshots.get(index);
    }

    /**
     * Error function to record any values that withhold an error
     * @param e error
     */
    protected void onError(FirebaseFirestoreException e) {};

    /**
     * Function to record any data changes
     */
    protected void onDataChanged() {}
}
