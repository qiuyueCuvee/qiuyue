package hou.controller;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hou.bean.ArticleListBean;
import hou.bean.LeaderBean;
import hou.bean.LogBean;
import hou.bean.UserBean;
import hou.service.ArticleService;
import hou.service.LeaderService;
import hou.utils.DateUtils;
import hou.utils.GetPostUtils;

@RestController
@RequestMapping(value = "/api")
@ComponentScan("hou.service")
@MapperScan("hou.mapper")
public class DataController {
	@Resource
	LeaderService leaderService;
	@Resource
	ArticleService articleService;
	
	
	//login API
	@RequestMapping(value = "/selectAllLeaders", method = RequestMethod.POST)
	public JSONObject selectAllLeaders(){
		JSONObject result = new JSONObject();
		try {
			List<Map<String,Object>> result_list=new ArrayList<Map<String,Object>>();
			List<LeaderBean> list = leaderService.selectAllLeaders();
			for (LeaderBean leaderBean : list) {
				Map<String,Object> map = new HashMap<String, Object>();
				String openid=leaderBean.getOpenid();
				List<LogBean> logs=leaderService.selectLogByName(openid);
				map.put("baseInfo", leaderBean);
				map.put("logs", logs);
				result_list.add(map);
			}
			result.put("code","200");
			result.put("data",result_list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	
	@RequestMapping(value = "/selectLogByName", method = RequestMethod.POST)
	public JSONObject selectLogByName(@RequestBody String data){
		//转译
		data=StringEscapeUtils.unescapeHtml4(data);
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else {
				JSONObject datajson = JSON.parseObject(data);
				if("".equals(datajson.get("openid")) || null==datajson.get("openid")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String openid=datajson.getString("openid");
					//先查询name是否已经存在
					int countName=leaderService.selCountByName(openid);
					if(countName==0) {
						//不存在
						result.put("code","204");
						result.put("data","");
						return result;
					}else {
						//存在
						List<LogBean> list=leaderService.selectLogByName(openid);
						result.put("code","200");
						result.put("data",list);
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	
	@RequestMapping(value = "/insertLeader", method = RequestMethod.POST)
	public JSONObject insertLeader(@RequestBody String data){
		//转译
		data=StringEscapeUtils.unescapeHtml4(data);
		//生成返回的json
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else{
				JSONObject datajson = JSON.parseObject(data);
				//得到对应的字段
				if(datajson.size()<1){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("name")) || null==datajson.get("name")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("state")) || null==datajson.get("state")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("pic")) || null==datajson.get("pic")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("openid")) || null==datajson.get("openid")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String name=datajson.getString("name");
					String state=datajson.getString("state");
					String pic=datajson.getString("pic");
					String openid=datajson.getString("openid");
					String desc=datajson.getString("desc");
					//得到系统时间
					String time=DateUtils.getDateTime();
					//先查询name是否已经存在
					int countName=leaderService.selCountByName(openid);
					if(countName!=0) {
						result.put("code","203");
						result.put("data","");
						return result;
					}else {
						//添加新的leader
						LeaderBean leader = new LeaderBean();
						leader.setName(name);
						leader.setOpenid(openid);
						leader.setPic(pic);
						leader.setState(state);
						leader.setTime(time);
						leader.setDesc(desc);
						leaderService.insertLeader(leader);
						int leader_id=leader.getId();
						if(leader_id!=0) {
							//插入一条log
							LogBean log = new LogBean();
							log.setLeader_id(leader_id);
							log.setState(state);
							log.setTime(time);
							log.setDesc(desc);
							leaderService.insertLog(log);
							result.put("code","200");
							result.put("data","");
							return result;
						}else {
							result.put("code","202");
							result.put("data","");
							return result;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	
	@RequestMapping(value = "/updateState", method = RequestMethod.POST)
	public JSONObject updateState(@RequestBody String data){
		//转译
		data=StringEscapeUtils.unescapeHtml4(data);
		//生成返回的json
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else{
				JSONObject datajson = JSON.parseObject(data);
				//得到对应的字段
				if(datajson.size()<1){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("openid")) || null==datajson.get("openid")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("state")) || null==datajson.get("state")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("desc")) || null==datajson.get("desc")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String openid=datajson.getString("openid");
					String state=datajson.getString("state");
					String desc=datajson.getString("desc");
					//得到系统时间
					String time=DateUtils.getDateTime();
					//先查询name是否已经存在
					int countName=leaderService.selCountByName(openid);
					if(countName==0) {
						result.put("code","204");
						result.put("data","");
						return result;
					}else {
						//更新state
						leaderService.updateState(openid,state,desc);
						//得到idByOpenid
						int leader_id=leaderService.selidByOpenid(openid);
						if(leader_id>0) {
							//插入一条log
							LogBean log = new LogBean();
							log.setLeader_id(leader_id);
							log.setState(state);
							log.setTime(time);
							log.setDesc(desc);
							leaderService.insertLog(log);
							result.put("code","200");
							result.put("data","");
							return result;
						}else {
							result.put("code","202");
							result.put("data","");
							return result;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	
	@RequestMapping(value = "/getRole", method = RequestMethod.POST)
	public JSONObject getRole(@RequestBody String data){
		//转译
		//data=StringEscapeUtils.unescapeHtml4(data);
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else {
				JSONObject datajson = JSON.parseObject(data);
				if("".equals(datajson.get("openid")) || null==datajson.get("openid")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String openid=datajson.getString("openid");
					//先查询name是否已经存在
					int countName=leaderService.selUserCountByOpenid(openid);
					if(countName==0) {
						//不存在
						result.put("code","204");
						result.put("data","");
						return result;
					}else {
						//存在
						Map<String,Object> role=leaderService.selectRoleByOpenid(openid);
						List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
						list.add(role);
						result.put("code","200");
						result.put("data",list);
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	
	@RequestMapping(value = "/pushUserInfo", method = RequestMethod.POST)
	public JSONObject pushUserInfo(@RequestBody String data){
		//转译
		//data=StringEscapeUtils.unescapeHtml4(data);
		//生成返回的json
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else{
				JSONObject datajson = JSON.parseObject(data);
				System.out.println(data);
				System.out.println(datajson);
				//得到对应的字段
				if(datajson.size()<1){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("userInfo")) || null==datajson.get("userInfo")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("openid")) || null==datajson.get("openid")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String openid=datajson.getString("openid");
					String userInfo=datajson.getString("userInfo");
					JSONObject userjson = JSON.parseObject(userInfo);
					String nickName=userjson.getString("nickName");
					String province=userjson.getString("province");
					String language=userjson.getString("language");
					String country=userjson.getString("country").toString();
					int gender= Integer.parseInt(userjson.getString("gender"));
					String city=userjson.getString("city");
					String avatarUrl=userjson.getString("avatarUrl");
					//先查询openid是否已经存在
					int countName=leaderService.selUserCountByOpenid(openid);
					if(countName==0) {
						result.put("code","204");
						result.put("data","");
						return result;
					}else {
						//添加新的user
						UserBean user= new UserBean();
						user.setAvatarUrl(avatarUrl);
						user.setCity(city);
						user.setCountry(country);
						user.setGender(gender);
						user.setLanguage(language);
						user.setNickName(nickName);
						user.setOpenid(openid);
						user.setProvince(province);
						int leader_id=leaderService.updateUser(user);
						if(leader_id!=0) {
							result.put("code","200");
							result.put("data","");
							return result;
						}else {
							result.put("code","202");
							result.put("data","");
							return result;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	@RequestMapping(value = "/onLogin", method = RequestMethod.POST)
	public JSONObject onLogin(@RequestBody String data){
		//转译
		//data=StringEscapeUtils.unescapeHtml4(data);
		//生成返回的json
		System.out.println("data="+data);
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else{
				JSONObject datajson = JSON.parseObject(data);
				//得到对应的字段
				if(datajson.size()<1){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("code")) || null==datajson.get("code")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String js_code=datajson.getString("code");
					String appid="wx7f6f304b9ddae45d";
					String secret="fe323af5370e124ee791c1f763cd3ac6";
					String grant_type="authorization_code";
					String wc=jscode2session(appid,secret,js_code,grant_type);
					JSONObject wcjson=JSONObject.parseObject(wc);
					String openid=wcjson.getString("openid");
					if(openid!=null) {
						String session_key=wcjson.getString("session_key");
						String unionid=wcjson.getString("unionid");
						UserBean user=new UserBean();
						user.setOpenid(openid);
						user.setSession_key(session_key);
						user.setUnionid(unionid);
						user.setRole(0);
						//先查询openid是否已经存在
						int countName=leaderService.selUserCountByOpenid(openid);
						Map<String,Object> map=new HashMap<String, Object>();
						List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
						if(countName==0) {
							//创建一个新的user
							leaderService.insertUser(user);
							result.put("code","200");
							result.put("data",list);
							map.put("role", 0);
							map.put("openid", openid);
							list.add(map);
							return result;
						}else {
							//已有openid，更新session_key
							leaderService.selectRoleByOpenid(openid);
							leaderService.updateSessionKey(user);
							map=leaderService.selectRoleByOpenid(openid);
							map.put("openid", openid);
							list.add(map);
							result.put("code","200");
							result.put("data",list);
							return result;
						}
					}else {
						//微信没有返回openid
						result.put("code","205");
						result.put("data","");
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}

	private String jscode2session(String appid, String secret, String js_code, String grant_type) {
		String url="https://api.weixin.qq.com/sns/jscode2session";
		String param="appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type;
		String result=GetPostUtils.sendGet(url, param);
		//去掉结果前面的/n
    	result = result.replaceAll("/n", "");
		System.out.println("【result】"+result);
        return result;
	}
	
	
	/*@RequestMapping(value = "/updatePic", method = RequestMethod.POST)
	public JSONObject updatePic(@RequestParam(value = "data", required = false, defaultValue = "") String data){
		//转译
		data=StringEscapeUtils.unescapeHtml4(data);
		//生成返回的json
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else{
				JSONObject datajson = JSON.parseObject(data);
				//得到对应的字段
				if(datajson.size()<1){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("name")) || null==datajson.get("name")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else if("".equals(datajson.get("pic")) || null==datajson.get("pic")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String name=datajson.get("name").toString();
					String pic=datajson.get("pic").toString();
					//先查询name是否已经存在
					int countName=leaderService.selCountByName(name);
					if(countName==0) {
						result.put("code","204");
						result.put("data","");
						return result;
					}else {
						//更新pic
						leaderService.updatePic(name,pic);
						result.put("code","200");
						result.put("data","");
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}*/
	
	//article API
	@RequestMapping(value = "/getArticleList", method = RequestMethod.POST)
	public JSONObject getArticleList(){
		JSONObject result = new JSONObject();
		try {
			List<ArticleListBean> list = articleService.selectArticleList();
			result.put("code","200");
			result.put("data",list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	
	@RequestMapping(value = "/getArticle", method = RequestMethod.POST)
	public JSONObject getArticle(@RequestBody String data){
		JSONObject result = new JSONObject();
		try {
			if("".equals(data) || data==null) {
				//参数缺失
				result.put("code","201");
				result.put("data","");
				return result;
			}else {
				JSONObject datajson = JSON.parseObject(data);
				if("".equals(datajson.get("id")) || null==datajson.get("id")){
					//参数缺失
					result.put("code","201");
					result.put("data","");
					return result;
				}else {
					String id = datajson.getString("id");
					List<ArticleListBean> list = articleService.getArticle(id);
					//修改list中的click_rate
					int updateClick_rate= articleService.updateClick_rate(id);
					if(updateClick_rate>0) {
						result.put("code","200");
						result.put("data",list);
						return result;
					}else {
						result.put("code","202");
						result.put("data","");
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code","202");
			result.put("data","");
			return result;
		}
	}
	
	
}
