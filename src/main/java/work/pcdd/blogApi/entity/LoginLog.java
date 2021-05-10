package work.pcdd.blogApi.entity;

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
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 1907263405@qq.com
 * @since 2021-04-15
 */
@ApiModel("登录日志实体")
@Data
@TableName("login_log")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录日志id")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "登录时间", required = true)
    @NotNull(message = "登录时间不能为空")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDatetime;

    @ApiModelProperty(value = "ip地址", required = true)
    @NotBlank(message = "ip地址不能为空")
    private String ipAddr;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("浏览器内核")
    private String engine;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("是否为移动端")
    @NotNull(message = "是否为移动终端不能为空")
    private Boolean isMobile;

}
