package com.twiceyuan.ssrules.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.twiceyuan.ssrules.App;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by twiceYuan on 08/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class ViewHelper {

    /**
     * dp 转换为 px
     *
     * @param dpValue dp
     * @return px
     */
    public static int getPx(float dpValue) {
        final float scale = App.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转换为 dp
     *
     * @param pxValue px
     * @return dp
     */
    public static int getDp(float pxValue) {
        final float scale = App.get().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 为 spinner 选中默认值，如果默认值不在 spinner 中，则选中第一个（如果至少存在一个选项）
     *
     * @param spinner      下拉选择框
     * @param defaultValue 默认值
     * @param converter    spinner 中实体转换为默认值的函数
     * @param <T>          spinner 实体类型
     * @param <R>          转换后默认值的类型
     */
    public static <T, R> void setDefaultOrFirst(Spinner spinner, R defaultValue, Func1<T, R> converter) {
        SpinnerAdapter adapter = spinner.getAdapter();
        int count = adapter.getCount();

        boolean isHaveDefault = false;
        for (int i = 0; i < count; i++) {
            //noinspection unchecked
            T item = (T) adapter.getItem(i);
            R compare = converter.call(item);
            if (compare.equals(defaultValue)) {
                spinner.setSelection(i);
                isHaveDefault = true;
                break;
            }
        }
        if (!isHaveDefault && count > 0) {
            spinner.setSelection(0);
        }
    }

    public static void spinnerSelect(Spinner spinner, Action1<Integer> positionCallback) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (positionCallback != null) {
                    positionCallback.call(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public static void watchText(EditText editText, Action1<String> textChangeListener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (textChangeListener != null) {
                    textChangeListener.call(editable.toString());
                }
            }
        });
    }
}
