package xjtu.ssoserver.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xjtu.ssoserver.entity.ClientInfo;

import java.util.List;

@Repository
@Mapper
public interface ClientInfoMapper {
   /***
    * @函数功能：存储客户端登出地址以及sessionId
    * @param tokenId:
    * @param clientLogOutURL:
    * @param jesessionId:
    * @return：void
    */
    @Select("INSERT INTO client_info (token_id,client_log_out_url,jesession_id) VALUES (#{tokenId},#{clientLogOutURL},#{jesessionId})")
    public void storeClientInfo(long tokenId,String clientLogOutURL,String jesessionId);
    /***
     * @函数功能：根据tokenId获取客户端信息
     * @param tokenId:
     * @return：java.util.List<xjtu.ssoserver.entity.ClientInfo>
     */
    @Select("SELECT client_log_out_url,jesession_id FROM client_info WHERE token_id=#{tokenId}")
    public List<ClientInfo> getAllClientInfoByTokenId(Long tokenId);
    /***
     * @函数功能：清除tokenId对应的客户端信息
     * @param tokenId:
     * @return：void
     */
    @Select("DELETE FROM client_info WHERE token_id=#{tokenId}")
    public void removeClientInfoByTokenId(long tokenId);
}
