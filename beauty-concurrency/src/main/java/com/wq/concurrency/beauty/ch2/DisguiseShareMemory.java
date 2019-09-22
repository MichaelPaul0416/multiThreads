package com.wq.concurrency.beauty.ch2;

/**
 * 内存伪共享的情况
 */
public class DisguiseShareMemory {

    private static final int row = 10240;

    private static final int column = 10240;

    public static void main(String[] args) {
        /**
         * array_1的初始化速度明显高于array_2
         * 因为cpu会在读入数据的时候,会将目标数据的附近数据也一起读入,也就是说array_1是顺序写入数据的,一次读进来,可能后面n次就不用再读入了
         * 而array_2在读取array_2[0][0]的时候虽然也是顺序读入,但是下一个要写入的不是array_2[0][1],而是array_2[1][0],这个可能远远没有读进来
         * 所以需要重新从住内存中读取数据,所以和主内存的交换次数就比array_1多得多,所以时间就消耗的多
         */
        int[][] array_1 = new int[row][column];
        long start = System.currentTimeMillis();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                array_1[i][j] = i * 2 + j;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("cost-a:" + (end - start));

        int[][] array_2 = new int[row][column];
        start = System.currentTimeMillis();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                array_2[j][i] = i * 2 + j;
            }
        }
        end = System.currentTimeMillis();
        System.out.println("cost-b:" + (end - start));
    }
}
