package cn.aradin.mybatis.plus.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration
@EnableTransactionManagement
public class AradinMybatisPlusAutoConfiguration {
	
	/**
	 * Add Page Module To Support Page Select
	 * @return
	 */
	@Bean
    public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(-1l);
		return paginationInterceptor;
    }
}
