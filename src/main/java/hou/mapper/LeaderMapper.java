package hou.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hou.bean.LeaderBean;
import hou.bean.LogBean;
import hou.bean.UserBean;

@Mapper
public interface LeaderMapper {

	public List<LeaderBean> selectAllLeaders();
	
	public int insertLeader(LeaderBean leader);

	public List<LogBean> selectLogByName(@Param("openid")String openid);

	public int selCountByName(@Param("openid")String openid);

	public void insertLog(LogBean log);

	public int updateState(@Param("openid")String openid, @Param("state")String state, @Param("desc")String desc);

	public int updatePic(@Param("name")String name, @Param("pic")String pic);

	public int selUserCountByOpenid(@Param("openid")String openid);

	public Map<String,Object> selectRoleByOpenid(@Param("openid")String openid);

	public int updateUser(UserBean user);

	public int insertUser(UserBean user);

	public int updateSessionKey(UserBean user);

	public int selidByOpenid(@Param("openid")String openid);
}
