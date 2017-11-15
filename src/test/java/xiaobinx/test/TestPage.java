package xiaobinx.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import xiaobinx.mybatis.page.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestPage {
	@Resource
	private UserMapper userMapper;

	@Test
	public void test() {
		Page page = new Page(2);
		page.setPageSize(2);
		List<User> list = userMapper.list(page);
		for (User user : list) {
			System.err.println(user);
		}
	}
}
