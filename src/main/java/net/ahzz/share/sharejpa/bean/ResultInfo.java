package net.ahzz.share.sharejpa.bean;

public class ResultInfo {
    /**
     * 用于保存返回数据对象的
     */
    public Object data;
    /**
     * 用于返回提示信息
     */
    public String mess;
    /**
     * 用于返回请求的成功与失败，true表示成功，false表示失败
     */
    public boolean success;
    /**
     * 统一的返回码，200表示成功，201表示后台报错。其他值由各业务接口决定
     */
    public int code;

    public Object extData1;
    public Object extData2;
    public Object extData3;
}
