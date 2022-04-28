package net.ahzz.share.sharejpa.query;

/**
 * 对条件进行分组
 * meimei
 * 2021-09-02
 */
public class ExprGroup {
    //当前条件组里的几个条件
    Exprs exprs = new Exprs();
    //当前条件组与其他条件的连接关系
    Connector connector;

    public ExprGroup(){

    }
    public ExprGroup(Connector connector){
        this.connector = connector;
    }
    public ExprGroup(String cont){
        this.connector = new Connector(cont);
    }

    public ExprGroup add(Expr expr,Connector connector){
        exprs.put(expr,connector);
        return this;
    }


    public ExprGroup add(Expr expr){
        exprs.put(expr,Connector.getDefault());
        return this;
    }

    public ExprGroup add(Expr expr,String connector){
        exprs.put(expr,new Connector(connector));
        return this;
    }
}
