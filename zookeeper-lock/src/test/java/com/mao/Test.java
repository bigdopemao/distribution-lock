package com.mao;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bigdope
 * @create 2019-01-12
 **/
public class Test {

    public static void main(String[] args) {
//        Map<String, String> map = new LinkedHashMap<>();
        Map<String, String> map = new HashMap<>();
//        Map<String, String> map = new TreeMap<>();
//        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("b", "b");
        map.put("a", "a");
        map.put("d", "d");
        map.put("c", "c");
        String putIfAbsent = map.putIfAbsent("a", "11");
        System.out.println(putIfAbsent);

//        map.get("a");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
        }

//        Set<String> set = new LinkedHashSet<>();
//        Set<String> set = new HashSet<>();
//        set.add("d");
//        set.add("a");
//        set.add("c");
//        for (String s : set) {
//            System.out.println(s);
//        }

        List<String> list = new LinkedList<>();
        Hashtable hashtable = new Hashtable();
//        hashtable.put(null, "a");

    }

}
