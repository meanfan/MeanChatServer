package com.mean.meanchat.mapper;

import com.mean.meanchat.bean.UserRelation;

public interface UserRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRelation record);

    int insertSelective(UserRelation record);

    UserRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRelation record);

    int updateByPrimaryKey(UserRelation record);
}