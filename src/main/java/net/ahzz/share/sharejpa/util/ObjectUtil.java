package net.ahzz.share.sharejpa.util;


import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

/*
 * @author 呼唤
 * @since 2011-3-1
 */
public class ObjectUtil extends BaseUtil{
	

	public static Object exchange(Object source, Object target) {

		Class oldPoClass = source.getClass();
		Class newPoClass = target.getClass();

		for (Method method : oldPoClass.getMethods()) {
			if (method.getName().equals("getClass")) {
				continue;
			}
			if (!isGetter(method))
				continue;
			try {
				Object result = method.invoke(source, new Object[0]);
				if (result != null) {
					if (result instanceof Collection) {
						if (!((Collection) result).isEmpty())
							;
					} else {
						if (result.getClass().isArray()) {
							if (result instanceof Object[]) {
								if (((Object[]) result).length != 0)
									;
							} else if (result instanceof byte[]) {
								if (((byte[]) result).length != 0)
									;
							} else if (result instanceof short[]) {
								if (((short[]) result).length != 0)
									;
							} else if (result instanceof int[]) {
								if (((int[]) result).length != 0)
									;
							} else if (result instanceof long[]) {
								if (((long[]) result).length != 0)
									;
							} else if (result instanceof float[]) {
								if (((float[]) result).length != 0)
									;
							} else if (result instanceof double[]) {
								if (((double[]) result).length != 0)
									;
							} else if (result instanceof char[]) {
								if (((char[]) result).length != 0)
									;
							} else if ((result instanceof boolean[])
									&& (((boolean[]) result).length == 0))
								;
						}

						String methodName = getter2Setter(method.getName());
						Class methodParam = method.getReturnType();
						try {
							Method setter = newPoClass.getMethod(methodName,
									new Class[] { methodParam });
							setter.invoke(target, new Object[] { result });
						} catch (NoSuchMethodException e) {
							log.warn("目标对象缺少" + e.getMessage() + "方法");
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return target;
	}


	public static Object exchange(Object source, Object target,String ...ignoreFields) {

		Class oldPoClass = source.getClass();
		Class newPoClass = target.getClass();

		for (Method method : oldPoClass.getMethods()) {

			if (method.getName().equals("getClass")) {
				continue;
			}
			if (!isGetter(method)){
				continue;
			}

			try {
				Object result = method.invoke(source, new Object[0]);
                String fieldName = _getFieldNameByMethod(method);
                if(ArrayUtils.contains(ignoreFields,fieldName)){
                    continue;
                }
				if (result != null) {
					if (result instanceof Collection) {
						if (!((Collection) result).isEmpty())
							;
					} else {
						if (result.getClass().isArray()) {
							if (result instanceof Object[]) {
								if (((Object[]) result).length != 0)
									;
							} else if (result instanceof byte[]) {
								if (((byte[]) result).length != 0)
									;
							} else if (result instanceof short[]) {
								if (((short[]) result).length != 0)
									;
							} else if (result instanceof int[]) {
								if (((int[]) result).length != 0)
									;
							} else if (result instanceof long[]) {
								if (((long[]) result).length != 0)
									;
							} else if (result instanceof float[]) {
								if (((float[]) result).length != 0)
									;
							} else if (result instanceof double[]) {
								if (((double[]) result).length != 0)
									;
							} else if (result instanceof char[]) {
								if (((char[]) result).length != 0)
									;
							} else if ((result instanceof boolean[])
									&& (((boolean[]) result).length == 0))
								;
						}

						String methodName = getter2Setter(method.getName());
						Class methodParam = method.getReturnType();
						try {
							Method setter = newPoClass.getMethod(methodName,
									new Class[] { methodParam });
							setter.invoke(target, new Object[] { result });
						} catch (NoSuchMethodException e) {
							log.warn("目标对象缺少" + e.getMessage() + "方法");
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return target;
	}

	/**
	 * 从方法名称，取字段的名称
	 * @param method
	 * @return
	 */
	private static String _getFieldNameByMethod(Method method) {
		String methodName = method.getName();
		if(!BaseUtil.empty(methodName)){
		    methodName = methodName.replace("get","");
		    String fieldName =  methodName.substring(0,1).toLowerCase()+methodName.substring(1,methodName.length());
            log.info("fieldName "+fieldName);
		    return fieldName;
		}

        return methodName;
	}

	public static boolean isGetter(Method method) {
		String name = method.getName();

		return (method.getParameterTypes().length == 0)
				&& (!name.equals("getClass")) && (name.startsWith("get"))
				&& (name.length() > 3);
	}

	public static String getter2Setter(String methodName) {
		if (methodName.startsWith("get")) {
			return methodName.replaceFirst("g", "s");
		}

		return methodName;
	}

	/**
	 * MD5 加密
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		if (str == null || "".equals(str)) {
			return "";
		}
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	/**
	 * 判断类里是否存在指定的字段
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static boolean hasField(Class<?> clazz, String fieldName) {
		List<String> list = getFieldNames(clazz);
		if (list.contains(fieldName)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取对象的所有字段名称
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<String> getFieldNames(Class clazz) {
		Field[] fields = getClassFields(clazz);
		List<String> fds = new ArrayList<String>();
		for (Field fd : fields) {
			fds.add(fd.getName());
		}
		return fds;
	}

	public static void setFieldValue(Object instance, String fieldName,
			Object fieldValue) {
		try {
			Class clazz = instance.getClass();
			Field[] fields = getClassFields(clazz);
			for (Field field : fields) {
				String fn = field.getName();
				if (fieldName.equalsIgnoreCase(fn)) {
					String stringLetter = fn.substring(0, 1).toUpperCase();
					String setName = "set" + stringLetter + fn.substring(1);
					if (fieldValue != null) {
						Method setMethod = clazz.getMethod(setName,
								new Class[] { field.getType() });
						// 调用拷贝对象的setXXX（）方法
						setMethod.invoke(instance, new Object[] { fieldValue });
					}
					break;
				}
				continue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到类的字段，这里同时得到父类的字段和当前类的字段，子类的字段将覆盖父类字段
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getClassFields(Class clazz) {
		Field[] fields1 = clazz.getDeclaredFields();
		Field[] fields2 = clazz.getSuperclass().getDeclaredFields();
		Field[] fs = (Field[]) ArrayUtils.addAll(fields1, fields2);
		return fs;
	}

	/**
	 * 将List<HashMap<String,Object>> 转换成 List<Column>
	 *
	 * @return
	 */
	public static List<Object> listParseClass(List<Map<String, Object>> list,
			Class clazz) {
		List<Object> olist = new ArrayList<Object>();
		try {
			List<String> fields = ObjectUtil.getFieldNames(clazz);
			if (!empty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Object obj = clazz.newInstance();
					Map<String, Object> map = list.get(i);
					for (String fn : fields) {
						ObjectUtil.setFieldValue(obj, fn,
								String.valueOf(map.get(fn)));
					}
					olist.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return olist;
	}

	/**
	 * 将Object类型的数组转换成String类型的
	 * 
	 * @param objs
	 * @return
	 */
	public static String[] objParseStr(Object[] objs) {
		if (objs != null) {
			String[] str = new String[objs.length];
			int i = 0;
			for (Object obj : objs) {
				str[i] = (String) obj;
				i++;
			}
			return str;
		}
		return null;
	}

	public static Object getFieldValue(String fieldName, String fieldType,
			Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		if (hasField(clazz, fieldName)) {
			String stringLetter = fieldName.substring(0, 1).toUpperCase();
			String getName = "get" + stringLetter + fieldName.substring(1);
			if ("Boolean".equalsIgnoreCase(fieldType)) {
				getName = "is" + stringLetter + fieldName.substring(1);
			}
			Method getMethod = clazz.getMethod(getName, new Class[] {});
			Object fieldValue = getMethod.invoke(obj, new Object[] {});
			return fieldValue;
		}
		return "";
	}

	public static Object getFieldValue(String fieldName, Object obj)
			throws Exception {
		return getFieldValue(fieldName, "", obj);
	}

	public static String getStringFieldValue(String fieldName, Object obj)
			throws Exception {
		return getFieldValue(fieldName, "", obj)+"";
	}

	/**
	 * 
	 * 将List<String>数组转换成Double数组
	 * 
	 * @param str
	 *            []
	 * @return
	 */
	public static double[] strParseDbl(List<String> str) {
		if (str != null) {
			int i = 0;
			double[] dbl = new double[str.size()];
			try {
				for (String strValue : str) {
					dbl[i] = Double.parseDouble(strValue);
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dbl;
		}
		return null;
	}

	/**
	 * 字符串拼接
	 * 
	 * @param strValue
	 *            [] 拼接数据
	 * @return
	 */
	public static String strAndStr(String... strValue) {
		StringBuilder strBuild = new StringBuilder();
		if (strValue != null) {
			for (int i = 0; i < strValue.length; i++) {
				strBuild.append(strValue[i]);
			}
		}
		return strBuild.toString();
	}

	/**
	 * 得到在对象obj里，field的实例对象
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	public static Object getFieldInstance(Object obj, Field field) {
		String fieldName = field.getName();
		String uperLetter = fieldName.substring(0, 1).toUpperCase();
		String getName = "get" + uperLetter + fieldName.substring(1);
		try {
			Method getMethod = obj.getClass()
					.getMethod(getName, new Class[] {});
			Object o = getMethod.invoke(obj, new Object[] {});
			if (o != null) {
				return o;
			}
			return field.getType().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置Field的实例
	 * 
	 * @param obj
	 * @param field
	 * @param instance
	 */
	public static void setFieldInstance(Object obj, Field field, Object instance) {
		try {
			Class<?> clazz = obj.getClass();
			String fn = field.getName();
			String stringLetter = fn.substring(0, 1).toUpperCase();
			String setName = "set" + stringLetter + fn.substring(1);
			Method setMethod = clazz.getMethod(setName,
					new Class[] { field.getType() });
			setMethod.invoke(obj, new Object[] { instance });
		} catch (Exception ex) {
		}
	}
	
	/**
	 * 得到泛型的类型，
	 * @param clazz 泛型的父对象，即哪个类接收别的类的泛型。
	 * 比如BusinessAction<T, M extends BaseManager<T, Integer>>这是个父类，所以传进来的class即是BusinessAction.class
	 * @param position 参数的位置，即泛型在类里做为参数的位置，从0开始计数
	 * @return
	 */
	public static Class<?> getFXClass(Class<?> clazz,int position){
		Type t = clazz.getGenericSuperclass();
		ParameterizedType p = (ParameterizedType)t;
		Class<?> c = (Class<?>)p.getActualTypeArguments()[position];
		return c;
	}
	
	public static Object getEntityId(Object entity) {
		Object id = null;
		try {
			id = ObjectUtil.getFieldValue("id",
					entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	public static Integer getEntityIntegerId(Object entity) {
		Integer id = 0;
		try {
			id = (Integer)ObjectUtil.getFieldValue("id",
					entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static long getEntityLongId(Object entity) {
		long id = 0;
		try {
			id = (Long)ObjectUtil.getFieldValue("id",
					entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public static boolean isPrimitiveType(Object obj) {
		if (obj == null) {
			return true;
		}
		String className = obj.getClass().getSimpleName();
		if ("String".equalsIgnoreCase(className)) {
			return true;
		}
		if ("Boolean".equalsIgnoreCase(className)) {
			return true;
		}
		if ("Long".equalsIgnoreCase(className)) {
			return true;
		}
		if ("Integer".equalsIgnoreCase(className)) {
			return true;
		}
		if ("Double".equalsIgnoreCase(className)) {
			return true;
		}
		if ("Float".equalsIgnoreCase(className)) {
			return true;
		}
		return false;
	}

	public static boolean isNotConnections(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof List) {
			return false;
		}
		if (obj instanceof Map) {
			return false;
		}
		return true;
	}

	public static boolean isList(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof List) {
			return true;
		}
		return false;
	}

	/**
	 * 将对象属性值 为字符null的，转换为"",有时间再实现吧。
	 * @param obj
	 * @return
	 */
	public static Object filterStrNull(Object obj) {
		return obj;
	}
	
	
	public static String[] getAllowFields(Class<?> clazz,String disAllowFields){
		List<String> flist = new ArrayList<String>();
		Field[] ff = clazz.getDeclaredFields();
		for(Field field : ff){
			String fieldname = field.getName();
			flist.add(fieldname);
		}
		String[] daf = disAllowFields.split(",");
		if(!empty(daf)){
			for(String s : daf){
				flist.remove(s);
			}
		}
		return flist.toArray(daf);
	}


	public static String getClassName(Class clazz){
		return clazz.toString().replace("class", "");
	}


	public static Object executeMethod(Class clazz,String method,Object[] params){
	    try {
            Object obj = clazz.newInstance();
			Class[] paramsTypes = getParamsType(params);
            Method getMethod = clazz.getMethod(method, paramsTypes);
            return getMethod.invoke(obj, params);
        }catch (Exception e){
	        e.printStackTrace();
        }
        return null;
    }

	/**
	 * 根据参数，得到参数类型
	 * @param params
	 * @return
	 */
	private static Class[] getParamsType(Object[] params) {
		if(params !=null){
			Class[] types = new Class[params.length];
			for (int i=0;i<params.length;i++){
				Object obj = params[i];
				Class type = obj.getClass();
				System.out.println("参数的类型type "+type);
				types[i] = type;
			}
			return types;
		}
		return  new Class[]{};

	}

	/**
     * 执行类的方法
     * @param clazzName
     * @param method
     * @param param 方法的参数
     * @return
     */
    public static Object executeMethod(String clazzName,String method,Object param){
        try {
            Class clazz = Class.forName(clazzName);
            Object[] params = new Object[]{param};
            Object obj = clazz.newInstance();
            Method getMethod = clazz.getMethod(method, new Class[]{Object.class});
            return getMethod.invoke(obj, params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行类的方法
     * @param method
     * @return
     */
    public static Object executeMethod(Class clazz,Object obj,String method){
        try {
            System.out.println("clazz "+clazz+" obj "+obj);
            Method getMethod = clazz.getMethod(method, new Class[] {});
            return getMethod.invoke(obj, new Object[] {});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行类的方法,这里传了类，又传对象的原因是，obj在外层是Object类型，
     * 而非真实的类型，所以需要另外传
     * @param clazz
     * @param method
     * @param params
     * @return
     */
	public static Object executeMethod(Class clazz,Object obj,String method,Object[] params){
		try {
			System.out.println("clazz "+clazz+" obj "+obj);
            Class[] paramsTypes = getParamsType(params);
			Method getMethod = clazz.getMethod(method, paramsTypes);
			return getMethod.invoke(obj, params);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行类的方法,这里传了类，又传对象的原因是，obj在外层是Object类型，
	 * 而非真实的类型，所以需要另外传
	 * @param clazz
	 * @param method
	 * @param params
	 * @return
	 */
	public static Object executeMethod(Class clazz,Object obj,String method,Object[] params,Class[] paramsTypes){
		try {
			System.out.println("clazz "+clazz+" obj "+obj);
			Method getMethod = clazz.getMethod(method, paramsTypes);
			return getMethod.invoke(obj, params);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前列表里，指定属性最小值的那个对象
	 * @param list
	 * @param clazz
	 * @param field
	 * @return
	 */
    public static Object getMinValueObj(List<?> list,Class clazz,String field) throws Exception{
    	Map<Long,Object> map = new HashMap<>();
        if(!BaseUtil.empty(list)){
            for (Object obj : list){
                String value = getFieldValue(field,obj)+"";
                if(BaseUtil.fullNotEmpty(value)){
                    map.put(Long.valueOf(value),obj);
                }
            }
        }
        Optional<Long> min = map.keySet().stream().min(Comparator.comparing(Function.identity()));
	    Long minprice = min.get();
	    return map.get(minprice);
    }


    public static  Object updateValue(Object target, Object source, String[] ignoreField) {
        try {
            for (String field : ignoreField) {
                Object value = getFieldValue(field, source);
                setFieldValue(target,field,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return target;
    }

    public static List<Object> convert(List<Object> list,Class clazz){
    	List<Object> newList = new ArrayList<>();
    	try {
            for (Object obj : list) {
                Object newObj = clazz.newInstance();
                exchange(obj,newObj);
                newList.add(newObj);
            }
        }catch (Exception e){
    	    e.printStackTrace();
        }
        return newList;

	}

	public static List<String> getFieldNamesByAnnotation(Class objc, Class ano){
		List<String> names = new ArrayList<>();
		Field[] declaredFields = objc.getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field field = declaredFields[i];
			Annotation annotation = field.getAnnotation(ano);
			if(annotation!=null){
				names.add(field.getName());
			}
		}
		return names;
	}

	public static List<Field> getFieldsByAnnotation(Class objc, Class ano){
		List<Field> fields = new ArrayList<>();
		Field[] declaredFields = objc.getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field field = declaredFields[i];
			Annotation annotation = field.getAnnotation(ano);
			if(annotation!=null){
				fields.add(field);
			}
		}
		return fields;
	}

	public static Object getAnnotation(Class objc, Class ano){
		Annotation annotation = objc.getAnnotation(ano);
		return annotation;
	}

	/**
	 * 根据类名，生成类的实例
	 * @param className
	 * @return
	 */
	public static Object getInstance(String className) throws Exception{
		return Class.forName(className).newInstance();
	}

	public static Object[] getFieldValues(String[] fields, Object entity) throws Exception{
		Object[] obj = null;
		if(!BaseUtil.empty(fields)){
			obj = new Object[fields.length];
			for(int i=0;i<fields.length;i++){
				obj[i] = getFieldValue(fields[i],entity);
			}
		}
		return obj;
	}
}
