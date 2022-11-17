package net.ahzz.share.sharejpa.query;

import net.ahzz.share.sharejpa.bean.Page;
import net.ahzz.share.sharejpa.bean.SpringBoot;
import net.ahzz.share.sharejpa.util.BaseUtil;
import net.ahzz.share.sharejpa.util.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.Set;

/**
 * 条件对象
 */
@Component
@Scope("prototype")
public class Condition {
    public static final Log log = LogFactory.getLog(Condition.class);
    EntityManager entityManager;
    public SpringQuery query;
    public Exprs<Expr,Connector> exprs = new Exprs<Expr,Connector>();
    public  Group group;
    public Order order;
    public JoinObjects<JoinObject> joinObjects = new JoinObjects<JoinObject>();
    public Page page;
    public Class entityClass;
    public  Having having;
    public  String alias;
    public SelectExprs<SelectExpr> selectExprs = new SelectExprs();
    public ExprGroups<ExprGroup> exprGroups = new ExprGroups();

    public  Condition(){

    }

    public Condition(Class entityClass){
        this.entityClass = entityClass;
    }


    public static Condition getInstance(Class entityClass){
        Condition condition = new Condition();
        condition.entityClass =entityClass;
        return condition;
    }

    public Condition add(Expr expr,Connector connector){
        exprs.put(expr,connector);
        return this;
    }

    public Condition add(Group group){
        this.group = group;
        return this;
    }

    public Condition add(ExprGroup exprGroup){
        exprGroups.add(exprGroup);
        return this;
    }

    /**
     * 添加一个查询条件
     * @param expr
     * @return
     */
    public Condition add(Expr expr){
        exprs.put(expr,Connector.getDefault());
        return this;
    }

    public Condition add(Expr expr,String connector){
        exprs.put(expr,new Connector(connector));
        return this;
    }

    /**
     * 添加排序
     * @param order
     * @return
     */
    public Condition add(Order order){
        this.order = order;
        return this;
    }

    public Condition add(Page page){
        this.page = page;
        return this;
    }

    public Condition add(JoinObject joinObject){
        this.joinObjects.add(joinObject);
        return this;
    }

    public Condition add(SelectExpr selectExpr){
        this.selectExprs.add(selectExpr);
        return this;
    }


    public void alias(String alias){
        this.alias = alias;
    }



    public Query getQuery() {
        Query query = getQueryWithoutPage();
        if(page !=null){
            query.setFirstResult(page.getStartRow());
            query.setMaxResults(page.getPageSize());
        }
        return query;
    }


    public Query getQueryWithoutPage() {
        StringBuffer sb = new StringBuffer();
        sb.append(_createSelectSql());
        if(!BaseUtil.empty(joinObjects)){
            sb.append(_createSqlFromJoinObjects());
        }
        sb.append(" where 1=1 ");

        if(!BaseUtil.empty(exprGroups)){
            sb.append(_createSqlFromExprGroups());
        }

        if(!BaseUtil.empty(exprs)){
            sb.append(_createSqlFromExprs());
        }
        if(group !=null){
            sb.append(_createSqlFromGroup());
        }
        if(having !=null){
            sb.append(_createSqlFromHaving());
        }
        if(order !=null){
            sb.append(_createSqlFromOrder());
        }
        log.info("whole sql  "+sb.toString());
        Query query = getEntityManager().createQuery(sb.toString());

        return query;
    }

    private EntityManager getEntityManager() {
        if(entityManager==null){
            entityManager = SpringBoot.getBean(EntityManager.class);
        }
        return entityManager;
    }

    private String _createSelectSql() {
        StringBuffer sb= new StringBuffer();
        if(!BaseUtil.empty(alias)){
            sb.append(" select "+alias+" from");
        }else{
            if(!BaseUtil.empty(selectExprs)){
                String select = _createSelectFromExprs();
                sb.append(" select " +select +" from ");
            }else {
                sb.append(" from ");
            }
        }
        log.info("entityClass in conditin "+entityClass);
        sb.append(ObjectUtil.getClassName(entityClass));
        if(!BaseUtil.empty(alias)){
            sb.append("  "+alias);
        }
        return  sb.toString();
    }


    private String _createSelectFromExprs() {
        StringBuffer sb = new StringBuffer();
        for (SelectExpr expr : selectExprs){
            sb.append(expr.toString()+",");
        }
        String sql = sb.substring(0,sb.length()-1);
        return sql;
    }

