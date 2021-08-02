package rule;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RuleExtractor {

//    Stack<String> valueStack = new Stack<>();
//    Stack<String> optStack = new Stack<>();
    Stack<String> npr = new Stack<>();

    public static String REG = "[{|}|\\[|\\]|(|)|&|\\|]|<=>|=>|<=";

    public static HashMap<String, Integer> prioritizedOps = new LinkedHashMap<>(8);

    static {
        prioritizedOps.put("(", 0);
        prioritizedOps.put(")", 0);
//        prioritizedOps.put("[", 0);
//        prioritizedOps.put("{", 0);
//        prioritizedOps.put("}", 0);
//        prioritizedOps.put("!",1);
//        prioritizedOps.put("^",2);
        prioritizedOps.put("&", 3);
        prioritizedOps.put("|", 4);
        prioritizedOps.put("=>", 5);
        prioritizedOps.put("<=", 6);
        prioritizedOps.put("<=>", 7);
    }

    public static boolean isOpt(String opt) {
        return prioritizedOps.containsKey(opt);
    }

    public static RuleExtractor npr(String rule) {
        if (null == rule || "".equals(rule)) {
            return null;
        }
        RuleExtractor re = new RuleExtractor();
        String[] members = rule.split(REG);

        // map the member
        LinkedHashMap<String, Boolean> memberMap =
                Arrays.stream(members).filter(v -> null == v || "".equals(v)).
                        collect(LinkedHashMap<String, Boolean>::new, (map, v) -> map.put(v, Boolean.TRUE), LinkedHashMap::putAll);
        int lastVIdx = 0;
        // 找到运算分量
        // 找到运算符
        //9

        for (String v : memberMap.keySet()) {
            if (null == v || "".equals(v)) {
                continue;
            }
            int idx = rule.indexOf(v);
            re.npr.push(v);
            if (idx > 0) {
                String opts = rule.substring(lastVIdx + 1, idx);
                if (prioritizedOps.containsKey(opts)) {
                    re.npr.push(opts);
                } else {
                    for (int i = opts.length() - 1; i >= 0; i--) {
                        String c = String.valueOf(opts.charAt(i));
                        if (prioritizedOps.containsKey(c)) {
                            re.npr.push(c);
                        }
                    }
                }
            }
            lastVIdx = idx;
        }
        return re;
    }

//    public static RuleExtractor buildOptValSplit(String rule) {
//        if (null == rule || "".equals(rule)) {
//            return null;
//        }
//        RuleExtractor re = new RuleExtractor();
//
//
//        String[] members = rule.split(REG);
//
//        int lastVIdx = 0;
//        for (String v : members) {
//            if (null == v || "".equals(v)) {
//                continue;
//            }
//            int idx = rule.indexOf(v);
//            re.valueStack.push(v);
//            if (idx > 0) {
//                String opts = rule.substring(lastVIdx + 1, idx);
//                if (prioritizedOps.containsKey(opts)) {
//                    re.optStack.push(opts);
//                } else {
//                    for (int i = 0; i < opts.length(); i++) {
//                        String c = String.valueOf(opts.charAt(i));
//                        if (prioritizedOps.containsKey(c)) {
//                            re.optStack.push(c);
//                        }
//                    }
//                }
//            }
//            lastVIdx = idx;
//        }
//        return re;
//    }

//    public static RuleExtractor build(String rule) {
//        if (null == rule || "".equals(rule)) {
//            return null;
//        }
//        RuleExtractor re = new RuleExtractor();
//        String[] members = rule.split(REG);
//
//        // map the member
//        LinkedHashMap<String, Boolean> memberMap =
//                Arrays.stream(members).filter(v -> null == v || "".equals(v)).
//                        collect(LinkedHashMap<String, Boolean>::new, (map, v) -> map.put(v, Boolean.TRUE), LinkedHashMap::putAll);
//        int lastVIdx = 0;
//        for (String v : memberMap.keySet()) {
//            if (null == v || "".equals(v)) {
//                continue;
//            }
//            int idx = rule.indexOf(v);
//            re.npr.push(v);
//            if (idx > 0) {
//                String opts = rule.substring(lastVIdx + 1, idx);
//                if (prioritizedOps.containsKey(opts)) {
//                    re.npr.push(opts);
//                } else {
//                    for (int i = opts.length() - 1; i >= 0; i--) {
//                        String c = String.valueOf(opts.charAt(i));
//                        if (prioritizedOps.containsKey(c)) {
//                            re.npr.push(c);
//                        }
//                    }
//                }
//            }
//            lastVIdx = idx;
//        }
//        return re;
//    }

    public String toString() {
        return  npr.toString();

    }

}
