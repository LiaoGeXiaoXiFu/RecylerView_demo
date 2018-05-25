package com.example.dianshang_rv;

import android.view.View;

/**
 * Created by 撩个小媳妇 on 2018/5/24.
 */

public interface ItemClickListener {
    /**
     * item点击
     * @param view
     * @param position
     */
    public void onItemClick(View view, int position);

    /**
     * Item长按
     */
    public void onItemLongClick(View view, int position);

    /**
     *
     * @param view
     * @param position
     */
    public void onItemSubViewClick(View view, int position);
}
