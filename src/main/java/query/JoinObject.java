package query;

/**
 * 连接对象
 */
public class JoinObject {

    public  final  static  String JOIN_TYPE_LEFT="left";
    public  final  static  String JOIN_TYPE_RIGHT="right";

    /**
     * 属性名
     */
    public String prop;

    /**
     * 连接方式
     */
    public String type="left";

    /**
     * 别名
     */
    public String alias="";

    /**
     * 主体,即主表
     */
    public String target;


    public JoinObject(String prop) {
        this.prop = prop;
    }

    public JoinObject(String prop, String alias) {
        this.prop = prop;
        this.alias = alias;
    }

    public JoinObject(String prop, String alias, String type) {
        this.prop = prop;
        this.alias = alias;
        this.type = type;
    }

    public JoinObject(String prop, String target,String alias, String type) {
        this.prop = prop;
        this.target = target;
        this.alias = alias;
        this.type = type;
    }

    public JoinObject alias(String alias){
        this.alias = alias+".";
        return  this;
    }

    public JoinObject target(String target){
        this.target = target+".";
        return  this;
    }

    /**
     *
     * @param prop 属性
     * @param target 目标主体
     * @param alias 别名
     * @param type 连接类型
     * @return
     */
    public static JoinObject create(String prop,String target,String alias, String type){
        return new JoinObject(prop,target,alias,type);
    }

    /**
     * 生成连接对象
     * @param prop 连接对象的别名
     * @param target 目标主体
     * @param alias 连接类型
     * @return
     */
    public static JoinObject create(String prop,String target, String alias){
        return new JoinObject(prop,target,alias);
    }

    public static JoinObject create(String prop, String alias){
        return new JoinObject(prop,alias);
    }

    public static JoinObject create(String prop){
        return new JoinObject(prop);
    }


}
