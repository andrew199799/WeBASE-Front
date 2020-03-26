/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.webank.webase.front.logparse;

import java.io.File;
import java.util.*;

/**
 * FileUtil.
 * format abi types from String
 */
public class FileUtil {

    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].getName());
            }
        }
        return files;
    }

    public static TreeMap<Long, String> getStatFiles(List<String> logNames) {
        TreeMap<Long, String> treeMap = new TreeMap<>();
        for (String name : logNames) {
            if(name.startsWith("stat_"))
            {
                long statFileNameNumber = getStatFileNameNumber(name);
                treeMap.put(statFileNameNumber,name);
            }
        }
        return treeMap;
    }

    public static Long getStatFileNameNumber(String name) {
        String[] sArray = name.split("\\.|_");
        Long statFileNameNumber = (long)0;
        if (sArray.length > 3)
        {
            statFileNameNumber = Long.valueOf(sArray[1])*100 +  Long.valueOf(sArray[2]);

        }
        return statFileNameNumber;
    }

    public static void clearOldStatFiles(TreeMap<Long, String> treeMap, String name) {
        Long statFileNameNumber = getStatFileNameNumber(name);
        Iterator iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if((Long)entry.getKey() < statFileNameNumber )
                iter.remove();
        }
        return ;
    }

    public static void clearCurrentStatFile(TreeMap<Long, String> treeMap, String name) {
        Long statFileNameNumber = getStatFileNameNumber(name);
        Iterator iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if(0 == Long.compare((Long)entry.getKey(), statFileNameNumber))
                iter.remove();
        }
        return ;
    }


    public static void main(String args[]) {
        Long statFileNameNumber = getStatFileNameNumber("log_2020031819.00.log");
        System.out.println("Hello World!" + statFileNameNumber);
        List<String> files = getFiles("C:\\nodelog");
        for (String s:files
             ) {
            System.out.println("0 " + s);
        }
        List<String> logNames = new ArrayList<String>();
        logNames.add("stat_2020031819.00.log");
        logNames.add("stat_2020031818.00.log");
        logNames.add("stat_2020031817.00.log");
        logNames.add("stat_2020031817.01.log");
        TreeMap<Long, String> treeMap = getStatFiles(files);

        Iterator iter = treeMap.entrySet().iterator();

        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry) iter.next();

            // 获取key

            Long key = (Long) entry.getKey();

            // 获取value

            String value = (String) entry.getValue();

            System.out.println("1 " + key + " " + value);
        }
        System.out.println("___________");
        clearOldStatFiles(treeMap, "stat_2020031818.00.log");

        iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Long key = (Long) entry.getKey();
            String value = (String) entry.getValue();

            System.out.println("2 " + key + " " + value);
        }
        System.out.println("___________");
        clearCurrentStatFile(treeMap, "stat_2020031818.00.log");

        iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Long key = (Long) entry.getKey();
            String value = (String) entry.getValue();

            System.out.println("3 " + key + " " + value);
        }
    }
}