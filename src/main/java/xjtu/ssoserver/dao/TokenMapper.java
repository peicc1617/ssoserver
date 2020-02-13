package xjtu.ssoserver.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xjtu.ssoserver.entity.Token;

import java.util.Map;

/**
 * @基本功能:
 * @program:ssoserver
 * @author:peicc
 * @create:2020-02-07 17:01:27
 **/
@Repository
@Mapper
public interface TokenMapper {
    /***
     * @函数功能：存储token
     * @param token:
     * @param userInfo:
     * @param gmtExpiry:
     * @return：void
     */
    @Select("INSERT INTO token (token,user_info,gmt_expiry) VALUES (#{token},#{userInfo},#{gmtExpiry})")
    public void storeToken(String token,String userInfo,long gmtExpiry);
    /***
     * @函数功能：取出token对应的用户信息
     * @param token:
     * @return：java.util.Map<java.lang.String,java.lang.String>
     */
    @Select("SELECT id,user_Info FROM token WHERE token=#{token}")
    public Token getInfo(String token);
    /***
     * @函数功能：判断token是否存在
     * @param token:
     * @return：boolean
     */
    @Select("SELECT 1 FROM token WHERE token=#{token}")
    public String containsToken(String token);
    /***
     * @函数功能：删除token对应的行记录
     * @param token:
     * @return：void
     */
    @Select("DELETE FROM token WHERE token=#{token}")
    public void removeToken(String token);
    /***
     * @函数功能：根据token获取tokenId
     * @param token:
     * @return：long
     */
    @Select("SELECT id FROM token WHERE token=#{token}")
    public Long getTokenIdByToken(String token);
}
