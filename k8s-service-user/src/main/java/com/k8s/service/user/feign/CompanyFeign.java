package com.k8s.service.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author thymi
 * @Date 2020/8/18
 */
@FeignClient(name = "service-company", url = "http://company:7001")
public interface CompanyFeign {
    /**
     * 获取公司名
     *
     * @return
     */
    @GetMapping("/company/name")
    String getCompanyName();
}
