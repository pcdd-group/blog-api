package work.pcdd.blogApi.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 可以对外公开的用户信息
 *
 * @author 1907263405@qq.com
 * @date 2021/2/23 20:44
 */
@ApiModel("用户公开信息dto")
@Data
public class ProfileDto implements Serializable {

    @ApiModelProperty(value = "用户id",required = true)
    @NotNull(message = "用户id不能为空")
    private Long id;

    @ApiModelProperty("用户名")
    @Length(max = 12, message = "用户名最大长度不能超过12")
    private String username;

    @ApiModelProperty("头像url")
    @URL(message = "头像地址必须是url格式")
    private String avatar;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("注册日期")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @ApiModelProperty("最后一次登录时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;

}
