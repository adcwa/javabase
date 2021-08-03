package rule;

import rule.opt.RuleOptHandler;

/**
 * 资料：正则表达式中的特殊字符（需要转义）
 * ^ 匹配输入字符串的开始位置，除非在方括号表达式中使用，此时它表示不接受该字符集合。要匹配 ^ 字符本身，请使用 \^。
 * $ 匹配输入字符串的结尾位置。如果设置了 RegExp 对象的 Multiline 属性，则 $ 也匹配 '\n' 或 '\r'。要匹配 $ 字符本身，请使用 \$。
 * . 匹配除换行符 \n 之外的任何单字符。要匹配 .，请使用 \。
 * \ 将下一个字符标记为或特殊字符、或原义字符、或后向引用、或八进制转义符。例如， 'n' 匹配字符 'n'。'\n' 匹配换行符。序列 '\\' 匹配 "\"，而 '\(' 则匹配 "("。
 * | 指明两项之间的一个选择。要匹配 |，请使用 \|。
 * { 标记限定符表达式的开始。要匹配 {，请使用 \{。
 * [ 标记一个中括号表达式的开始。要匹配 [，请使用 \[。
 * ( 和 ) 标记一个子表达式的开始和结束位置。子表达式可以获取供以后使用。要匹配这些字符，请使用 \( 和 \)。
 * * 匹配前面的子表达式零次或多次。要匹配 * 字符，请使用 \*。
 * + 匹配前面的子表达式一次或多次。要匹配 + 字符，请使用 \+。
 * ? 匹配前面的子表达式零次或一次，或指明一个非贪婪限定符。要匹配 ? 字符，请使用 \?。
 */
public class RuleTest {

    public static void main(String[] args) {
//        // [A, B, +, C, D, -, *]
//        System.out.println( RPN.rpn("((A+B))*(C-D)").toString());
//        // [A, B, +, C, *, D, -]
//        System.out.println( RPN.rpn("((A+B))*C-D").toString());
//        // [A, B, +, C, *, D, -]
//        System.out.println( RPN.rpn("(((A+B))*C)-D").toString());
//        // [A, !, B, C, &, |]
//        System.out.println( RPN.rpn("!A|B&C").toString());
//        //[A, B, !, |, C, |, D, E, ^, |]
//        System.out.println( RPN.rpn("A|!B|C|D^E").toString());

        test();
//        long start = System.currentTimeMillis();
//        // 模拟多个车
//        for (int i = 0; i < 200; i++) {
//            testC(10000,i+3);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println( " cost "+(end-start));
    }
    public static void testC(int capacity , int step ){
        // 模拟一个车的配置
        BitMap bitMap = new BitMap(capacity);
        // 此处的i为特征的ID，模拟10000个特征
        for (int i = 10; i < 10000; i+=step) {
            BitMapUtils.setBit(bitMap.getBits(),i);
        }
        BitMapUtils.setBit(bitMap.getBits(),3);
        long start = System.currentTimeMillis();
        int cnt = 0;
        // 模拟891多条规则
        for (int i = 1; i < 100; i++) {
            for (int j = 1; j < 10; j++) {
                String rule  = new StringBuilder().
                        append(1).append(i).append(j).append("&").
                        append(2).append(i).append(j).append("&").
                        append(3).append(i).append(j).append("&").
                        append(4).append(i).append(j).append("&").
                        append(5).append(i).append(j).append("&").
                        append(6).append(i).append(j).append("&").
                        append(7).append(i).append(j).append("&").
                        append(8).append(i).append(j).append("&").
                        append("(").append(9).append(i).append(j).append("|").append(i).append(j).append(")").
                        append("^F11").toString();
//                String rule  = "1"+i+""+j;
                RPN rpn = RPN.build(rule);
//                System.out.println(rpn.toString());
                Boolean aBoolean = RPN.handleRule(rpn, new RuleOptHandler(bitMap));
//                System.out.println(aBoolean +" "+rule);
                cnt++;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(cnt +" cost "+(end-start));
    }

    public static void test(){
        long start = System.currentTimeMillis();
        // 模拟具有10000个特征的车，通过byte[]数组下标为特征id构建位图
        System.out.println("模拟具有10000个特征的车");
        BitMap bitMap = new BitMap(10000);
        for (int i = 50; i < 10000; i+=6) {
            BitMapUtils.setBit(bitMap.getBits(),i);
        }
        BitMapUtils.setBit(bitMap.getBits(),3);
        // 目前支持&｜！^(),运算符可以通过OptHandler自定义
        String rule = "(F11|F22&311&(411|11))^F11";
        RPN rpn = RPN.build(rule);
        System.out.println("使用条件："+rule);
        System.out.println("泥波兰表达式："+rpn.toString());
        Boolean aBoolean = RPN.handleRule(rpn, new RuleOptHandler(bitMap));
        System.out.println("解析结果"+aBoolean );
        long end = System.currentTimeMillis();
        System.out.println(" cost "+(end-start));
    }
}
