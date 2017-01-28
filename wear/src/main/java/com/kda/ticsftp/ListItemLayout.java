package com.kda.ticsftp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.wearable.view.WearableListView;
import android.widget.TextView;

class ListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private TextView mName;
    private TextView mDescription;
    private ImageView mImage;

    private final float mFadedAlpha;

    public ListItemLayout(Context context) {
        this(context, null);
    }

    public ListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mFadedAlpha = getResources()
                .getInteger(R.integer.action_text_faded_alpha) / 100f;
    }

    // Get references to the icon and text in the item layout definition
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // These are defined in the layout file for list items
        // (see next section)
        mName = (TextView) findViewById(R.id.li_main_name);
        mDescription = (TextView) findViewById(R.id.li_main_description);
        mImage = (ImageView) findViewById(R.id.li_main_img);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        mName.setAlpha(1f);
        mDescription.setAlpha(1f);
        if (mImage != null) {
            mImage.setAlpha(1f);
        }
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        mName.setAlpha(mFadedAlpha);
        mDescription.setAlpha(mFadedAlpha);
        if (mImage != null) {
            mImage.setAlpha(mFadedAlpha);
        }
    }
}
