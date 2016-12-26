package io.github.alexeychurchill.stickynotes.activity.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.alexeychurchill.stickynotes.R;

/**
 * Menu adapter
 */

public class MenuAdapter extends BaseAdapter {
    private String[] mMenuStrings;
    private Drawable[] mMenuDrawables;
    private Context mContext;

    public MenuAdapter(Context context, String[]menuStrings, Drawable[] menuDrawables) {
        this.mContext = context;
        this.mMenuStrings = menuStrings;
        this.mMenuDrawables = menuDrawables;
    }

    @Override
    public int getCount() {
        return mMenuStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return mMenuStrings[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater =
                    ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            view = inflater.inflate(R.layout.list_menu_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        ViewHolder viewHolder = ((ViewHolder) view.getTag());
        viewHolder.bind(mMenuStrings[i], mMenuDrawables[i]);
        return view;
    }

    private static class ViewHolder {
        private ImageView mIVImage;
        private TextView mTVTitle;

        ViewHolder(View view) {
            mIVImage = ((ImageView) view.findViewById(R.id.ivImage));
            mTVTitle = ((TextView) view.findViewById(R.id.tvTitle));
        }

        void bind(String name, Drawable icon) {
            mTVTitle.setText(name);
            mIVImage.setImageDrawable(icon);
        }
    }
}
