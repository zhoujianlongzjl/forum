package com.qingfengzhuyue.forum.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
