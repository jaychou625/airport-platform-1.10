package com.br.entity.map;

import lombok.*;

/**
 * 预警实体类
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Aew {
    @Getter @Setter Integer aewSeq;
    @Getter @Setter Integer aewLevel;
    @Getter @Setter String  aewScene;
}
