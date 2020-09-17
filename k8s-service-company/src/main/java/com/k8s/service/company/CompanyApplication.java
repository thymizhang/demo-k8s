package com.k8s.service.company;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.time.LocalDateTime;

/**
 * @Author thymi
 * @Date 2020/8/18
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyApplication {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);

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

    @Value("${server.port}")
    private String port;

    @GetMapping("/port")
    public String getPort(){
        return port;
    }

    @GetMapping("/name")
    public String getName(){
        return "ylwq company.";
    }

    @GetMapping("/now")
    public String getTime(){
        LocalDateTime now = LocalDateTimeUtil.now();
        return LocalDateTimeUtil.format(now, DatePattern.NORM_DATETIME_PATTERN);
    }
}
