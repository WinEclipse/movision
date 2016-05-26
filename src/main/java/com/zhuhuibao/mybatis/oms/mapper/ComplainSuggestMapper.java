package com.zhuhuibao.mybatis.oms.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;

public interface ComplainSuggestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ComplainSuggest record);

    int insertSelective(ComplainSuggest record);

    ComplainSuggest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ComplainSuggest record);

    int updateByPrimaryKey(ComplainSuggest record);

	List<ComplainSuggest> findAllComplaintSuggest(RowBounds rowBounds, Map<String, Object> map);
}