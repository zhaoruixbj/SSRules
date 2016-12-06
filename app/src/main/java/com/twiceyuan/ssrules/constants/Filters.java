package com.twiceyuan.ssrules.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by twiceYuan on 05/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public interface Filters {

    Pattern URL = Pattern.compile("(?i)\\b((?:[a-z][\\w-]+:(?:/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\\'\".,<>?«»“”‘’]))");

    List<String> ACL_TYPE = Arrays.asList("[black_list]", "[white_list]");

    Map<String, String> TYPE = new HashMap<String, String>() {{
        put("[black_list]", "黑名单");
        put("[white_list]", "白名单");
    }};

    Map<String, String> MODE = new HashMap<String, String>() {{
        put("[proxy_all]", "模式：只代理黑名单外地址");
        put("[bypass_all]", "模式：只代理白名单地址");
    }};
}
