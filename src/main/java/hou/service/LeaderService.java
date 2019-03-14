package hou.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import hou.bean.LeaderBean;
import hou.bean.LogBean;
import hou.bean.UserBean;
import hou.mapper.LeaderMapper;

@ComponentScan({"hou.mapper"})
@Service
public class LeaderService {
	@Resource
	public LeaderMapper leaderMapper;
	
	public List<LeaderBean> selectAllLeaders() throws Exception{
		return leaderMapper.selectAllLeaders();
	}
	
	public int insertLeader(LeaderBean leader) throws Exception{
		return leaderMapper.insertLeader(leader);
	}

	public List<LogBean> selectLogByName(String openid) {
		return leaderMapper.selectLogByName(openid);
	}

	public int selCountByName(String openid) {
		return leaderMapper.selCountByName(openid);
	}

	public void insertLog(LogBean log) {
		leaderMapper.insertLog(log);
	}

	public int updateState(String openid, String state, String desc) {
		return leaderMapper.updateState(openid,state,desc);
	}

	public int updatePic(String name, String pic) {
		return leaderMapper.updatePic(name,pic);
	}

	public int selUserCountByOpenid(String openid) {
		return leaderMapper.selUserCountByOpenid(openid);
	}

	public Map<String,Object> selectRoleByOpenid(String openid) {
		return leaderMapper.selectRoleByOpenid(openid);
	}

	public int updateUser(UserBean user) {
		return leaderMapper.updateUser(user);
	}

	public int insertUser(UserBean user) {
		return leaderMapper.insertUser(user);
	}

	public int updateSessionKey(UserBean user) {
		return leaderMapper.updateSessionKey(user);
	}

	public int selidByOpenid(String openid) {
		return leaderMapper.selidByOpenid(openid);
	}

	
}
