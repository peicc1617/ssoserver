package xjtu.ssoserver.entity;

import lombok.Data;

/**
 * @基本功能:
 * @program:ssoserver
 * @author:peicc
 * @create:2020-02-10 14:41:59
 **/
@Data
public class Result {
    private boolean state;
    private String error;
    private Object content;

    public Result(boolean state, String error, Object content) {
        this.state = state;
        this.error = error;
        this.content = content;
    }
    /***
     * @函数功能：成功
     * @param object:
     * @return：xjtu.ssoserver.entity.Result
     */
    public static Result success(Object object){
        return new Result(true,null,object);
    }
    /***
     * @函数功能：失败
     * @param msg:
     * @return：xjtu.ssoserver.entity.Result
     */
    public static Result failure(String msg){
        return new Result(false,msg,null);
    }
}
