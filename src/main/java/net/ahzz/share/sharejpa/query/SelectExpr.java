package net.ahzz.share.sharejpa.query;


import net.ahzz.share.sharejpa.util.BaseUtil;

/**
 *
 * 选择的表达式，即出现在select 后面的表达式
 * meimei
 * 2020/8/14
 */
public class SelectExpr {
    public  final  static String FUNC_COUNT="count";
    public  final  static String FUNC_SUM="sum";
    public  final  static String FUNC_AVG="avg";
    public  final  static String FUNC_MAX="max";
    public  final  static String FUNC_MIN="min";
    public  final  static String FUNC_DISTINCT="DISTINCT";
    public  final  static String FUNC_SUBSTR="SUBSTRING";

    public String func;
    public String field;
    public String alias;
    public int length;


    public SelectExpr(String func,String field){
        this.func = func;
        this.field = field;
    }

    public SelectExpr(String func,String field,String alias){
        this.func = func;
        this.field = field;
        this.alias = alias;
    }

    public static SelectExpr create(String func,String field){
        return new SelectExpr(func,field,field);
    }

    public static SelectExpr count(){
        return new SelectExpr(FUNC_COUNT,"*","alias");
    }

    public static SelectExpr month(String field,int length){
        SelectExpr selectExpr =  new SelectExpr(FUNC_SUBSTR,field);
        selectExpr.length = length;
        selectExpr.alias = field;
        return selectExpr;
    }

    public static SelectExpr month(String field,int length,String alias){
        SelectExpr selectExpr =  new SelectExpr(FUNC_SUBSTR,field);
        selectExpr.length = length;
        selectExpr.alias = alias;
        return selectExpr;
    }

    public static SelectExpr day(String field,int length,String alias){
        SelectExpr selectExpr =  new SelectExpr(FUNC_SUBSTR,field);
        selectExpr.length = length;
        selectExpr.alias = alias;
        return selectExpr;
    }

    public static SelectExpr field(String field){
        return new SelectExpr("",field,field);
    }
    public static SelectExpr field(String field,String alias){
        return new SelectExpr("",field,alias);
    }

    public static SelectExpr count(String field){
        return new SelectExpr(FUNC_COUNT,field,field);
    }
    public static SelectExpr count(String field,String alias){
        return new SelectExpr(FUNC_COUNT,field,alias);
    }

    public static SelectExpr sum(String field){
        return new SelectExpr(FUNC_SUM,field,field);
    }

    public static SelectExpr sum(String field,String alias){
        return new SelectExpr(FUNC_SUM,field,alias);
    }

    public static SelectExpr max(String field){
        return new SelectExpr(FUNC_MAX,field,field);
    }

    public static SelectExpr max(String field,String alias){
        return new SelectExpr(FUNC_MAX,field,alias);
    }

    public static SelectExpr min(String field){
        return new SelectExpr(FUNC_MIN,field,field);
    }

    public static SelectExpr min(String field,String alias){
        return new SelectExpr(FUNC_MIN,field,alias);
    }

    public static SelectExpr avg(String field){
        return new SelectExpr(FUNC_AVG,field,field);
    }
    public static SelectExpr avg(String field,String alias){
        return new SelectExpr(FUNC_AVG,field,alias);
    }

    public static SelectExpr distinct(String field){
        return new SelectExpr(FUNC_DISTINCT,field);
    }

    public String toString(){
        StringBuffer sb  = new StringBuffer();
        if(!BaseUtil.empty(func)){
            sb.append(func);
            sb.append("(");
            sb.append(field);
            if(FUNC_SUBSTR.equals(func)){
                sb.append(",1,");
                sb.append(length);
            }
            sb.append(")");
        }else {
            sb.append(field);
        }
        if(!BaseUtil.empty(alias)){
            sb.append(" as "+alias);
        }
        return sb.toString();
    }


}
