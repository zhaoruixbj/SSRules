package com.twiceyuan.ssrules.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.twiceyuan.commonadapter.library.LayoutId;
import com.twiceyuan.commonadapter.library.ViewId;
import com.twiceyuan.commonadapter.library.holder.CommonHolder;
import com.twiceyuan.ssrules.R;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@LayoutId(R.layout.item_acl_line)
public class AclLineHolder extends CommonHolder<String> {

    @ViewId(R.id.tv_line) private TextView tvLine;

    @Override
    public void bindData(String s) {

        Context context = getItemView().getContext();

        tvLine.setTypeface(null, Typeface.NORMAL);
        tvLine.setTextColor(ContextCompat.getColor(context, R.color.text_normal));

        if (s.startsWith("[")) {
            tvLine.setTypeface(null, Typeface.BOLD);
            tvLine.setTextColor(ContextCompat.getColor(context, R.color.text_dark));
        }

        if (s.startsWith("#")) {
            tvLine.setTextColor(ContextCompat.getColor(context, R.color.text_light));
        }

        tvLine.setText(s);
    }
}
