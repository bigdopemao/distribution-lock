package com.mao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * https://blog.csdn.net/u014082714/article/details/51867973
 * https://www.cnblogs.com/assassin666/p/5903448.html
 * @author bigdope
 * @create 2019-01-17
 **/
public class Test1 {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 2};
//        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//        for(int i=0; i < arr.length; i++) {
//            if(map.containsKey(arr[i])) {
//                map.put(arr[i], map.get(arr[i]) + 1);
//            } else {
//                map.put(arr[i], 1);
//            }
//        }
//
//        Collection<Integer> collection = map.values();
//
//        Integer num = Collections.max(collection);
//
//        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
//            if(num.equals(entry.getValue())) {
//                System.out.println(entry.getKey());
//                break;
//            }
//        }

        getMethod_4(arr);
    }


    /**数组中元素重复最多的数
     * @param array
     * @author shaobn
     * @param array
     */
    public static void getMethod_4(int[] array){
        int count_2 = 0;
        int num = 0;
        for(int i=0;i<array.length;i++){
            int count = 0;
            for(int j=i+1;j<array.length;j++){
                if(array[i]==array[j]){
                    count++;
                }
                continue;
            }
            if(count>count_2){
                count_2 = count;
                num = array[i];
            }

        }
        System.out.println(num);
    }

}
