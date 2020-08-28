package com.ruoyi.wuliu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class randomLongUtils {

    public static void main(String[] args) {
        List<Integer> random = random(94960, 33);
        random.forEach(item->{
            System.out.println(item);
        });


    }
    public static List<Integer> random(int totalNum, int count) {
        // 创建一个长度的数组
        List<Integer> redList = new ArrayList<>();

        int totalMoney = (totalNum);

        /*if (totalMoney < count || totalMoney < 1) {
            return redList; // 返回空的集合
        }*/

        //2. 进行随机分配
        Random rand = new Random();

        int leftMoney = totalMoney;  // 剩余的值
        int leftCount = count;  // 剩余份数
        // 随机分配公式：1 + rand.nextInt(leftMoney / leftCount * 2);
        for (int i = 0; i < count - 1; i++) {
            int money_ = 0;
            if (leftMoney > 0) {
                if ((leftMoney / leftCount * 2) < 1) {
                    money_ = leftMoney;
                } else {
                    money_ = 1 + rand.nextInt(leftMoney / leftCount * 2);
                }

            } else {
                money_ = 0;
            }
            redList.add(money_);
            if (money_ > 0) {
                leftMoney -= money_;
                leftCount--;
            }

        }
        // 把剩余的最后一个放到最后一个包里
        redList.add(leftMoney);
        return redList;
    }
}
