package cn.aradin.mybatis.boot.starter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.aradin.spring.core.rowmap.Row;

public interface IBaseMapper<T> {
	/**
	 * 新增对象
	 * @param user
	 * @return
	 */
	public int save(T object);
	
	/**
	 * 修改对象
	 * @param user
	 * @return
	 */
	public int mod(T object);
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public T get(@Param("id") Object id);
	
	/**
	 * 删除对象
	 * @param id
	 * @return
	 */
	public int del(@Param("id") Object id);
	
	/**
	 * 条件统计
	 * @param row
	 * @return
	 */
	public int count(Row row);
	
	/**
	 * 查询列表
	 * @param row
	 * @return
	 */
	public List<T> find(Row row);
}
