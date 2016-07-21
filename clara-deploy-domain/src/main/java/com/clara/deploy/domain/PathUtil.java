package com.clara.deploy.domain;

/**
 * Created by marui5 on 2016/7/21.
 */
public class PathUtil {

    public static String getPath(String root, String ... more) {
        String separator = "\\";
        StringBuilder path = new StringBuilder(root);
        for(String file : more) {
            path.append(separator).append(file);
        }

        return path.toString();
    }

    public static String getRelativePath(String path, String fileName) {
        String separator = "\\";

        StringBuilder relativePath = new StringBuilder(path);
        return relativePath.append(fileName).append(separator).toString();
    }

}
