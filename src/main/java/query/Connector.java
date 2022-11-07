package query;

/**
 * 连接符
 */
public class Connector {
    public static final String CONT_AND="and";
    public static final String CONT_OR="or";

    public  String value="and";

    public static Connector getDefault(){
        Connector connector = new Connector();
        return connector;
    }

    public static Connector getEmpty(){
        Connector connector = new Connector();
        connector.value="";
        return connector;
    }

    public Connector(String cont){
        this.value = cont;
    }

    public Connector(){
    }


}
