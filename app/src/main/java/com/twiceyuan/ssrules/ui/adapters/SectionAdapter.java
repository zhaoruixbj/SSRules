package com.twiceyuan.ssrules.ui.adapters;

import android.content.Context;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.twiceyuan.commonadapter.library.adapter.CommonAdapter;
import com.twiceyuan.commonadapter.library.holder.CommonHolder;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public abstract class SectionAdapter<T, VH extends CommonHolder<T>> extends CommonAdapter<T, VH> implements SectionTitleProvider {

    public SectionAdapter(Context context, Class<? extends CommonHolder<T>> holderClass) {
        super(context, holderClass);
    }
}
