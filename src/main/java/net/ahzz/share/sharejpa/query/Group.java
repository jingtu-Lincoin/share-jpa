package net.ahzz.share.sharejpa.query;

/**
 * 分组
 */
public class Group {
    /**
     * 分组名称，多个以逗号隔开
     */
    public String values;

    /**
     * 单个分组名称
     */
    public  String group;

    /**
     * 别名
     */
    public String alias="";

    public Group alias(String alias){
        this.alias = alias+".";
        return  this;
    }

    public Group(String values){
        this.values = values;
    }

    public static Group values(String values){
        return new Group(values);
    }

}
