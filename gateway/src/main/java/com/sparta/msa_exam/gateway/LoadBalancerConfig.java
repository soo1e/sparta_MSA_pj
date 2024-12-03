package com.sparta.msa_exam.gateway;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class LoadBalancerConfig {

    @Bean
    public ServiceInstanceListSupplier weightedServiceInstanceListSupplier() {
        return new WeightedServiceInstanceListSupplier("product-service");
    }

    static class WeightedServiceInstanceListSupplier implements ServiceInstanceListSupplier {
        private final String serviceId;
        private final Random random = new Random();

        public WeightedServiceInstanceListSupplier(String serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public String getServiceId() {
            return serviceId;
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            // 가중치에 따라 ServiceInstance를 구성
            List<ServiceInstance> instances = new ArrayList<>();

            // 여기에서 DiscoveryClient를 통해 서비스 인스턴스 가져오기 (예: Eureka)
            // 가상의 인스턴스를 아래처럼 예시로 사용
            ServiceInstance instance1 = createServiceInstance("product-service", "localhost", 19093, 70);
            ServiceInstance instance2 = createServiceInstance("product-service", "localhost", 19094, 30);

            for (int i = 0; i < 70; i++) { // 70% 가중치
                instances.add(instance1);
            }
            for (int i = 0; i < 30; i++) { // 30% 가중치
                instances.add(instance2);
            }

            return Flux.just(instances);
        }

        private ServiceInstance createServiceInstance(String serviceId, String host, int port, int weight) {
            return new ServiceInstance() {
                @Override
                public String getServiceId() {
                    return serviceId;
                }

                @Override
                public String getHost() {
                    return host;
                }

                @Override
                public int getPort() {
                    return port;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public java.net.URI getUri() {
                    return java.net.URI.create(String.format("http://%s:%d", host, port));
                }

                @Override
                public java.util.Map<String, String> getMetadata() {
                    return null;
                }
            };
        }
    }
}
