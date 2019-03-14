package hou.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hou.bean.ArticleListBean;

@Mapper
public interface ArticleMapper {

	public List<ArticleListBean> selectArticleList();

	public List<ArticleListBean> getArticle(@Param("id")String id);

	public int updateClick_rate(@Param("article_id")String article_id);
	
}
