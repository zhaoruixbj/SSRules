package com.twiceyuan.ssrules.ui.adapters;

import android.text.format.Formatter;
import android.widget.TextView;

import com.twiceyuan.commonadapter.library.LayoutId;
import com.twiceyuan.commonadapter.library.ViewId;
import com.twiceyuan.commonadapter.library.holder.CommonHolder;
import com.twiceyuan.ssrules.R;
import com.twiceyuan.ssrules.constants.Formats;
import com.twiceyuan.ssrules.model.AclFile;

/**
 * Created by twiceYuan on 02/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@LayoutId(R.layout.item_acl_file)
public class AclFileHolder extends CommonHolder<AclFile> {

    @ViewId(R.id.tv_name)        private TextView tv_name;
    @ViewId(R.id.tv_last_update) private TextView tv_last_update;
    @ViewId(R.id.tv_file_size)   private TextView tv_file_size;
    @ViewId(R.id.tv_path) private TextView tv_path;

    @Override
    public void bindData(AclFile aclFile) {
        String fileSize = Formatter.formatFileSize(getItemView().getContext(), aclFile.fileSize);
        tv_path.setText(aclFile.filePath);
        tv_file_size.setText(fileSize);
        tv_name.setText(aclFile.fileName);
        tv_last_update.setText(String.format("上次修改：%s", Formats.dateFormat.format(aclFile.lastUpdate)));
    }
}
