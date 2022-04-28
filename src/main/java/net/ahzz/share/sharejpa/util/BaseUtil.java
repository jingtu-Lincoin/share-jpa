package net.ahzz.share.sharejpa.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 公共的Util类
 * 
 * @author 呼唤
 * 
 */
public class BaseUtil {
	
	protected static Log log = LogFactory.getLog(BaseUtil.class);
	private static SecureRandom random = new SecureRandom();

	/**
	 * 判断字符是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean empty(String s) {
		return s == null || "".equals(s) || s.trim().equals("")
				|| "null".equalsIgnoreCase(s)
				|| "undefined".equalsIgnoreCase(s);

	}

	public static boolean empty(Object s) {
	    if(s !=null){
	        return empty(String.valueOf(s));
        }
        return  true;
	}
	
	/**
	 * 判断字符是否为空或为0
	 * 
	 * @param s
	 * @return
	 */
	public static boolean emptyOrZero(String s) {
		return s == null || "".equals(s) || s.trim().equals("")
				|| "null".equalsIgnoreCase(s) || "0".equals(s)
				|| "undefined".equalsIgnoreCase(s);

	}
	
	/**
	 * 判断多个字符串是否为空
	 * @param s
	 * @return 当所有字符串都不为空，才返回真
	 */
	public static boolean notEmpty(String... s) {
		for(String ss : s){
			if(empty(ss)){
				return false;
			}
		}
		return true;
	}

	public static boolean notEmpty(Integer... s) {
		for(Integer ss : s){
			if(empty(ss)){
				return false;
			}
		}
		return true;
	}

