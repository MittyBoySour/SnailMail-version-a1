// SNAILMAIL SPARE CODE

// scroll to bottom for recyclerview 

mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);

        int mailMessageCount = mFirebaseAdapter.getItemCount();
        int lastVisiblePosition =
                mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        // If the recycler view is initially being loaded or the
        // user is at the bottom of the list, scroll to the bottom
        // of the list to show the newly added message.
        if (lastVisiblePosition == -1 ||
                (positionStart >= (mailMessageCount - 1) &&
                        lastVisiblePosition == (positionStart - 1))) {
            mMailRecyclerView.scrollToPosition(positionStart);
        }

    }
});


// dateTime to firebase

public void storeDatetoFirebase() {

    handler = new Handler();

    runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);
            try {
                Date date = new Date();
                Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                String stringdate = dt.format(newDate);

                System.out.println("Submission Date: " + stringdate);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("My_Date");
                databaseReference.child("init_date").setValue(stringdate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    handler.postDelayed(runnable, 1 * 1000);
}
