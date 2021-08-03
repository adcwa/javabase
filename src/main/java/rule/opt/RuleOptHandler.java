package rule.opt;

import rule.BitMap;
import rule.BitMapUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleOptHandler implements  OptHandler<Boolean> {

    private BitMap bitMap ;
    private static String F_TRUE = "_TRUE";
    private static String F_FALSE = "_FALSE";

    // 所有的特征
    private static Map<String,Integer> featureMap  = new HashMap<>();

    // 按特征族分组
    private static Map<String,Map<String,Integer>> groupByFamily = new HashMap<>();

    // 通过特征找到其特征族
    private static Map<String,String> featureParentMap  = new HashMap<>();

    static {
        // init test 特征数据 ,
        int id = 1;
        for (int i = 1; i < 60; i++) {
            String fm = "F"+i;
            Map<String,Integer> m = new HashMap<>();
            for (int j = 0; j < 20; j++) {
                String s = fm + j;
                m.put(s,id++);
                featureParentMap.put(s,fm);
            }
            groupByFamily.putIfAbsent(fm,new HashMap<>());
            groupByFamily.get(fm).putAll(m);
            featureMap.putAll(m);
        }
    }
    public static  Integer getId(String v ){
        return featureMap.containsKey(v)? featureMap.get(v):Integer.parseInt(v);
    }


    public RuleOptHandler(BitMap bitMap) {
        this.bitMap = bitMap;
    }

    @Override
    public  String handler(String v1, String v2, String opt){
        String result;
        if("&".equals(opt)){
            result = handlerAnd(v1,v2);
        }else if("|".equals(opt)){
            result = handlerOr(v1,v2);
        }else if("!".equals(opt)){
            result = handlerNot(v2);
        }else if("^".equals(opt)){
            result = handlerXor(v2);
        }else{
           result  = "unknow";
        }
//        System.out.println(String.format("%s %s %s = %s ",v1,opt,v2,result));
        return result;
    }

    private  String handlerAnd(String v1,String v2){

        if(F_FALSE.equals(v1)||F_FALSE.equals(v2)){
            return F_FALSE;
        }
        if(F_TRUE.equals(v1)&&F_TRUE.equals(v2)){
            return F_TRUE;
        }

        // 暂时直接认为v1、v2就是id，之后考虑将编码映射为数值

        // 配置表或者车型配置的bitmap表示
        byte[] bits = bitMap.getBits();

        byte[] newBits = Arrays.copyOf(bits, bits.length);
        if(!F_TRUE.equals(v1)){
            BitMapUtils.setBit(newBits,getId(v1));
        }
        if(!F_TRUE.equals(v2)){
            BitMapUtils.setBit(newBits,getId(v2));
        }
        List<Integer> match = BitMapUtils.match(bits, newBits);

        if(match.size()>0){
            return F_FALSE;
        }
        return F_TRUE;
    }

    private  String handlerOr(String v1,String v2){

        // 配置表或者车型配置的bitmap表示
        byte[] bits = bitMap.getBits();
        String result = F_FALSE;
        if(F_TRUE.equals(v1)||F_TRUE.equals(v2)){
            return F_TRUE;
        }else if(F_FALSE.equals(v1)&& F_FALSE.equals(v2)){
            result =  F_FALSE;
        }else if (F_FALSE.equals(v2)){
            byte[] newBits = Arrays.copyOf(bits, bits.length);
            BitMapUtils.setBit(newBits,getId(v1));
            List<Integer> match2 = BitMapUtils.match(bits, newBits);
            result =  match2.size()==0?F_TRUE:F_FALSE;
        }else if(F_FALSE.equals(v1)){
            byte[] newBits = Arrays.copyOf(bits, bits.length);
            BitMapUtils.setBit(newBits,getId(v2));
            List<Integer> match = BitMapUtils.match(bits, newBits);
            result =  match.size()==0?F_TRUE:F_FALSE;
        }else{
            byte[] newBits = Arrays.copyOf(bits, bits.length);
            BitMapUtils.setBit(newBits,getId(v1));
            byte[] newBits2 = Arrays.copyOf(bits, bits.length);
            BitMapUtils.setBit(newBits2,getId(v2));

            List<Integer> match = BitMapUtils.match(bits, newBits);
            List<Integer> match2 = BitMapUtils.match(bits, newBits2);

            if(match.size()==0 || match2.size()==0){
                result =  F_TRUE;
            }
        }
        return result ;
    }

    private   String handlerNot(String v){
        if(F_FALSE.equals(v)){
            return F_TRUE;
        }else if (F_TRUE.equals(v)){
            return F_FALSE;
        }

        // 配置表或者车型配置的bitmap表示
        byte[] bits = bitMap.getBits();

        byte[] newBits = Arrays.copyOf(bits, bits.length);
        int vi = getId(v);

        BitMapUtils.setBit(newBits, vi);

        List<Integer> match = BitMapUtils.match(bits, newBits);
        if(match.get(0) == vi ){
            return F_TRUE;
        }
        return F_FALSE;
    }

    private  String handlerXor(String v){
       if(F_TRUE.equals(v)){
           return F_FALSE;
       }else if (F_FALSE.equals(v)){
           return F_TRUE;
       }
        String parent = featureParentMap.get(v);
        if(null !=parent){
            Map<String, Integer> map = groupByFamily.get(parent);
            // 配置表或者车型配置的bitmap表示
            byte[] bits = bitMap.getBits();
            // 逐个特征的判断
            for (String s : map.keySet()) {
                byte[] newBits = Arrays.copyOf(bits, bits.length);
                int vi = map.get(s);
                BitMapUtils.setBit(newBits, vi);
                List<Integer> match = BitMapUtils.match(bits, newBits);
                if(s.equals(v )){
                    if(match.size()==0){
                        return F_FALSE;
                    }
                }else{
                    if(match.size()==0){
                        return F_TRUE;
                    }
                }
            }
        }
        return F_FALSE;
    }

    @Override
    public Boolean finalResult(String v) {
        return F_TRUE.equals(v);
    }


}
