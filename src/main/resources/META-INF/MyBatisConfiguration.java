package xiaobinx.mymis.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import xiaobinx.mybatis.page.PageInterceptor;
import xiaobinx.mybatis.page.pagesqlbuilder.MysqlPageSqlBuilder;

@Configuration
@EnableTransactionManagement
public class MyBatisConfiguration {

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		bean.setConfiguration(configuration);
		bean.setDataSource(dataSource);
		// 添加插件
		bean.setPlugins(new Interceptor[] { new PageInterceptor(new MysqlPageSqlBuilder()) });
		return bean.getObject();
	}
}
