package work.pcdd.blogApi.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 1907263405@qq.com
 * @date 2021/2/25 22:46
 */
@ApiModel("请求响应JSON")
@Data
public class Result implements Serializable {
    @ApiModelProperty("响应码")
    private Integer code;

    @ApiModelProperty("响应信息")
    private String msg;

    @ApiModelProperty("响应数据")
    private Object data;


    public static Result success() {
        return Result.success(null);
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    public static Result fail(String msg) {
        return Result.fail(400, msg);
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
