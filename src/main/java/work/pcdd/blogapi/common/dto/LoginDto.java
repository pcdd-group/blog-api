package work.pcdd.blogapi.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 1907263405@qq.com
 * @date 2021/2/24 14:49
 */
@ApiModel("登录dto")
@Data
public class LoginDto implements Serializable {

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    @Length(max = 25,message = "用户名长度不能超过25位")
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    @Length(max = 16,message = "密码长度不能超过16位")
    private String password;

}
