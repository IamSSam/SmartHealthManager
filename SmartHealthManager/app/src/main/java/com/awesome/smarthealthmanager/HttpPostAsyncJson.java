package com.awesome.smarthealthmanager;

import java.io.InputStream;

/**
 * Created by yoonjae on 29/11/2016.
 */

public interface HttpPostAsyncJson {
    public String POST(String url, Person person);
    public String convertInputStreamToString(InputStream inputStream);
}
