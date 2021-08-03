package rule;

import rule.opt.OptHandler;
import rule.opt.RuleOptHandler;

import java.util.*;

/**
 * 逆波兰表达式生成的array deque
 * <li><b>仅仅支持单字符运算符与小括号（不支持中括号、大括号）</b></li>
 * <li>可以自定义符号的优先级</li>
 *
 * @author 54117
 */
public class RPN {

    Queue<String> rpn = new ArrayDeque<>();

    private static final HashMap<String, Integer> prioritizedOps = new LinkedHashMap<>(16);

    // single operation
    private static final Set<String> singleOpt  = new HashSet<>();


    static {
        prioritizedOps.put("(", 1000);
        prioritizedOps.put(")", 1000);

        prioritizedOps.put("!", 1);
        singleOpt.add("!");
        prioritizedOps.put("^", 2);
        singleOpt.add("^");
        prioritizedOps.put("&", 3);
        prioritizedOps.put("|", 4);
        //        prioritizedOps.put("=>", 5);
        //        prioritizedOps.put("<=", 6);
        //        prioritizedOps.put("<=>", 7);
        prioritizedOps.put("*", 10);
        prioritizedOps.put("/", 10);
        prioritizedOps.put("+", 20);
        prioritizedOps.put("-", 20);

    }

    public static <T> T  handleRule(RPN rpn, OptHandler<T> optHandler){
        Queue<String> q = rpn.rpn;
        Stack<String> vs = new Stack<>();
        while(!q.isEmpty()){
            String v = q.poll();
            boolean isOpt = prioritizedOps.containsKey(v);
            if(isOpt){
                String v2 = vs.pop();
                String v1 = singleOpt.contains(v)?null:vs.pop();
                vs.push(optHandler.handler(v1,v2,v));
            }else{
                vs.push(v);
            }
        }
        String ret = vs.pop();
        if(ret == null ){
            throw new IllegalStateException("rule处理状态异常");
        }
        return optHandler.finalResult(ret);
    }

    public static RPN rpn(String rule) {
        if (null == rule || "".equals(rule)) {
            return null;
        }
        RPN re = new RPN();
        Queue<String> rpn = re.rpn;

        Stack<String> optStack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rule.length(); i++) {
            String s = String.valueOf(rule.charAt(i));
            boolean isOpt = prioritizedOps.containsKey(s);
            if (isOpt) {
                //
                if (sb.length() > 0) {
                    String preV = sb.toString();
                    sb.delete(0, sb.length());
                    rpn.offer(preV);
                }
                if ("(".equals(s)) {
                    optStack.push(s);
                } else if (")".equals(s)) {
                    while (!optStack.isEmpty()) {
                        String ss = optStack.pop();
                        if ("(".equals(ss)) {
                            break;
                        }
                        rpn.offer(ss);
                    }
                } else {
                    // 比较
                    Integer optP = prioritizedOps.getOrDefault(s, 10000);
                    if (!optStack.isEmpty()) {
                        //判断是否在比较的过程中入栈
                        boolean pushed = false;
                        while (!optStack.isEmpty()) {
                            Integer lstP = prioritizedOps.getOrDefault(optStack.peek(), 10000);
                            // 优先级高，则入栈
                            if (optP < lstP) {
                                optStack.push(s);
                                pushed = true;
                                break;
                            } else {
                                //否则退栈输出
                                rpn.offer(optStack.pop());
                            }
                        }
                        if (!pushed) {
                            optStack.push(s);
                        }
                    } else {
                        //栈为空，直接入栈
                        optStack.push(s);
                    }
                }
            } else {
                sb.append(s);
            }
        }
        if (sb.length() > 0) {
            String preV = sb.toString();
            rpn.offer(preV);
        }
        while (!optStack.isEmpty()) {
            rpn.offer(optStack.pop());
        }
        return re;
    }

    @Override
    public String toString() {
        return rpn.toString();
    }

}
