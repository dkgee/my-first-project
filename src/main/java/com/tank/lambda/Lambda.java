package com.tank.lambda;

import com.tank.lambda.bean.Person;

import java.util.*;
import java.util.function.Predicate;

/**
 * Description:测试Lambda测试
 * User： JinHuaTao
 * Date：2017/4/15
 * Time: 9:32
 */

public class Lambda {

    public static void testList(){
        String[] atp = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka",
                "David Ferrer","Roger Federer",
                "Andy Murray","Tomas Berdych",
                "Juan Martin Del Potro"};
        List<String> players =  Arrays.asList(atp);
        //01,使用 lambda 表达式以及函数操作(functional operation)
        players.forEach((player)-> System.out.println(player + ";"));

        //02,在 Java 8 中使用双冒号操作符(double colon operator)
        players.forEach(System.out::println);


    }

    public static void testList02(){
        String[] atp = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka",
                "David Ferrer","Roger Federer",
                "Andy Murray","Tomas Berdych",
                "Juan Martin Del Potro"};
        //01, 匿名类可以使用lambda表达式来代替
        //Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));
        //Arrays.sort(atp, sortByName);

        //02, 匿名类可以使用lambda表达式来代替
        Arrays.sort(atp, (String s1, String s2) -> (s1.compareTo(s2)));

        List<String> players =  Arrays.asList(atp);
        players.forEach(System.out::println);

        Comparator<String> sortByName = (String s1, String s2) ->(s1.substring(s1.indexOf(" ")).compareTo(s2.substring(s2.indexOf(" "))));
        Comparator<String> sortByNameLength = (String s1, String s2) ->(s1.length() - s2.length());

        Comparator<String> sortByLastLetter = (String s1, String s2) -> (
             (s1.charAt(s1.length()) - 1) - (s2.charAt(s2.length()) -1)
        );
    }

    public static void testList03(){
        List<Person> javaProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));
                add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));
                add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));
                add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));
                add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));
                add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));
                add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));
                add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));
                add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));
                add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));
            }
        };

        List<Person> phpProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Jarrod", "Pace", "PHP programmer", "male", 34, 1550));
                add(new Person("Clarette", "Cicely", "PHP programmer", "female", 23, 1200));
                add(new Person("Victor", "Channing", "PHP programmer", "male", 32, 1600));
                add(new Person("Tori", "Sheryl", "PHP programmer", "female", 21, 1000));
                add(new Person("Osborne", "Shad", "PHP programmer", "male", 32, 1100));
                add(new Person("Rosalind", "Layla", "PHP programmer", "female", 25, 1300));
                add(new Person("Fraser", "Hewie", "PHP programmer", "male", 36, 1100));
                add(new Person("Quinn", "Tamara", "PHP programmer", "female", 21, 1000));
                add(new Person("Alvin", "Lance", "PHP programmer", "male", 38, 1600));
                add(new Person("Evonne", "Shari", "PHP programmer", "female", 40, 1800));
            }
        };

//        System.out.println("所有程序员的姓名:");
//        javaProgrammers.forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));
//        phpProgrammers.forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));

//        System.out.println("给程序员加薪 5% :");
//        Consumer<Person> giveRaise = e -> e.setSalary(e.getSalary()/100 * 5 + e.getSalary());
//        javaProgrammers.forEach(giveRaise);
//
//        javaProgrammers.forEach((p) -> System.out.printf("%s %s %s;", p.getFirstName(), p.getLastName(), p.getSalary()));

//        System.out.println("下面是月薪超过 $1,400 的PHP程序员:");
//        phpProgrammers.stream().filter((p) -> (p.getSalary() > 1400))
//                .forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));

        //定义过滤器
        Predicate<Person> agaFilter = (p) -> (p.getAge() > 25);
        Predicate<Person> salaryFilter = (p) -> (p.getSalary() > 1400);
        Predicate<Person> genderFilter = (p) -> ("female".equals(p.getGender()));
//
//        System.out.println("下面是年龄大于 24岁且月薪在$1,400以上的女PHP程序员:");
//        phpProgrammers.stream()
//                .filter(agaFilter)
//                .filter(salaryFilter)
//                .filter(genderFilter)
//                .forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));
//
//        System.out.println("年龄大于 24岁的女性 Java programmers:");
//        javaProgrammers.stream()
//                .filter(agaFilter)
//                .filter(genderFilter)
//                .forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));

//        System.out.println("最前面的3个 Java programmers:");
//        javaProgrammers.stream()
//                .limit(3)
//                .forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));
//
//        System.out.println("最前面的3个女性 Java programmers:");
//        javaProgrammers.stream()
//                .filter(genderFilter)
//                .limit(3)
//                .forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));

//        System.out.println("根据 name 排序,并显示前5个 Java programmers:");
//        List<Person> sortedJavaProgrammers = javaProgrammers.stream()
//                .sorted((p, p2) -> (p.getFirstName().compareTo(p2.getFirstName())))
//                .limit(5)
//                .collect(Collectors.toList());
//        sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));
//
//        System.out.println("根据 salary 排序 Java programmers:");
//        sortedJavaProgrammers = javaProgrammers
//                .stream()
//                .sorted((p1, p2)->(p2.getSalary() - p1.getSalary()))
//                .collect(Collectors.toList());
//        sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));

//        System.out.println("工资最低的 Java programmer:");
//        Person  pers = javaProgrammers
//                .stream()
//                .min((p1, p2) -> (p1.getSalary() - p2.getSalary()))
//                .get();
//        System.out.printf("Name: %s %s; Salary: $%,d.", pers.getFirstName(), pers.getLastName(), pers.getSalary());
//
//        System.out.println("工资最高的 Java programmer:");
//        Person person = javaProgrammers
//                .stream()
//                .max((p1, p2)->(p1.getSalary() - p2.getSalary()))
//                .get();
//        System.out.printf("Name: %s %s; Salary: $%,d.", person.getFirstName(), person.getLastName(), person.getSalary());
//
//        System.out.println("计算付给 Java programmers 的所有money:");
//        int totalSalary = javaProgrammers
//                .parallelStream()
//                .mapToInt(p -> p.getSalary())
//                .sum();
//
//        System.out.println(totalSalary);


        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        IntSummaryStatistics stats = numbers
                .stream()
                .mapToInt((x) -> x)
                .summaryStatistics();


        System.out.println("List中最大的数字 : " + stats.getMax());
        System.out.println("List中最小的数字 : " + stats.getMin());
        System.out.println("所有数字的总和   : " + stats.getSum());
        System.out.println("所有数字的平均值 : " + stats.getAverage());

    }

    public static void main(String[] args) {
        //testList();

        testList03();
    }



}
