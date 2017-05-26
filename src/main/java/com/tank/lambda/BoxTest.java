package com.tank.lambda;

/**
 * Description:
 * User： JinHuaTao
 * Date：2017/5/3
 * Time: 14:33
 */

public class BoxTest {

    public static void main(String[] args) {

       // System.out.println(isEqual(12,12));
//        System.out.println(isEqual(new Integer(12), new Integer(12)));

        String x = "abc".substring(2);
        String y = "abc".substring(2);
        System.out.println(x);
        System.out.println(x.equals("c"));

    }

    public static boolean isEqual(int x, int y){
        return x == y;
    }

    public static boolean isEqual(Integer x, Integer y){
        return x == y;
    }
}
