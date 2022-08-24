package com.frame.contants;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/9
 * Time: 17:35
 * Description:
 */
public enum CookieConstant {
    USER_TOKEN("user-token"),;

    public String getName() {
        return name;
    }

    private String name;

    CookieConstant(String name) {
        this.name = name;
    }
}
