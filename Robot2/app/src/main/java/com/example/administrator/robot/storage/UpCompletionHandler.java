package com.example.administrator.robot.storage;
import com.example.administrator.robot.http.Response;

/**
 * Created by bailong on 15/10/8.
 */
public interface UpCompletionHandler {
    void complete(String key, Response r);
}
