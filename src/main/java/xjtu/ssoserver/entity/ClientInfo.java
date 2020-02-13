package xjtu.ssoserver.entity;

import lombok.Data;

/**
 * @基本功能:客户端信息（主要包括登出地址、sessionId）
 * @program:ssoserver
 * @author:peicc
 * @create:2020-02-10 13:44:54
 **/
@Data
public class ClientInfo {
    private long id;
    private String clientLogOutURL;
    private String jesessionId;
}
