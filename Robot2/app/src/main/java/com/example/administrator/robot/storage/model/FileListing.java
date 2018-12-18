package com.example.administrator.robot.storage.model;

import com.example.administrator.robot.util.StringUtils;

/**
 * Created by bailong on 15/2/20.
 */
public final class FileListing {
    public FileInfo[] items;
    public String marker;
    public String[] commonPrefixes;

    public boolean isEOF() {
        return StringUtils.isNullOrEmpty(marker);
    }
}
