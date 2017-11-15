package xiaobinx.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import xiaobinx.mybatis.page.Page;

@Mapper
public interface UserMapper {
	@Select("select * from user")
	public List<User> list(Page page);
}
