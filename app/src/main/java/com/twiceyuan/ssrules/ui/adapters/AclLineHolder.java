package com.twiceyuan.ssrules.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.twiceyuan.commonadapter.library.LayoutId;
import com.twiceyuan.commonadapter.library.ViewId;
import com.twiceyuan.commonadapter.library.holder.CommonHolder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.constants.Filters;

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

        if (matchHeader(context, s)) {
            return;
        }

        if (s.startsWith("#")) {
            tvLine.setTextColor(ContextCompat.getColor(context, R.color.text_light));
        }

        tvLine.setText(s);
    }

    private boolean matchHeader(Context context, String s) {
        if (!s.startsWith("[")) {
            return false;
        }
        tvLine.setTypeface(null, Typeface.BOLD);
        tvLine.setTextColor(ContextCompat.getColor(context, R.color.text_dark));

        if (Filters.MODE.keySet().contains(s)) {
            tvLine.setText(Filters.MODE.get(s));
        } else if (Filters.TYPE.keySet().contains(s)) {
            tvLine.setText(Filters.TYPE.get(s));
        } else {
            tvLine.setText(s);
        }
        return true;
    }
}
