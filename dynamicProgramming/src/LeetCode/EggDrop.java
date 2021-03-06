package LeetCode;

/**
 * 你将获得 K 个鸡蛋，并可以使用一栋从 1 到 N  共有 N 层楼的建筑。
 * 每个蛋的功能都是一样的，如果一个蛋碎了，你就不能再把它掉下去。
 * 你知道存在楼层 F ，满足 0 <= F <= N 任何从高于 F 的楼层落下的鸡蛋都会碎，从 F 楼层或比它低的楼层落下的鸡蛋都不会破。
 * 每次移动，你可以取一个鸡蛋（如果你有完整的鸡蛋）并把它从任一楼层 X 扔下（满足 1 <= X <= N）。
 * 你的目标是确切地知道 F 的值是多少。
 * 无论 F 的初始值如何，你确定 F 的值的最小移动次数是多少？
 * <p>
 * 示例 1：
 * 输入：K = 1, N = 2
 * 输出：2
 * 解释：
 * 鸡蛋从 1 楼掉落。如果它碎了，我们肯定知道 F = 0 。
 * 否则，鸡蛋从 2 楼掉落。如果它碎了，我们肯定知道 F = 1 。
 * 如果它没碎，那么我们肯定知道 F = 2 。
 * 因此，在最坏的情况下我们需要移动 2 次以确定 F 是多少。
 * 示例 2：
 * 输入：K = 2, N = 6
 * 输出：3
 * 示例 3：
 * 输入：K = 3, N = 14
 * 输出：4
 * <p>
 * 提示：
 * 1 <= K <= 100
 * 1 <= N <= 10000
 */
public class EggDrop {
    /**
     * 经典DP问题：
     * 此题的核心在于理解鸡蛋的个数与可移动次数的制约关系，从而装换问题的视角
     * 把求最小移动次数的问题转化为最大确定层数的问题即可
     * <p>
     * 需要注意，因为我们要采取最保守的策略，即当只有一个鸡蛋时，只能从已知的最低层一层一层往上尝试来确定目标层数
     * 因此，当我们只有一个鸡蛋时，能移动几次就能确定几层
     * 另外，当我们只能移动一次时，即使我们拥有再多的鸡蛋，也只能确定一层的高度
     * 基于此我们可以画出辅助判断a个鸡蛋移动b次最多可以确定多少层的dp表
     * 其中，上面两条准则就是初始条件，我们可以确定第一行和第一列的结果
     * a/b  1   2   3   4   5   6   7   8   9   10  ……
     * 1    1   2   3   4   5   6   7   8   9   10  ……
     * 2    1
     * 3    1
     * 4    1
     * 5    1
     * 6    1
     * 7    1
     * 8    1
     * 9    1
     * 10   1
     * ……   ……
     * 那么接下来的空位需要怎么填充，即每一个值是如何依赖已有的状态的
     * 我们考虑在还有a个鸡蛋可以移动b次的情况下，任意选择一层扔下鸡蛋，会出现两种情况：
     * 1 鸡蛋碎了，我们还剩下a-1个鸡蛋和b-1次移动的机会
     * 2 鸡蛋没碎，我们还剩下a个鸡蛋和b-1次移动的机会
     * 而整个楼层的层数可以分为鸡蛋碎了以后往下找的一半和鸡蛋没碎往上找的一半相加，最后还要加本次扔鸡蛋所确定的这一层
     * 因此由状态转移方程：f(a,b) = f(a-1,b-1) + f(a,b-1) + 1
     * 于是我们可以完善上面的dp表：
     * a/b  1   2   3   4   5   6   7   8   9   10  ……
     * 1    1   2   3   4   5   6   7   8   9   10  ……
     * 2    1   3   6   10  15  21  28  36  45  55  ……
     * 3    1   3   7   14  25  41  63  92  129 175 ……
     * 4    1   3   7   15  30  56  98  162 255 385 ……
     * 5    1   3   7   15  31  62  119 218 381 637 ……
     * 6    1   3   7   15  31  63  126 246 465 847 ……
     * 7    1   3   7   15  31  63  127 254 501 967 ……
     * 8    1   3   7   15  31  63  127 255 510 1012
     * 9    1   3   7   15  31  63  127 255 511 1022
     * 10   1   3   7   15  31  63  127 255 511 1023
     * ……   ……  ……
     * 由于dp表中每一个值只会依赖其左边一列的状态，所以可以简化为一位数组
     */
    public int superEggDrop(int K, int N) {
//      声明一个一位数组，长度等于鸡蛋数量+1
        int[] dp = new int[K + 1];
//      操作的次数
        int ans = 0;
//        最终K个蛋扔ans次最多确定的层数一旦超过N层，即为所求的结果
        while (dp[K] < N) {
//          从后往前计算（这样每一次计算可以得出一列的dp表状态，并且计算下一列时从后往前计算不会破坏依赖关系）
            for (int i = K; i > 0; i--)
//                此处等号左侧的dp[i]相当于f(a,b)，等号右侧的堆排dp[i]相当于f(a,b-1)，……
                dp[i] = dp[i] + dp[i - 1] + 1;
//            每计算一轮等于多移动一次
            ans++;
            System.out.println(K + "个鸡蛋，扔" + ans + "次，最多能确定" + dp[K] + "层楼");
        }
        return ans;
    }

    public static void main(String[] args) {
        EggDrop eggDrop = new EggDrop();
        eggDrop.superEggDrop(4, 386);
    }
}
