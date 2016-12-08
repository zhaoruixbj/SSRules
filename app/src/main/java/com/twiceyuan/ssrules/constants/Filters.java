package com.twiceyuan.ssrules.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twiceYuan on 05/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public interface Filters {

    Map<String, String> TYPE = new HashMap<String, String>() {{
        put("[black_list]", "黑名单");
        put("[white_list]", "白名单");
    }};

    Map<String, String> MODE = new HashMap<String, String>() {{
        put("[proxy_all]", "模式：只代理黑名单外地址");
        put("[bypass_all]", "模式：只代理白名单地址");
    }};
}
