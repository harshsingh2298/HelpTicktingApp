package com.cms.cdl.beans;

import com.cms.cdl.dto.response_dto.EmpResDTO;
import com.cms.cdl.dto.user_dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmpAndUserResponse {
    private FileAndObjectTypeBean fileAndObjectTypeBean;
    private UserDTO userDTO;
}
