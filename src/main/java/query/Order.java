package query;

/**
 * 排序
 */
public class Order {
    public final static String DIRECTION_ASC="asc";
    public final static String DIRECTION_DESC="desc";

    /**
     * 排名属性名
     */
    public String name;
    /**
     * 方向
     */
    public String direction="asc";


    /**
     * 多个表达式
     */
    public String values;

    public String alias="";

    public Order alias(String alias){
        this.alias = alias+".";
        return  this;
    }

    public Order(String name, String direction) {
        this.name = name;
        this.direction = direction;
    }

    public Order(String name) {
        this.name = name;
    }

    public Order() {
    }


    public static Order create(String name, String direction){
        return new Order(name,direction);
    }

    public static Order create(String name){
        return new Order(name);
    }

    public static Order createMulti(String values){
       Order order = new Order();
       order.values = values;
       return order;
    }

}
