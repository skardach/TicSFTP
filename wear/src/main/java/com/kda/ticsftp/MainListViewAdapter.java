package com.kda.ticsftp;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

final class MainListViewAdapter extends WearableListView.Adapter {
    private final Context mContext;
    private List<ListElement> mElements;
    private final LayoutInflater mInflater;

    static class ListElement {
        private int mTag;
        public String mName;
        public String mDescription;

        ListElement(int tag, String name, String description) {
            mTag = tag;
            mName = name;
            mDescription = description;
        }

        public void formatItemView(Context context,
                                   WearableListView.ViewHolder holder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.mName.setVisibility(View.GONE);
            itemHolder.mDescription.setVisibility(View.GONE);
            itemHolder.mImg.setVisibility(View.GONE);

            if (mName != null) {
                itemHolder.mName.setVisibility(View.VISIBLE);
                itemHolder.mName.setText(mName);
            }
            if (mDescription != null) {
                itemHolder.mDescription.setVisibility(View.VISIBLE);
                itemHolder.mDescription.setText(mDescription);
            }
        }

        public int getTag() {
            return mTag;
        }
    }
    static class ToggleListElement extends ListElement {
        private boolean mToggled;

        ToggleListElement(int tag, String name, String description,
                          boolean initial) {
            super(tag, name, description);
            mToggled = initial;
        }

        @Override
        public void formatItemView(Context context,
                                   WearableListView.ViewHolder holder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            super.formatItemView(context, holder);
            if (mToggled) {
                itemHolder.mImg.setImageDrawable(
                        context.getResources().getDrawable(
                                R.drawable.circle_ticked));
            } else {
                itemHolder.mImg.setImageDrawable(
                        context.getResources().getDrawable(
                                R.drawable.circle_unticked));
            }
            itemHolder.mImg.setVisibility(View.VISIBLE);
        }

        public void handleClick(Context context,
                                WearableListView.ViewHolder holder) {
            mToggled = !mToggled;
            formatItemView(context, holder);
        }

        public boolean isToggled() {
            return mToggled;
        }
    }

    MainListViewAdapter(Context context, List<ListElement> elements) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mElements = elements;
    }

    private static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView mName;
        private TextView mDescription;
        private ImageView mImg;

        ItemViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.li_main_name);
            mDescription =
                    (TextView) itemView.findViewById(R.id.li_main_description);
            mImg = (ImageView) itemView.findViewById(R.id.li_main_img);
        }
    }


    public WearableListView.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(
                mInflater.inflate(R.layout.list_item_main, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        ListElement e = mElements.get(position);
        if (e == null)
            return;
        e.formatItemView(mContext, holder);
        holder.itemView.setTag(e.getTag());
    }

    @Override
    public int getItemCount() {
        return mElements.size();
    }
}
