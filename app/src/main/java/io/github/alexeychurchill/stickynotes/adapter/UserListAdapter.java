package io.github.alexeychurchill.stickynotes.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.model.User;

/**
 * User list adapter for RecyclerView
 */

public abstract class UserListAdapter<U> extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<U> mDataList;
    private OnUserListActionListener mActionListener;

    public void setDataList(List<U> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setActionListener(OnUserListActionListener actionListener) {
        this.mActionListener = actionListener;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position, getUser(position), getInfo(position), mActionListener);
    }

    @Override
    public int getItemCount() {
        return (mDataList == null) ? 0 : mDataList.size();
    }

    protected User getUser(int position) {
        return getUser(mDataList.get(position));
    }

    protected String getInfo(int position) {
        return getInfo(mDataList.get(position));
    }

    protected abstract User getUser(U u);

    protected abstract String getInfo(U u);

    public interface OnUserListActionListener {
        boolean userListNeedActionOne();
        String getActionOneTitle();
        void onUserListActionOne(int position);
        boolean userListNeedActionTwo();
        String getActionTwoTitle();
        void onUserListActionTwo(int position);
        boolean needItemClick();
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mRootView;
        private TextView mTVFirstLine;
        private TextView mTVSecondLine;
        private TextView mTVThirdLine;
        private Button mBtnFirstAction;
        private Button mBtnSecondAction;
        private OnUserListActionListener mActionListener;
        private int mPosition = -1;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            mTVFirstLine = ((TextView) itemView.findViewById(R.id.tvFirstLine));
            mTVSecondLine = ((TextView) itemView.findViewById(R.id.tvSecondLine));
            mTVThirdLine = ((TextView) itemView.findViewById(R.id.tvThirdLine));
            mBtnFirstAction = ((Button) itemView.findViewById(R.id.btnFirstAction));
            mBtnSecondAction = ((Button) itemView.findViewById(R.id.btnSecondAction));
        }

        void bind(int position, User user, String info, OnUserListActionListener listener) {
            mPosition = position;
            bindUser(user);
            bindInfo(info);
            bindActions(listener);
        }

        private void bindActions(OnUserListActionListener listener) {
            mActionListener = listener;
            if (listener == null) {
                return;
            }
            // First button
            mBtnFirstAction.setOnClickListener(
                    (listener.userListNeedActionOne()) ? mActionOneClickListener : null
            );
            mBtnFirstAction.setVisibility(
                    (listener.userListNeedActionOne()) ? View.VISIBLE : View.GONE
            );
            mBtnFirstAction.setText(
                    (listener.getActionOneTitle() != null) ? listener.getActionOneTitle() : ""
            );
            // Second button
            mBtnSecondAction.setOnClickListener(
                    (listener.userListNeedActionTwo()) ? mActionTwoClickListener : null
            );
            mBtnSecondAction.setVisibility(
                    (listener.userListNeedActionTwo()) ? View.VISIBLE : View.GONE
            );
            mBtnSecondAction.setText(
                    (listener.getActionTwoTitle() != null) ? listener.getActionTwoTitle() : ""
            );
            // Root view
            mRootView.setOnClickListener(
                    (listener.needItemClick()) ? mItemClickListener : null
            );
        }

        private void bindInfo(String info) {
            if (info == null) {
                mTVThirdLine.setVisibility(View.GONE);
                return;
            }
            mTVThirdLine.setVisibility(View.VISIBLE);
            mTVThirdLine.setText(info);
        }

        private void bindUser(User user) {
            StringBuilder firstLineBuilder = new StringBuilder();
            if (user.getName() != null) {
                firstLineBuilder.append(user.getName().trim());
            }
            if (user.getLastName() != null) {
                if (firstLineBuilder.length() > 0) {
                    firstLineBuilder.append(' ');
                }
                firstLineBuilder.append(user.getLastName().trim());
            }
            if (firstLineBuilder.length() > 0) {
                mTVFirstLine.setText(firstLineBuilder.toString());
                mTVSecondLine.setVisibility(View.VISIBLE);
                mTVSecondLine.setText(user.getLogin());
            } else {
                mTVFirstLine.setText(user.getLogin());
                mTVSecondLine.setVisibility(View.GONE);
            }
        }

        private View.OnClickListener mItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null) {
                    mActionListener.onItemClick(mPosition);
                }
            }
        };

        private View.OnClickListener mActionOneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null) {
                    mActionListener.onUserListActionOne(mPosition);
                }
            }
        };

        private View.OnClickListener mActionTwoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null) {
                    mActionListener.onUserListActionTwo(mPosition);
                }
            }
        };
    }
}
