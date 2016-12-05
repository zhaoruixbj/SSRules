package com.twiceyuan.ssrules.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twiceYuan on 04/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public class ACLs {
    public static Map<String, String> fileNameMap = new HashMap<String, String>() {{
        put("bypass-lan.acl", "绕过局域网地址");
        put("bypass-china.acl", "绕过中国大陆地址");
        put("bypass-lan-china.acl", "绕过局域网及中国大陆地址");
        put("gfwlist.acl", "仅代理中国大陆无法访问的地址");
        put("china-list.acl", "仅代理中国大陆地址");
    }};
}
