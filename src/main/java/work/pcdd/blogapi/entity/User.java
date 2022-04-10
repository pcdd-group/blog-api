package work.pcdd.blogapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 由于使用了Redis缓存，需要序列化和反序列化LocalDateTime类型的字段
 *
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */
@ApiModel("用户实体")
@Data
@TableName("m_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    @Length(max = 12, message = "用户名最大长度不能超过12")
    private String username;

    @ApiModelProperty("头像url")
    @URL(message = "头像地址必须是url格式")
    private String avatar;

    @ApiModelProperty(value = "邮箱",required = true)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度必须为6-16")
    private String password;

    @ApiModelProperty("账号状态：1为正常，-1为被锁定，0为被禁用")
    private Integer status;

    @ApiModelProperty("角色")
    private String role;

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
