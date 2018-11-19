package com.gloic.freebird.services.vo.request;

import lombok.Getter;

/**
 * @author gloic
 */
@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
