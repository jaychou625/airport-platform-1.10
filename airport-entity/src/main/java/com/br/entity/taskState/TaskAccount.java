package com.br.entity.taskState;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskAccount {
    @Getter @Setter private Integer id;
    @Getter @Setter private String account;
    @Getter @Setter private String user_Name;
}
