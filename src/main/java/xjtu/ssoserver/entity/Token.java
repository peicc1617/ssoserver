package xjtu.ssoserver.entity;

import lombok.Data;

import java.util.Date;

/**
 * @基本功能:
 * @program:ssoserver
 * @author:peicc
 * @create:2020-02-07 16:53:21
 **/
@Data
public class Token {
    private long id;
    private String token;
    private String userInfo;
    private Date gmtCreate;//创建时间
    private Date gmtModified;// 更新时间
    private long gmtExpiry;//过期时间

}
