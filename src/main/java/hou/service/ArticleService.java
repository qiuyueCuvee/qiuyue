package hou.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import hou.bean.ArticleListBean;
import hou.mapper.ArticleMapper;

@ComponentScan({"hou.mapper"})
@Service
public class ArticleService {
	@Resource
	public ArticleMapper articleMapper;

	public List<ArticleListBean> selectArticleList() {
		return articleMapper.selectArticleList();
	}

	public List<ArticleListBean> getArticle(String id) {
		return articleMapper.getArticle(id);
	}
	
	public int updateClick_rate(String article_id) {
		return articleMapper.updateClick_rate(article_id);
	}
}
