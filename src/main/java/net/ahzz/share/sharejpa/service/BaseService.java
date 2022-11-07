package net.ahzz.share.sharejpa.service;

import net.ahzz.share.sharejpa.bean.Page;
import net.ahzz.share.sharejpa.bean.ResultInfo;
import net.ahzz.share.sharejpa.bean.SpringBoot;
import net.ahzz.share.sharejpa.util.BaseUtil;
import net.ahzz.share.sharejpa.util.ObjectUtil;
import net.ahzz.share.sharejpa.util.TimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import query.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class BaseService<T,I> {

    public static final Log log = LogFactory.getLog(BaseService.class);
    private T entity;
    private Class<T> entityClass;
    private JpaRepository<T, Long> dao;
    @Autowired
    public EntityManager entityManager;
    public Condition condition;
    @Autowired
    public MessageSource messageSource;

    public BaseService(){

    }

    public BaseService(JpaRepository dao) {
        this.dao = dao;
        entityClass = (Class<T>) ObjectUtil.getFXClass(this.getClass(), 0);
    }


    public synchronized Condition getCondition(){
        return Condition.getInstance(entityClass);
    }

    public T save(T entity) {
        entity =  dao.save(entity);
        return entity;
    }

    public T update(T entity) {
        try {
            Long id = (Long) ObjectUtil.getEntityId(entity);
            T obj = get(id);
            T old = entityClass.newInstance();
            BeanUtils.copyProperties(obj,old);
            obj = (T) ObjectUtil.exchange(entity, obj);
            entity = dao.save(obj);
        }catch(Exception e){
               e.printStackTrace();
        }
        return entity;
    }

    public T update(T entity,String ... ignoreField) {
        try {
            Long id = (Long) ObjectUtil.getEntityId(entity);
            T obj = get(id);


            obj = (T) ObjectUtil.exchange(entity, obj,ignoreField);
            entity = dao.save(obj);
        }catch(Exception e){
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 根据Id删除
     * @param id
     * @return
     */
    public boolean deleteById(Long id){
        boolean isSuccess = false;
        try {
            dao.deleteById(id);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    public T find(T entity){
        Example<T> example = Example.of(entity);
        Optional<T> optiona = dao.findOne(example);
        if(optiona.isPresent()){
            return optiona.get();
        }
        return null;
    }

    public T findById(Long id){
        Optional<T> optional = dao.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    public List<T> findAll(T entity){
        Example<T> example = Example.of(entity);
        return  (List<T> )dao.findAll(example);
    }



    public boolean remove(T entity) {
        try {
            dao.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public T get(Long id) {
        if(BaseUtil.fullNotEmpty(id)){
            Optional<T> optional = dao.findById(id);
            if(optional!=null && optional.isPresent()){
                return optional.get();
            }
        }
        return null;
    }

    public List<T> getAll() {
        return dao.findAll();
    }


    public List<T> getAll(Page page) {
        Pageable pageable = PageRequest.of(page.getCurrentPage(), page.getPageSize());
        List<T> list = dao.findAll(pageable).getContent();
        return list;
    }

    public Page<T> getPageAll(Page<T> page) {
        Pageable pageable = PageRequest.of(page.getCurrentPage(), page.getPageSize());
        org.springframework.data.domain.Page spage = dao.findAll(pageable);
        long counts = counts();
        page.setTotalCounts(counts);
        page.setList(spage.getContent());
        return page;
    }

    public long counts() {
        return dao.count();
    }

    public long counts1() {
        return dao.count();
    }


    @Transactional
    public ResultInfo remove(String ids, String split) {
        ResultInfo info = new ResultInfo();
        if (BaseUtil.notEmpty(ids, split)) {
            String[] idss = ids.split(split);
            for (String id : idss) {
                dao.deleteById(Long.valueOf(id));
                info.success = true;
            }
        }
        return info;
    }

    /**
     * 执行更新操作
     *
     * @param sql
     * @return
     */
    @Transactional
    public boolean executeUpdate(String sql) {

        try {
            entityManager.createNativeQuery(sql).executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 执行update,返回受影响的数量
     * @param sql
     * @return
     */
    @Transactional
    public int executeUpdateWithCounts(String sql) {
        return entityManager.createNativeQuery(sql).executeUpdate();
    }


    /**
     * 根据查询条件进行查询
     * @param query
     * @return
     */
    public List<T> findAll(SpringQuery query){
        if(query !=null){
            if(query.example !=null && query.sort !=null){
                return dao.findAll(query.example,query.sort);
            }else if (query.example !=null && query.pageable !=null){
                return dao.findAll(query.example,query.pageable).getContent();
            }else if (query.example!=null){
                return  dao.findAll(query.example);
            }else if(query.pageable !=null){
                return  dao.findAll(query.pageable).getContent();
            }else if(query.sort !=null){
                return  dao.findAll(query.sort);
            }else {
                return  dao.findAll();
            }
        }
        return  dao.findAll();
    }

    /**
     * 根据Condition查询条件封装查询
     * @param con 查询条件对象
     * @return
     */
    public List<T> findAll(Condition con){
        if(con !=null){
            if(con.query!=null){
                return findAll(con.query);
            }else{
                Query query = con.getQuery();
                return query.getResultList();
            }
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * 查询分页的内容
     * @param con
     * @return
     */
    public Page<T> findPageAll(Condition con){
        if(con.page !=null){
            List<T> list = findAll(con);
            con.page.setList(list);
            //为了查询数量，修改查询的condition
            con.add(SelectExpr.count());
            long counts = findCount(con);
            con.page.setTotalCounts(counts);
            return  con.page;
        }
        return new Page<>();
    }

    /**
     * 得到资源文件内容
     * @param key
     * @return
     */
    public String getMessage(String key) {
        return messageSource.getMessage(key,null, Locale.getDefault());
    }

    /**
     * 根据查询条件，查询一条记录，如果有多条记录，选择第一条
     * @param condition
     * @return
    */
    public T findOne(Condition condition) {
        List<T> list = findAll(condition);
        if(!BaseUtil.empty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据sql查询，得到指定的类型
     * @param sql
     * @param clazz 要返回的指定的类型
     * @return
     */
    public List findAll(String sql,Class clazz){
        return  entityManager.createNativeQuery(sql,clazz).getResultList();
    }


    /**
     * 保存所有对象
     * @param list
     */
    @Transactional
    public List<T> saveAll(List<T> list) {
       return dao.saveAll(list);
    }

    /**
     * 更新所有对象
     * @param list
     */
    @Transactional
    public List<T> updateAll(List<T> list) {
        return dao.saveAll(list);
    }

    /**
     * 得到所有对象列表
     * @param ids 对象的id,以逗号隔开
     * @return
     */
    public List<T> findAllByIds(String ids){
        Condition condition = getCondition();
        String[] idarr = ids.split(",");
        if(!BaseUtil.empty(idarr)){
            Long[] nidsa = BaseUtil.convertStringToLong(idarr);
            condition.add(Expr.in("id",nidsa));
            return findAll(condition);
        }
        return BaseUtil.emptyList();

    }



    /**
     * 得到所有对象列表
     * @param ids 对象的id,以逗号隔开
     * @return
     */
    public List<T> findAllByIds(Long[] ids){
        Condition condition = getCondition();
        if(!BaseUtil.empty(ids)){
            condition.add(Expr.in("id",ids));
            return findAll(condition);
        }
        return BaseUtil.emptyList();

    }

    public List<T> findAllByIds(List<BigInteger> ids){
        Condition condition = getCondition();
        if(!BaseUtil.empty(ids)){
            condition.add(Expr.in("id",ids));
            return findAll(condition);
        }
        return BaseUtil.emptyList();

    }

    public ResultInfo exist(String field, String fieldValue) {
        ResultInfo info = new ResultInfo();
        Condition condition = getCondition();
        condition.add(Expr.eq(field,fieldValue));
        List<T> list = findAll(condition);
        if(!BaseUtil.empty(list)){
            info.success = true;
        }
        return info;
    }

    /**
     * 查询记录的条数
     * @param condition
     * @return
     */
    public long findCount(Condition condition) {
        Query query = condition.getQueryWithoutPage();
        Object obj = query.getSingleResult();
        if(obj instanceof Long){
            return (Long)obj;
        }
        return 0;
    }

    /**
     * 查询记录的条数
     * @param condition
     * @return
     */
    public Object[] findByFunc(Condition condition) {
        Query query = condition.getQuery();
        return (Object[])query.getSingleResult();
    }


    @Transactional
    public ResultInfo enable(String ids) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                sb.append(" set enable='Y' ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    @Transactional
    public ResultInfo disable(String ids) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                sb.append(" set enable='N' ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    protected void removeAll(String table,String prop, Object value) {
        String sql = " delete from "+table+" where "+prop+" = :value";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("value",value);
        query.executeUpdate();
    }

    @Transactional
    public ResultInfo agree(String ids,long userid,String userName) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                if(ObjectUtil.hasField(entityClass,"checkStatus")){
                    sb.append(" set checkStatus='24' ,");
                    if(ObjectUtil.hasField(entityClass,"status")){
                        sb.append("  status='2' ,");
                    }

                }else if(ObjectUtil.hasField(entityClass,"status")){
                    sb.append(" set  status='2' ,");
                }
                if(ObjectUtil.hasField(entityClass,"operateTime")){
                    sb.append(" operateTime= '"+ TimeUtil.getCurrentTime()+"',");
                }
                if(ObjectUtil.hasField(entityClass,"operatorId")&&BaseUtil.fullNotEmpty(userid)){
                    sb.append(" operatorId= "+ userid+" ,");
                }

                if(ObjectUtil.hasField(entityClass,"operatorName")&&!BaseUtil.empty(userName)){
                    sb.append(" operatorName= '"+ userName+"',");
                }
                sb.append(" id=id ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 同意
     * @param ids
     * @param userid
     * @param userName
     * @param status 要设置的状态数值
     * @return
     */
    @Transactional
    public ResultInfo agree(String ids,long userid,String userName,String status) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                if(ObjectUtil.hasField(entityClass,"checkStatus")){
                    sb.append(" set checkStatus='2' ,");
                    if(ObjectUtil.hasField(entityClass,"status")){
                        sb.append("  status='2' ,");
                    }

                }else if(ObjectUtil.hasField(entityClass,"status")){
                    sb.append(" set  status='2' ,");
                }
                if(ObjectUtil.hasField(entityClass,"operateTime")){
                    sb.append(" operateTime= '"+ TimeUtil.getCurrentTime()+"',");
                }
                if(ObjectUtil.hasField(entityClass,"operatorId")&&BaseUtil.fullNotEmpty(userid)){
                    sb.append(" operatorId= "+ userid+" ,");
                }

                if(ObjectUtil.hasField(entityClass,"operatorName")&&!BaseUtil.empty(userName)){
                    sb.append(" operatorName= '"+ userName+"',");
                }
                sb.append(" id=id ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     *  上架，默认会更新putStatus=4，如果没有putStatus，则更新status=4
     * @param ids
     * @return
     */
    @Transactional
    public ResultInfo putOn(String ids,long userid,String userName) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                if(ObjectUtil.hasField(entityClass,"checkStatus")){
                    sb.append(" set checkStatus='4' ,");
                    if(ObjectUtil.hasField(entityClass,"status")){
                        sb.append("  status='4' ,");
                    }

                }else if(ObjectUtil.hasField(entityClass,"status")){
                    sb.append(" set  status='4' ,");
                }
                if(ObjectUtil.hasField(entityClass,"operateTime")){
                    sb.append(" operateTime= '"+ TimeUtil.getCurrentTime()+"',");
                }
                if(ObjectUtil.hasField(entityClass,"operatorId")&&BaseUtil.fullNotEmpty(userid)){
                    sb.append(" operatorId= "+ userid+" ,");
                }
                if(ObjectUtil.hasField(entityClass,"upTime")){
                    sb.append(" upTime= "+ TimeUtil.getCurrentTime()+" ,");
                }

                if(ObjectUtil.hasField(entityClass,"operatorName")&&!BaseUtil.empty(userName)){
                    sb.append(" operatorName= '"+ userName+"',");
                }
                sb.append(" id=id ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     *  下架，默认会更新putStatus=5，如果没有putStatus，则更新status=5
     * @param ids
     * @return
     */
    @Transactional
    public ResultInfo putDown(String ids,long userid,String userName) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                if(ObjectUtil.hasField(entityClass,"checkStatus")){
                    sb.append(" set checkStatus='5' ,");
                    if(ObjectUtil.hasField(entityClass,"status")){
                        sb.append("  status='5' ,");
                    }

                }else if(ObjectUtil.hasField(entityClass,"status")){
                    sb.append(" set  status='5' ,");
                }
                if(ObjectUtil.hasField(entityClass,"operateTime")){
                    sb.append(" operateTime= '"+ TimeUtil.getCurrentTime()+"',");
                }
                if(ObjectUtil.hasField(entityClass,"downTime")){
                    sb.append(" downTime= "+ TimeUtil.getCurrentTime()+" ,");
                }
                if(ObjectUtil.hasField(entityClass,"operatorId")&&BaseUtil.fullNotEmpty(userid)){
                    sb.append(" operatorId= "+ userid+" ,");
                }

                if(ObjectUtil.hasField(entityClass,"operatorName")&&!BaseUtil.empty(userName)){
                    sb.append(" operatorName= '"+ userName+"',");
                }
                sb.append(" id=id ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     *  审批不同意，默认会更新checkStatus=3，如果没有checkStatus，则更新status=3
     * @param ids
     * @return
     */
    @Transactional
    public ResultInfo disagree(String ids,long userid,String userName,String reason) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                if(ObjectUtil.hasField(entityClass,"checkStatus")){
                    sb.append(" set checkStatus='3' ,");
                    if(ObjectUtil.hasField(entityClass,"status")){
                            sb.append("  status='3' ,");
                    }

                }else if(ObjectUtil.hasField(entityClass,"status")){
                    sb.append(" set  status='3' ,");
                }
                if(ObjectUtil.hasField(entityClass,"operateTime")){
                    sb.append(" operateTime= '"+ TimeUtil.getCurrentTime()+"',");
                }
                if(ObjectUtil.hasField(entityClass,"operatorId")&&BaseUtil.fullNotEmpty(userid)){
                    sb.append(" operatorId= "+ userid+" ,");
                }

                if(ObjectUtil.hasField(entityClass,"operatorName")&&!BaseUtil.empty(userName)){
                    sb.append(" operatorName= '"+ userName+"',");
                }
                if(ObjectUtil.hasField(entityClass,"reason")&&!BaseUtil.empty(reason)){
                    sb.append(" reason= '"+ reason+"' ,");
                }
                sb.append(" id=id ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     *  审批不同意，默认会更新checkStatus=3，如果没有checkStatus，则更新status=3
     * @param ids
     * @return
     */
    @Transactional
    public ResultInfo disagree(String ids,long userid,String userName,String reason,String status) {
        ResultInfo info = new ResultInfo();
        try {
            if(!BaseUtil.empty(ids)){
                List<Long> idss =BaseUtil.createIdList(ids);
                StringBuffer sb = new StringBuffer();
                sb.append(" update ");
                sb.append(ObjectUtil.getClassName(entityClass));
                if(ObjectUtil.hasField(entityClass,"checkStatus")){
                    sb.append(" set checkStatus='3' ,");
                    if(ObjectUtil.hasField(entityClass,"status")){
                        if(!BaseUtil.empty(status)){
                            sb.append("  status='"+status+"' ,");
                        }else {
                            sb.append("  status='3' ,");
                        }
                    }

                }else if(ObjectUtil.hasField(entityClass,"status")){
                    if(!BaseUtil.empty(status)){
                        sb.append(" set  status='"+status+"' ,");
                    }else {
                        sb.append(" set  status='3' ,");
                    }
                }

                if(ObjectUtil.hasField(entityClass,"operateTime")){
                    sb.append(" operateTime= '"+ TimeUtil.getCurrentTime()+"',");
                }
                if(ObjectUtil.hasField(entityClass,"operatorId")&&BaseUtil.fullNotEmpty(userid)){
                    sb.append(" operatorId= "+ userid+" ,");
                }

                if(ObjectUtil.hasField(entityClass,"operatorName")&&!BaseUtil.empty(userName)){
                    sb.append(" operatorName= '"+ userName+"',");
                }
                if(ObjectUtil.hasField(entityClass,"reason")&&!BaseUtil.empty(reason)){
                    sb.append(" reason= '"+ reason+"' ,");
                }
                sb.append(" id=id ");
                sb.append(" where id in (:ids)");
                Query query = entityManager.createQuery(sb.toString());
                query.setParameter("ids",idss);
                query.executeUpdate();

                info.success = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 得到数据库第一条记录的id
     * @return
     */
    public long getFirstId(){
        T entity = getFirst();
        if(entity!=null){
            return ObjectUtil.getEntityLongId(entity);
        }
        return 0;
    }

    /**
     * 得到数据库第一条记录
     * @return
     */
    public T getFirst() {
        List<T> list = getAll();
        if(!BaseUtil.empty(list)){
           return   list.get(0);
        }
        return null;
    }

    /**
     * 根据parent字段，得到当前对象的子列表数据
     * @param parent
     * @return
     */
    public  List<T> getListByParent(long parent) {
        Condition condition = getCondition();
        condition.add(Expr.eq("parent",parent));
        return findAll(condition);
    }

    public  List<T> getListByParent(long parent,boolean enable) {
        Condition condition = getCondition();
        condition.add(Expr.eq("parent",parent));
        condition.add(Expr.eq("enable","Y"));
        return findAll(condition);
    }



    /**
     * 这个是用来封装获取业务对象的方法
     * @param bcode
     * @param bid
     * @return
     */
    public Object getBobj(String bcode,Long bid){
        String service = BaseUtil.toFirstUpperStr(bcode)+"Service";
        Object serviceObj = SpringBoot.getBean(service);
        Object entity = ObjectUtil.executeMethod(serviceObj.getClass(),serviceObj,"get",new Object[]{bid});
        return entity;
    }

    /**
     * 执行业务方法
     * @param bcode
     * @param method
     * @param params
     * @return
     */
    public Object excuteBusinessMethod(String bcode,String method,Object[] params){
        String service = BaseUtil.toFirstUpperStr(bcode)+"Service";
        Object serviceObj = SpringBoot.getBean(service);
        return ObjectUtil.executeMethod(serviceObj.getClass(),serviceObj,method,params);
    }

    /**
     * 给包含bid,bcode的对象，包装oboj
     * @param list
     * @return
     */
    public List<T> wrapBobj(List<T> list){
        if(!BaseUtil.empty(list)){
            try {
                for (T obj : list) {
                    Long bid = (Long)ObjectUtil.getFieldValue("bid", obj);
                    String bcode = (String)ObjectUtil.getFieldValue("bcode", obj);
                    Object bobj = getBobj(bcode,bid);
                    ObjectUtil.setFieldValue(obj,"bobj",bobj);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 查询记录的条数
     * @param condition
     * @return
     */
    public List<Object[]> findListByFunc(Condition condition) {
        Query query = condition.getQuery();
        return  query.getResultList();
    }

    /**
     * 更新所有数据，调用sql 操作
     *
     * @param bean
     */
    @Transactional(readOnly = false)
    protected void updateAllByIds(OperateBean bean) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update ");
        sb.append(bean.tableName);
        sb.append(" set ");
        sb.append(bean.field);
        sb.append("=");
        sb.append(bean.value);
        sb.append(" where id in (");
        String[] idarr = bean.ids.split(",");
        for (String id : idarr) {
            sb.append(id + ",");
        }
        String sql = sb.substring(0, sb.length() - 1);
        sql += ")";
        log.info("sql = " + sql);
        this.executeUpdate(sql);
    }

    public boolean falseDelete(Long id) {
        T entity = get(id);
        if (entity != null) {
            if (ObjectUtil.hasField(entityClass, "delFlag")) {
                ObjectUtil.setFieldValue(entity, "delFlag", 1);
                update(entity);
            }
        }
        return false;
    }


}
