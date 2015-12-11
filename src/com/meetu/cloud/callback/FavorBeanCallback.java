package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.bean.FavorBean;

public interface FavorBeanCallback {
void callback(List<FavorBean> favorlist,AVException e);
}
