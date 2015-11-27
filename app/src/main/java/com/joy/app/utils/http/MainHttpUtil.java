package com.joy.app.utils.http;

import java.util.Map;

/**
 * 首页的一些网络请求
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-16
 */
public class MainHttpUtil extends BaseHtpUtil {

    /**
     * 获取首页的目标列表
     *
     * @return
     */
    public static String getMainRouteList(int page, int count) {

        Map<String, Object> params = getBaseParams();
        params.put(KEY_PAGE, page);
        params.put(KEY_COUNT, count);
        return createGetUrl(URL_POST_MAIN_ROUTE_LIST, params);
    }
}
