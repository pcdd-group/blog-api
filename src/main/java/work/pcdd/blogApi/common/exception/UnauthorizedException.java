package work.pcdd.blogApi.common.exception;

/**
 * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
 * 已登录，但权限不足（例如用户访问管理员的接口）
 *
 * @author 1907263405@qq.com
 * @date 2021/3/22 23:02
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

}
