package com.feedapp.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feedapp.R;
import com.feedapp.callback.OnItemClickListener;
import com.feedapp.db.entities.Feed;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;
import java.util.Objects;

public class FeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<FeedListAdapter.HeaderViewHolder> {


    private List<Feed> mFeeds;
    private static final int ITEM_TYPE_PEOPLE = 198;
    private static final int ITEM_TYPE_PLACE = 199;
    private static final int ITEM_TYPE_QUOTE = 200;
    private OnItemClickListener mClickListener;

    public FeedListAdapter(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        String title = mFeeds.get(position).getTitle();
        if ("people".equalsIgnoreCase(title)) {
            return ITEM_TYPE_PEOPLE;
        } else if ("place".equalsIgnoreCase(title)) {
            return ITEM_TYPE_PLACE;
        } else {
            return ITEM_TYPE_QUOTE;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case ITEM_TYPE_PEOPLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_feed_item, parent, false);
                viewHolder = new PeopleViewHolder(view);
                break;
            case ITEM_TYPE_PLACE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_feed_item, parent, false);
                viewHolder = new PlaceViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_feed_item, parent, false);
                viewHolder = new QuoteViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PeopleViewHolder) {
            PeopleViewHolder holder = (PeopleViewHolder) viewHolder;

            Glide.with(holder.peopleImage.getContext())
                    .load(mFeeds.get(position).getImageUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.image_placeholder)
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.peopleImage);
            holder.peopleName.setText(mFeeds.get(position).getName());
            holder.text.setText(mFeeds.get(position).getText());

        } else if (viewHolder instanceof PlaceViewHolder) {
            PlaceViewHolder holder = (PlaceViewHolder) viewHolder;

            Glide.with(holder.placeImage.getContext())
                    .load(mFeeds.get(position).getImageUrl())
                    .error(R.drawable.image_placeholder)
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.placeImage);
            holder.placeName.setText(mFeeds.get(position).getName());

        } else {
            QuoteViewHolder holder = (QuoteViewHolder) viewHolder;
            holder.authorText.setText(String.format("Written by-%s", mFeeds.get(position).getName()));
            holder.quoteText.setText(mFeeds.get(position).getText());
        }
    }

    @Override
    public long getHeaderId(int position) {
        return mFeeds.get(position).getTime();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        holder.title.setText(""+mFeeds.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mFeeds == null ? 0 : mFeeds.size();
    }

    public void updateWithNewList(List<Feed> newFeeds) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(newFeeds));
        diffResult.dispatchUpdatesTo(this);
        mFeeds = newFeeds;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder{

        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v->{
                if (mClickListener!=null)
                    mClickListener.onItemClick(mFeeds.get(getAdapterPosition()));
            });
        }
    }

    class PeopleViewHolder extends BaseViewHolder {

        ImageView peopleImage;
        TextView peopleName;
        TextView text;

        public PeopleViewHolder(@NonNull View view) {
            super(view);
            peopleImage = view.findViewById(R.id.image);
            peopleName = view.findViewById(R.id.nameText);
            text = view.findViewById(R.id.text);

        }
    }

    class QuoteViewHolder extends BaseViewHolder {
        TextView quoteText, authorText;
        ;

        public QuoteViewHolder(@NonNull View view) {
            super(view);
            quoteText = view.findViewById(R.id.quoteText);
            authorText = view.findViewById(R.id.authorText);
        }
    }

    class PlaceViewHolder extends BaseViewHolder {
        ImageView placeImage;
        TextView placeName;

        public PlaceViewHolder(@NonNull View view) {
            super(view);
            placeImage = view.findViewById(R.id.image);
            placeName = view.findViewById(R.id.placeName);
        }
    }

    class HeaderViewHolder extends BaseViewHolder {

        TextView title;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    class DiffUtilCallback extends DiffUtil.Callback {

        private List<Feed> newList;

        DiffUtilCallback(List<Feed> newList) {
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return mFeeds == null ? 0 : mFeeds.size();
        }

        @Override
        public int getNewListSize() {
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Feed newFeed = newList.get(newItemPosition);
            Feed oldFeed = mFeeds.get(oldItemPosition);
            return TextUtils.equals(newFeed.getName(), oldFeed.getName());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return Objects.equals(mFeeds.get(oldItemPosition), newList.get(newItemPosition));
        }

    }
}
