package xiaobinx.mybatis.page.pagesqlbuilder;

import xiaobinx.mybatis.page.Page;

public interface PageSqlBuilder {

	public String buildPageSql(String sql, Page page);

	public String buildCountSql(String sql);

}
