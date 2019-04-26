package com.br.entity.access;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单实体
 * @Author zyx
 * @date 2019 02 21
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu implements Serializable {

    @Getter @Setter private Integer menuSeq;
    @Getter @Setter private String menuName;
    @Getter @Setter private Integer menuLevel;
    @Getter @Setter private String menuUrl;
    @Getter @Setter private String menuIcon;
    @Getter @Setter private String menuCreateBy ;
    @Getter @Setter private Date menuCreateTime;
    @Getter @Setter private String menuUpdateBy ;
    @Getter @Setter private Date menuUpdateTime;
    @Getter @Setter private String menuRemark;
    @Getter @Setter private Integer menuParentSeq;
    @Getter @Setter private List<Menu> menuChildMenus;

}
