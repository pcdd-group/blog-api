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
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
@ApiIgnore
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

    @GetMapping("/jdk")
    public Result jdkInfo() {
        Map<String, Object> map = new HashMap<>();

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString();
            String hostName = addr.getHostName().toString();
            Properties props = System.getProperties();
            map.put("本机IP", ip);
            map.put("本机名称", hostName);
            map.put("操作系统的名称", props.getProperty("os.name"));
            map.put("操作系统的版本", props.getProperty("os.version"));
            map.put("操作系统的构架", props.getProperty("os.arch"));
            map.put("Java的运行环境版本", props.getProperty("java.version"));
            map.put("Java的运行环境供应商", props.getProperty("java.vendor"));
            map.put("Java供应商的URL", props.getProperty("java.vendor.url"));
            map.put("Java的安装路径", props.getProperty("java.home"));
            map.put("Java的虚拟机规范版本", props.getProperty("java.vm.specification.version"));
            map.put("Java的虚拟机规范供应商", props.getProperty("java.vm.specification.vendor"));
            map.put("Java的虚拟机规范名称", props.getProperty("java.vm.specification.name"));
            map.put("Java的虚拟机实现版本", props.getProperty("java.vm.version"));
            map.put("Java的虚拟机实现供应商", props.getProperty("java.vm.vendor"));
            map.put("Java的虚拟机实现名称", props.getProperty("java.vm.name"));

            map.put("Java运行时环境规范版本", props.getProperty("java.specification.version"));
            map.put("Java运行时环境规范供应商", props.getProperty("java.specification.vender"));
            map.put("Java运行时环境规范名称", props.getProperty("java.specification.name"));
            map.put("Java的类格式版本号", props.getProperty("java.class.version"));
            map.put("默认的临时文件路径", props.getProperty("java.io.tmpdir"));
            map.put("一个或多个扩展目录的路径", props.getProperty("java.ext.dirs"));
            map.put("文件分隔符", props.getProperty("file.separator"));
            map.put("路径分隔符", props.getProperty("path.separator"));
            map.put("行分隔符", props.getProperty("line.separator"));
            map.put("用户的账户名称", props.getProperty("user.name"));
            map.put("用户的主目录", props.getProperty("user.home"));
            map.put("用户的当前工作目录", props.getProperty("user.dir"));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return Result.success(map);

    }

    @GetMapping("/sys")
    public Result sysInfo() {
        Map<String, Object> map = new HashMap<>();
        Runtime r = Runtime.getRuntime();
        int availableProcessors = r.availableProcessors();
        long freeMemory = r.freeMemory();
        long totalMemory = r.totalMemory();
        long maxMemory = r.maxMemory();
        map.put("可用的处理器个数", availableProcessors);
        map.put("Java虚拟机中的可用内存量", freeMemory / Math.pow(1024, 2));
        map.put("Java虚拟机中的内存总量", totalMemory / Math.pow(1024, 2));
        map.put("Java虚拟机将尝试使用的最大内存量", maxMemory / Math.pow(1024, 2));

        return Result.success(map);
    }

}
