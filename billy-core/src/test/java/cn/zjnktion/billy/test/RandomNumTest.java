package cn.zjnktion.billy.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/28.
 */
public class RandomNumTest {

    private static final Random RAN = new Random();

    public static String ran10Num(long staffNum) {
        BigDecimal staff = new BigDecimal(staffNum);
        BigDecimal time = new BigDecimal(new Date().getTime());
        BigDecimal seed = staff.add(time);
        RAN.setSeed(seed.longValue());
        long num = Math.abs(RAN.nextLong() % 10000000000L);
        return String.format("%010d", num);
    }

    public static String ran14Num(long staffNum) {
        BigDecimal staff = new BigDecimal(staffNum);
        BigDecimal time = new BigDecimal(new Date().getTime());
        BigDecimal seed = staff.add(time);
        RAN.setSeed(seed.longValue());
        long num = Math.abs(RAN.nextLong() % 100000000000000L);
        return String.format("%014d", num);
    }

    public static String ran16Num(long staffNum) {
        BigDecimal staff = new BigDecimal(staffNum);
        BigDecimal time = new BigDecimal(new Date().getTime());
        BigDecimal seed = staff.add(time);
        RAN.setSeed(seed.longValue());
        long num = Math.abs(RAN.nextLong() % 10000000000000000L);
        return String.format("%016d", num);
    }

    public static void main(String[] args) {
        System.out.println(RandomNumTest.ran10Num(10086000));
        System.out.println(RandomNumTest.ran14Num(10086000));
        System.out.println(RandomNumTest.ran16Num(10086000));

        System.out.println(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS));
    }
}
