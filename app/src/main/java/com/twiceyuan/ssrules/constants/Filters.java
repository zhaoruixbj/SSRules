package com.twiceyuan.ssrules.constants;

import java.util.regex.Pattern;

/**
 * Created by twiceYuan on 05/12/2016.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */

public interface Filters {

    Pattern URL = Pattern.compile("(?i)\\b((?:[a-z][\\w-]+:(?:/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\\'\".,<>?«»“”‘’]))");
}
