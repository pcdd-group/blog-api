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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */
@ApiModel("文章实体")
@Data
@TableName("m_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "文章id")
    private Long id;

    @ApiModelProperty(value = "用户id",required = true)
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "文章标题",required = true)
    @NotBlank(message = "文章标题不能为空")
    private String title;

    @ApiModelProperty(value = "文章描述",required = true)
    @NotBlank(message = "文章描述不能为空")
    private String description;

    @ApiModelProperty(value = "文章内容",required = true)
    @NotBlank(message = "文章内容不能为空")
    private String content;

    @ApiModelProperty("发布日期")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @ApiModelProperty("文章状态")
    private Integer status;

    //private User user;
}