	public static boolean notEmpty(Long... s) {
		for(Long ss : s){
			if(empty(ss)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断列表是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static boolean empty(List<?> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 判断Map是否为空
	 * 
	 * @param map
	 * @return
	 */
	public static boolean empty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 判断数字是否为空
	 * 
	 * @param i
	 * @return
	 */
	public static boolean empty(Integer i) {
		return i == null || i == 0;
	}

	/**
	 * 得到当前年月
	 * 
	 * @return xxxx-xx
	 */
	public static String getCurrentMonth() {
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM");
		return matter1.format(dt);
	}

	/**
	 * 得到上个月 String
	 * 
	 * @return xxxx-xx
	 */
	public static String getLastMonth() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String time = format.format(c.getTime());
		return time;
	}

	/**
	 * 判断字符串是否为整数或小数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		boolean result = false;
		String regex1 = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
		boolean flag1 = str.matches(regex1);
		if (flag1) {
			String regex2 = "^[-+]?([0-9]+)$";
			boolean flag2 = str.matches(regex2);
			if (flag2) {
				result = true;
			} else {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 得到当前日期 yyyy-mm-dd
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		return matter1.format(dt);
	}

	public static double toDouble(String s){
		if(isNumeric(s)){
			return Double.parseDouble(s);			
		}else{
			return 0.0;
		}
	}
	
	public static double toDouble(Object obj){
		if(obj==null){
			return 0.0;
		}
		if(isNumeric(obj.toString())){
			return Double.parseDouble(obj.toString());			
		}else{
			return 0.0;
		}
	}

	/**
	 * 判断不为空串，也不为0
	 * @param id
	 * @return
	 */
	public static boolean fullNotEmpty(Object id) {
		if(id !=null){
			String sid = id.toString();
			if(!empty(sid)){
				Long lid = Long.valueOf(sid);
				if(!empty(lid) && lid !=0){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean empty(Long s) {
		return s == null || "".equals(s) || s.longValue()==0;

	}
	/**
	 * 判断是否为空，但是0不包含在内，即0不为空
	 * @param s
	 * @return
	 */
	public static boolean emptyWithZero(Long s) {
		return s == null || "".equals(s);

	}
	
	/**
	 * 判断字符数据是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean empty(String[] s) {
		return s==null || s.length==0;

	}
	
	/**
	 * 判断字符数据是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean empty(Object[] s) {
		return s==null || s.length==0;

	}

	/**
	 * 将string转成int
	 * @param idss
	 * @return
	 */
	public static int[] convertStringToInt(String[] idss) {
		int ss[] = new int[idss.length];
		if(!BaseUtil.empty(idss)){
			for(int i=0;i<idss.length;i++){
				String id = idss[i];
				if(!BaseUtil.empty(id)){
					ss[i] = Integer.valueOf(id);
				}
			}
		}
		return ss;
	}

	/**
	 * 将string转成int
	 * @param idss
	 * @return
	 */
	public static Long[] convertStringToLong(String[] idss) {
        Long ss[] = new Long[idss.length];
		if(!BaseUtil.empty(idss)){
			for(int i=0;i<idss.length;i++){
				String id = idss[i];
				if(!BaseUtil.empty(id)){
					ss[i] = Long.valueOf(id);
				}
			}
		}
		return ss;
	}

    public static String replace(String s, String s1,String s2) {
		s = s.replace(s1,s2);
		return  s;
    }

	/**
	 * 更换文件的后缀
	 * @param path 要更换的后缀当前的文件名称
	 * @param suffix
	 * @return
	 */
    public static String changeFileSuffix(String path, String suffix) {
        if(!BaseUtil.empty(path)){
            int index = path.lastIndexOf(".");
            String start = path.substring(0,index);
            path = start+suffix;
        }
    	return path;
    }

	public static String randomString(int length) {
		String str = new BigInteger(130, random).toString(32);
		return str.substring(0, length);
	}


	public static List emptyList(){
        return Collections.emptyList();
    }

    public static Map emptyMap(){
        return Collections.emptyMap();
    }

	/**
	 * 从数据里删除指定位置的数据
	 * @param list
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<?> removeList(List<?> list,int start,int end){
    	if(!empty(list)){
    		Iterator iterable = list.iterator();
    		int i=0;
    		while (iterable.hasNext() && i<end){
    			iterable.remove();
    			i++;
			}
		}
		return list;
	}


	/**
	 * 根据id的字符串，生成id的list,用于in查询
	 * @param ids
	 * @return
	 */
    public static List<Long> createIdList(String ids) {
    	List<Long> list = new ArrayList<>();
    	if(!empty(ids)){
    		String[] idss = ids.split(",");
    		for (String s : idss){
    			if(fullNotEmpty(s)){
    				list.add(Long.valueOf(s));
				}
			}
		}
		return list;
    }

	/**
	 * 将首字母大写
	 * @param str
	 * @return
	 */
	public static String toFirstUpperStr(String str) {
		String ns =  str.substring(0,1).toLowerCase()+str.substring(1,str.length());
    	return ns;
	}

	/**
	 * 将首字母大写
	 * @param str
	 * @return
	 */
	public static String toFirstLowerStr(String str) {
		String ns =  str.substring(0,1).toLowerCase()+str.substring(1,str.length());
		return ns;
	}

	/**
	 * 移除字符串最后的符号
	 * @param str
	 * @return
	 */
	public static String removeLastSymbol(String str) {
		if(!BaseUtil.empty(str) && str.length()>1){
			str = str.substring(0,str.length()-1);
			return str;
		}
		return "";
	}

	/**
	 * 将列表转为数组类型
	 * @param list
	 * @return
	 */
	public static Long[] toLongArray(List list){
		if(!empty(list)){
			Long[] ts = new Long[list.size()];
			for(int i=0;i<list.size();i++){
				ts[i] = (Long)list.get(i);
			}
			return ts;
		}
		return null;
	}

	/**
	 * 判断字符串s是否在后面的字符数组里
	 * @param s
	 * @param strings
	 * @return
	 */
	public static boolean inArr(String s,String ... strings){
		for(String ss : strings){
			if(s.equals(ss)){
				return true;
			}
		}
		return false;
	}

}
