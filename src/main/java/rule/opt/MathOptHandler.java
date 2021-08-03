package rule.opt;

import rule.BitMap;

public class MathOptHandler implements  OptHandler {

    private BitMap bitMap ;

    public MathOptHandler(BitMap bitMap) {
        this.bitMap = bitMap;
    }

    @Override
    public  String  handler(String v1, String v2, String opt){
        String result = null;
        if("+".equals(opt)){
            result =  handlerPlus(v1,v2);
        }else{
            // todo
            result  = String.format("%s %s %s",v1,opt,v2);
        }
        return result;
    }

    private   String handlerPlus(String v1,String v2){
        System.out.println(String.format("%s  +  %s",v1,v2));
        return "%s  + %s";
    }





}
