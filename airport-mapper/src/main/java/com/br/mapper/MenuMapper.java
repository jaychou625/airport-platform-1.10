package com.br.mapper;

import com.br.entity.access.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 菜单 Mapper
 *
 * @Author zyx
 * @Date 2019 03 01
 */
@Mapper
@Transactional
public interface MenuMapper {

    /**
     * 单个部门查询 By 序号
     * @return
     */
    Menu find(@Param("menuSeq") Integer menuSeq);

    /**
     * 查询所有菜单
     */
    List<Menu> findAll();


    /**
     * 查询菜单集合 By 角色序号
     * @return
     */
    List<Menu> findMenusByRoleSeq(@Param("roleSeq") Integer roleSeq);


    /**
     * 查询子菜单 By menuSeq,
     * @return List<Menu>
     */
    List<Menu> findChildMenus(@Param("menuSeq") Integer menuSeq);



}
