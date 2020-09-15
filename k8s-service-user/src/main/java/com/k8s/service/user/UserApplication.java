package com.k8s.service.user;

import com.k8s.service.user.feign.CompanyFeign;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author thymi
 * @Date 2020/8/18
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
@RequestMapping("/user")
@Slf4j
public class UserApplication {

    /**
     * 加入该段内容, prometheus才能获取到数据
     * @param applicationName
     * @return
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);

        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long mb = 1024 * 1024;
        String mega = " MB";

        long physicalMemory;
        try {
            physicalMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        } catch (Exception e) {
            physicalMemory = -1L;
        }

        int availableCores = Runtime.getRuntime().availableProcessors();

        log.info("========================== System Info ==========================");
        log.info("Java version: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        log.info("Operating system: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        log.info("CPU Cores: " + availableCores);
        if (physicalMemory != -1L) {
            log.info("Physical Memory: " + format.format(physicalMemory / mb) + mega);
        }
        log.info("========================== JVM Memory Info ==========================");
        log.info("Max allowed memory: " + format.format(maxMemory / mb) + mega);
        log.info("Allocated memory:" + format.format(allocatedMemory / mb) + mega);
        log.info("Used memory in allocated: " + format.format((allocatedMemory - freeMemory) / mb) + mega);
        log.info("Free memory in allocated: " + format.format(freeMemory / mb) + mega);
        log.info("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
        log.info("Heap Memory Usage: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        log.info("Non-Heap Memory Usage: " + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
        log.info("=================================================================\n");
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private CompanyFeign companyFeign;

    @Value("${server.port}")
    private String port;

    @GetMapping("/port")
    public String getPort() {
        return port;
    }

    /**
     * 获取容器上的服务列表
     *
     * @return
     */
    @GetMapping("/service")
    public List<String> getServiceList() {
        return discoveryClient.getServices();
    }

    @GetMapping("/{user}/{pwd}")
    public String userAuth(@PathVariable("user") String user,@PathVariable("pwd") String pwd){
        Map<String,String> users = new HashMap<>();
        users.put("thymi","111111");
        users.put("bill","111111");
        users.put("mask","111111");
        users.put("jack","111111");

        String userPassword = users.get(user);
        if(userPassword.equals(pwd)){
            return user + " login success.";
        }

        return user + " login failed.";
    }

    /**
     * 获取容器中的服务信息
     *
     * @param name
     * @return
     */
    @GetMapping("/instance/{name}")
    public Object getInstance(@PathVariable("name") String name) {
        return discoveryClient.getInstances(name);
    }

    @GetMapping("/company")
    public String getCompany() {
        log.info(">>> 调用company接口 <<<");
        return companyFeign.getCompanyName();
    }
}
