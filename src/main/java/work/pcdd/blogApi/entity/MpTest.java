package work.pcdd.blogApi.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-04-15
 */
@Data
public class MpTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "p_id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "p_name")
    private String name;

    @TableField(value = "p_price")
    private BigDecimal price;

    @TableField(value = "p_description")
    private String description;


}
