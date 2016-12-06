package com.twiceyuan.ssrules.model;

import java.io.Serializable;

/**
 * Created by twiceYuan on 02/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class AclFile implements Serializable {

    public String filePath; // 文件路径
    public String fileName; // 文件名称
    public long lastUpdate; // 最后修改
    public long fileSize; // 文件大小

    @Override
    public String toString() {
        return fileName;
    }
}
