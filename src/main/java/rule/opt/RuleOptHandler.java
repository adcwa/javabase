package rule.opt;

import rule.BitMap;
import rule.BitMapUtils;

import java.util.Arrays;
import java.util.List;

public class RuleOptHandler implements  OptHandler<Boolean> {

    private BitMap bitMap ;

    private static String F_TRUE = "_TRUE";
    private static String F_FALSE = "_FALSE";

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
            BitMapUtils.setBit(newBits,Integer.parseInt(v1));
        }
        if(!F_TRUE.equals(v2)){
            BitMapUtils.setBit(newBits,Integer.parseInt(v2));
        }
        List<Integer> match = BitMapUtils.match(bits, newBits);

        if(match.size()>0){
            // TODO 提示处理
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
            BitMapUtils.setBit(newBits,Integer.parseInt(v1));
            List<Integer> match2 = BitMapUtils.match(bits, newBits);
            result =  match2.size()==0?F_TRUE:F_FALSE;
        }else if(F_FALSE.equals(v1)){
            byte[] newBits = Arrays.copyOf(bits, bits.length);
            BitMapUtils.setBit(newBits,Integer.parseInt(v2));
            List<Integer> match = BitMapUtils.match(bits, newBits);
            result =  match.size()==0?F_TRUE:F_FALSE;
        }else{
            byte[] newBits = Arrays.copyOf(bits, bits.length);
            BitMapUtils.setBit(newBits,Integer.parseInt(v1));
            byte[] newBits2 = Arrays.copyOf(bits, bits.length);
            BitMapUtils.setBit(newBits2,Integer.parseInt(v2));

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
        int vi = Integer.parseInt(v);

        BitMapUtils.setBit(newBits, vi);

        List<Integer> match = BitMapUtils.match(bits, newBits);
        if(match.get(0) == vi ){
            return F_TRUE;
        }
        return F_FALSE;
    }

    private  String handlerXor(String v){
       System.out.println(String.format("^%s",v));

//        return String.format(" xor  %s",v);
        throw new RuntimeException("not support operate xor ");
    }

    @Override
    public Boolean finalResult(String v) {
        return F_TRUE.equals(v);
    }


}
