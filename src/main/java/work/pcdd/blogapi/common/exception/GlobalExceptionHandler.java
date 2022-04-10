package work.pcdd.blogapi.common.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import work.pcdd.blogapi.common.vo.Result;

/**
 * @author 1907263405@qq.com
 * @date 2021/2/25 22:52
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获实体校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常：", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(objectError.getDefaultMessage());
    }

    /**
     * 捕获断言异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("断言异常：", e);
        return Result.fail(400, e.getMessage());
    }

    /**
     * 捕获JWT校验异常
     * SignatureVerificationException好像捕获不到，只能由RuntimeException捕获
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JWTVerificationException.class)
    public Result handler(JWTVerificationException e) {
        log.error("JWT校验异常：{}", e.getMessage(), e);
        return Result.fail(401, e.getMessage());
    }

    /**
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     * 压根就没登录（例如游客）
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MyUnauthenticatedException.class)
    public Result handler(MyUnauthenticatedException e) {
        log.error("JWT校验异常：", e);
        return Result.fail(401, e.getMessage());
    }

    /**
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     * 已登录，但权限不足（例如用户访问管理员的接口）
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(MyUnauthorizedException.class)
    public Result handler(MyUnauthorizedException e) {
        log.error("JWT校验异常：", e);
        return Result.fail(403, e.getMessage());
    }

    /**
     * 捕捉接口404异常
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handle(NoHandlerFoundException e) {
        return Result.fail(404, "请求的资源不存在");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public Result handle(RuntimeException e) {
        log.error("运行异常：", e);
        return Result.fail(500, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handle(Exception e) {
        log.error("其他异常：", e);
        return Result.fail(500, e.getMessage());
    }

}
