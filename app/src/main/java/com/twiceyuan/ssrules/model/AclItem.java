package com.twiceyuan.ssrules.model;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class AclItem {

    public static final int TYPE_DOMAIN = 1;
    public static final int TYPE_IP = 2;

    public String content;

    public AclItem(String content) {
        this.content = content;
    }

    public int getType() {
        if (content.contains("/")) {
            return TYPE_IP;
        } else {
            return TYPE_DOMAIN;
        }
    }

    public String getTypeName() {
        return getType() == TYPE_DOMAIN ? "domain" : "IP";
    }

    public String getHost() {
        if (getType() == TYPE_IP) {
            int flag = content.indexOf("/");
            return content.substring(0, flag);
        }
        if (getType() == TYPE_DOMAIN) {
            return content.replace("(.*\\.)?", "").replace("\\", "");
        }
        return "";
    }

    public int getMask() {
        if (getType() == TYPE_IP) {
            int flag = content.indexOf("/");
            return Integer.parseInt(content.substring(flag + 1));
        } else {
            return -1;
        }
    }
}
