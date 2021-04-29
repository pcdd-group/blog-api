package work.pcdd.blogApi.common.exception;

/**
 * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
 * 压根就没登录（例如游客）
 *
 * @author 1907263405@qq.com
 * @date 2021/3/22 22:46
 */
public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException(String message) {
        super(message);
    }

}
