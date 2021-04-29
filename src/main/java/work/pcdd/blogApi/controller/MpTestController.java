package work.pcdd.blogApi.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;
import work.pcdd.blogApi.common.vo.Result;
import work.pcdd.blogApi.entity.MpTest;
import work.pcdd.blogApi.mapper.BlogMapper;
import work.pcdd.blogApi.mapper.MpTestMapper;
import work.pcdd.blogApi.service.IMpTestService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-04-15
 */

@Api(tags = "测试接口")
@RestController
@RequestMapping("/test")
@Transactional(rollbackFor = Exception.class)
public class MpTestController {

    @Autowired
    IMpTestService mpTestService;

    @Autowired
    MpTestMapper mpTestMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    BlogMapper blogMapper;

    @GetMapping
    public Result generateQrCode(HttpServletRequest req, HttpServletResponse resp) {
        QrConfig config = new QrConfig(300, 300);
        String logo = "https://public-1300393241.cos.ap-beijing.myqcloud.com/qjl.png";
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\qjl.png";

        try {
            // 设置二维码logo
            config.setImg(ResourceUtils.getFile(path))
                    .setForeColor(Color.BLACK)
                    .setBackColor(Color.WHITE)
                    // 高纠错级别
                    .setErrorCorrection(ErrorCorrectionLevel.Q);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        resp.setContentType("image/jpeg;charset=UTF-8");
        //BufferedImage image = QrCodeUtil.generate("https://www.pcdd.work/", config);
        // 直接写到前端，不返回
        //boolean flag = ImageIO.write(image, "jpg", resp.getOutputStream());

        String imgBase64 = QrCodeUtil.generateAsBase64("https://www.pcdd.work/", config, "jpg");
        return Result.success(imgBase64);
    }

    @GetMapping("/time")
    public Result nowTime() {
        return Result.success(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @GetMapping("/path")
    public Result ins() {
        return Result.success(System.getProperty("user.dir"));
    }

    @GetMapping("/ip")
    public Result getIp(HttpServletRequest req) {
        return Result.success(req.getRemoteAddr());
    }

    @GetMapping("/fun1")
    public Result fun1() {
        // 调用fun2接口
        return Result.success(restTemplate.getForObject("http://localhost:8181/mp-test/fun2", String.class));
    }

    @GetMapping("/fun2")
    public String fun2() {
        return "调用了fun2接口！";
    }

}
