package xjtu.ssoserver.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xjtu.ssoserver.entity.User;

/**
 * @基本功能:
 * @program:ssoserver
 * @author:peicc
 * @create:2020-02-06 22:50:14
 **/
@Repository
@Mapper
public interface UserMapper {
    /***
     * @函数功能：查询用户信息是否存在
     * @param userName:
     * @param password:
     * @return：xjtu.ssoserver.entity.User
     */
    @Select("SELECT * FROM user WHERE user_name=#{userName} AND password=#{password}")
    public User getUser(String userName, String password);
    /***
     * @函数功能：注册用户
     * @param user:
     * @return：void
     */
    @Select("INSERT INTO user (user_name,password,nick_name,domain,domain_name,email,phone_number,job_number) VALUES (#{userName},#{password},#{nickName},#{domain},#{domainName},#{email},#{phoneNumber},#{jobNumber})")
    public void addUser(User user);
}
