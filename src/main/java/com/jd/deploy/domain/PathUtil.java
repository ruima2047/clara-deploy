package com.jd.deploy.domain;

import java.io.File;

/**
 * Created by marui5 on 2016/7/21.
 */
public class PathUtil {

    /**
     * 获取路径
     * ex.E:\export\Update\sh\PC
     * @param root
     * @param more
     * @return
     */
    public static String getPath(String root, String... more) {
//        String separator = "\\";
        StringBuilder path = new StringBuilder(root);
        for(String file : more) {
            path.append(File.separator).append(file);
        }

        return path.toString();
    }

    /**
     * 获取相对路径
     * ex. DLL\Check\
     * @param path
     * @param fileName
     * @return
     */
    public static String getRelativePath(String path, String fileName) {
//        String separator = "\\";

        StringBuilder relativePath = new StringBuilder(path);
        return relativePath.append(fileName).append(File.separator).toString();
    }

    public static String separatorUniform(String original) {
        char []originalchars = original.toCharArray();
        char []newchars = new char[original.length()];
        for(int i = 0; i < original.length(); i++) {
            if(originalchars[i] == '/') {
                newchars[i] = '\\';
                continue;
            }
            newchars[i] = originalchars[i];
        }
        return new String(newchars);
    }

}
