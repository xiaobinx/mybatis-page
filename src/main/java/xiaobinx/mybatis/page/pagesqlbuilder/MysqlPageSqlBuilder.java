package xiaobinx.mybatis.page.pagesqlbuilder;

import xiaobinx.mybatis.page.Page;

public class MysqlPageSqlBuilder implements PageSqlBuilder {

	@Override
	public String buildPageSql(String sql, Page page) {
		long offset = (page.getP() - 1) * page.getPageSize();
		return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(page.getPageSize())
				.toString();
	}

	@Override
	public String buildCountSql(String sql) {
		return new StringBuilder("select count(1) from (").append(sql).append(") PAGE_ALIAS").toString();
	}
}
