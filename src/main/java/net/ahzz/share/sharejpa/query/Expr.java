package net.ahzz.share.sharejpa.query;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;

/**
 * 表达式
 */
public class Expr {

    public final static String OPT_EQ = "=";
    public final static String OPT_NEQ = "!=";
    public final static String OPT_GT = ">";
    public final static String OPT_GE = ">=";
    public final static String OPT_LT = "<";
    public final static String OPT_LQ = "<=";
    public final static String OPT_LIKE = "like";
    public final static String OPT_IN = "in";
    public final static String OPT_IS_NULL = "is null";
    public final static String OPT_IS_NOT_NULL = "is not null";


    /**
     * 属性名称
     */
    public String prop;
    /**
     * 属性值
     */
    public Object value;

    /**
     * 操作符
     */
    public String opt = "=";

    /**
     * 别名
     */
    public String alias = "";


    public Expr(String prop, Object value, String opt) {
        this.prop = prop;
        this.value = value;
        this.opt = opt;
    }

    public Expr(String prop, Object value) {
        this.prop = prop;
        this.value = value;
    }


    public Expr alias(String alias) {
        this.alias = alias + ".";
        return this;
    }

    public static Expr create(String prop, Object value) {
        if (value instanceof Collection) {
            return new Expr(prop, _changeSymbol(value.toString()), OPT_IN);
        } else if (value.getClass().isArray()) {
            return new Expr(prop, _changeSymbol(ArrayUtils.toString(value)), OPT_IN);
        } else {
            return new Expr(prop, value);
        }
    }

    public static Expr create(String prop, Object value, String opt) {
        if (OPT_LIKE.equals(opt)) {
            return like(prop, value);
        }
        return new Expr(prop, value, opt);
    }


    /**
     * 单独的like方法
     *
     * @param prop
     * @param value
     * @return
     */
    public static Expr like(String prop, Object value) {
        String temp = String.valueOf(value);
        temp = "%" + value + "%";
        return new Expr(prop, temp, OPT_LIKE);
    }

    /**
     * 单独的相等于方法
     *
     * @param prop
     * @param value
     * @return
     */
    public static Expr eq(String prop, Object value) {
        return new Expr(prop, value, OPT_EQ);
    }

    public static Expr neq(String prop, Object value) {
        return new Expr(prop, value, OPT_NEQ);
    }

    /**
     * 单独的like方法,%在前
     *
     * @param prop
     * @param value
     * @return
     */
    public static Expr slike(String prop, Object value) {
        String temp = String.valueOf(value);
        temp = "%" + value;
        return new Expr(prop, temp, OPT_LIKE);
    }

    /**
     * 单独的like方法,%在后
     *
     * @param prop
     * @param value
     * @return
     */
    public static Expr elike(String prop, Object value) {
        String temp = String.valueOf(value);
        temp = value + "%";
        return new Expr(prop, temp, OPT_LIKE);
    }

    public static Expr isNull(String prop) {
        return new Expr(prop, "", OPT_IS_NULL);
    }

    public static Expr isNotNull(String prop) {
        return new Expr(prop, "", OPT_IS_NOT_NULL);
    }


    public static Expr create(String prop, Object[] values) {
        return new Expr(prop, _changeSymbol(values), OPT_IN);
    }

    public static Expr create(String prop, Collection values) {
        return new Expr(prop, _changeSymbol(values), OPT_IN);
    }

    /**
     * 单独的in方法
     *
     * @param prop
     * @param values
     * @return
     */
    public static Expr in(String prop, Object[] values) {
        return new Expr(prop, _changeSymbol(values), OPT_IN);
    }


    private static String _changeSymbol(Object[] values) {
        StringBuffer sb = new StringBuffer();
        if (values instanceof String[]) {
            sb.append("(");
            for (Object obj : values) {
                sb.append("'" + obj + "',");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(")");
            return sb.toString();
        } else if (values instanceof Long[] || values instanceof Integer[]) {
            sb.append("(");
            for (Object obj : values) {
                sb.append("" + obj + ",");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(")");
            return sb.toString();
        } else {
            return ArrayUtils.toString(values);
        }

    }

    private static String _changeSymbol(Collection values) {
        StringBuffer sb = new StringBuffer();
        if (values != null && !values.isEmpty()) {
            sb.append("(");
            for (Object obj : values) {
                if (obj instanceof String) {
                    sb.append("'" + obj + "'");
                } else {
                    sb.append(obj);
                }
                sb.append(",");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(")");
        }
        return sb.toString();
    }

    /**
     * 单独的in方法
     *
     * @param prop
     * @param values
     * @return
     */
    public static Expr in(String prop, Collection values) {
        return new Expr(prop, _changeSymbol(values), OPT_IN);
    }

    /**
     * 根据值的类型，返回sql需要的值
     *
     * @return
     */
    public String getValue() {
        String real = "";
        String temp = "";
        if (value instanceof String) {
            temp = (String) value;
            if (temp.indexOf("(") != -1 && temp.length() > 2) {
                real = temp;
            } else if (temp.length() > 0) {
                real = "'" + temp + "'";
            }
        } else if (value instanceof Long || value instanceof Integer) {
            real = value + "";
        }
        return real;
    }

    private static String _changeSymbol(String s) {
        s = s.replace("[", "(");
        s = s.replace("]", ")");
        s = s.replace("{", "(");
        s = s.replace("}", ")");
        return s;
    }

}