    private String _createSqlFromGroup() {
         String sql=" group by ";
         if(!BaseUtil.empty(group.values)){
             sql+= _getGroupValue(group);
         }else{
             sql+=group.alias+group.group;
         }
        return sql;
    }

    /**
     * 生成group by信息，由于jpa别名不起作用，这里进行转换
     * @param group
     * @return
     */
    private String _getGroupValue(Group group) {
        StringBuffer sb = new StringBuffer();
        String values[] = group.values.split(",");
        for(String val : values){
            sb.append(_getGroupValueFromSelectExpr(val));
            sb.append(",");
        }
        return BaseUtil.removeLastSymbol(sb.toString());
    }

    private String _getGroupValueFromSelectExpr(String val) {
        for(int i=0;i<selectExprs.size();i++){
            SelectExpr expr = selectExprs.get(i);
            if(expr.alias.equals(val)){
                val = "col_"+i+"_0_";
                break;
            }
        }
        return val;
    }

    /**
     * 创建having子句
     * @return
     */
    private String _createSqlFromHaving() {
        String sql=" having ";
        if (!BaseUtil.empty(having.exprs)){
            sql+=_createSqlFromExprs();
        }else if (!BaseUtil.empty(having.values)){
            sql+=having.values;
        }
        log.info("having sql "+sql );
        return  sql;
    }

    /**
     * 创建order子句
     * @return
     */
    private String _createSqlFromOrder() {
        String sql=" order by ";
        if(!BaseUtil.empty(order.values)){
            sql += order.values;
        }else if(!BaseUtil.empty(order.name)){
            sql+=order.alias+order.name+" "+order.direction;
        }
        log.info("order sql "+sql );
        return sql;
    }

    /**
     * 生成连接对象的sql
     * @return
     */
    private String _createSqlFromJoinObjects() {
        StringBuffer sb = new StringBuffer();
        for (JoinObject jo:joinObjects){
            sb.append(" " +jo.type+ " join " +jo.target+jo.prop+" "+BaseUtil.replace(jo.alias,".",""));
        }
        log.info(" join sql "+sb.toString() );
        return sb.toString();
    }

    /**
     * 根据表达式生成sql
     * @return
     */
    private String _createSqlFromExprs() {
        String sql = _createSqlFromExprs(exprs);
        log.info("exprs sql "+sql );
        return sql;
    }

    /**
     * 根据表达式生成sql
     * @return
     */
    private String _createSqlFromExprs(Exprs exprs) {
        StringBuffer sb = new StringBuffer();
        Set<Expr> keys = exprs.keySet();
        Iterator<Expr> it = keys.iterator();
        while (it.hasNext()){
            Expr expr = it.next();
            String value =expr.getValue();
            //    if(!BaseUtil.empty(value) || (Expr.OPT_IS_NULL.equals(expr.opt) ||Expr.OPT_IS_NOT_NULL.equals(expr.opt))){
            Connector connector = (Connector)exprs.get(expr);
            if(Expr.OPT_IS_NULL.equals(expr.opt) ||Expr.OPT_IS_NOT_NULL.equals(expr.opt)) {
                sb.append(" " + connector.value + " ");
                sb.append(" ( ");
                sb.append(expr.alias + expr.prop);
                sb.append(" " + expr.opt + " ");
                sb.append(" )");
            }else {
                if(!BaseUtil.empty(expr.prop)){
                    sb.append(" "+connector.value+" ");
                    sb.append(" ( ");
                    sb.append( expr.alias+expr.prop);
                    sb.append(" "+expr.opt+" ");
                    sb.append( value);
                    sb.append(" )");
                }
            }

        }
        log.info("exprs sql "+sb.toString() );
        return sb.toString();
    }

    private String _createSqlFromExprGroups() {
        StringBuffer sb= new StringBuffer();
        for(ExprGroup exprGroup : exprGroups){
            StringBuffer sb1= new StringBuffer();
            sb1.append(exprGroup.connector.value);
            sb1.append(" ( ");
            sb1.append(_createSqlFromExprs(exprGroup.exprs));
            sb1.append(" ) ");
            sb.append(sb1.toString()+" ");
        }
        return sb.toString();
    }


    /**
     * 清空查询条件
     */
    public void clear() {
        exprs.clear();
        joinObjects.clear();
    }

    /**
     * 判断当前condition是否存在count函数
     * @return
     */
    public boolean hasSelectCount() {
        for(SelectExpr expr :selectExprs){
            if(SelectExpr.FUNC_COUNT.equals(expr.func)){
                return true;
            }
        }
        return false;
    }
}
