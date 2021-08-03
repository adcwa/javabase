package rule.opt;

import rule.BitMap;

public class RuleOptHandler implements  OptHandler {

    private BitMap bitMap ;

    public RuleOptHandler(BitMap bitMap) {
        this.bitMap = bitMap;
    }

    @Override
    public  String  handler(String v1, String v2, String opt){
        String result = null;
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
        return result;
    }

    private  String handlerAnd(String v1,String v2){
        System.out.println(String.format("%s & %s",v1,v2));
        return String.format(" %s  and %s ",v1,v2);
    }

    private  String handlerOr(String v1,String v2){
        System.out.println(String.format(" %s | %s",v1,v2));
        return String.format(" %s  or %s ",v1,v2);
    }

    private   String handlerNot(String v){
        System.out.println(String.format(" !%s ",v));
        return String.format(" not %s ",v);
    }

    private  String handlerXor(String v){
        System.out.println(String.format("^%s",v));
        return String.format(" xor  %s",v);
    }



}
