package com.feedapp.callback;

import com.feedapp.db.entities.Feed;

public interface OnItemClickListener {
    void onItemClick(Feed feed);
}
