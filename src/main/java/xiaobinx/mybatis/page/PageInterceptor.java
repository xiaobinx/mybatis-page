package xiaobinx.mybatis.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import xiaobinx.mybatis.page.pagesqlbuilder.PageSqlBuilder;

@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }))
public class PageInterceptor implements Interceptor {

	private PageSqlBuilder pageSqlBuilder;

	public PageInterceptor() {
		super();
	}

	public PageInterceptor(PageSqlBuilder pageSqlBuilder) {
		super();
		this.pageSqlBuilder = pageSqlBuilder;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql();
		Object parameter = boundSql.getParameterObject();

		// 查找参数表中的page对象
		Page page = null;
		if (parameter instanceof Map) {
			Map parameterMap = (Map) parameter;
			for (Object object : parameterMap.values()) {
				if (object instanceof Page) {
					page = (Page) object;
					break;
				}
			}
		} else if (parameter instanceof Page) {
			page = (Page) parameter;
		}

		if (null != page) {
			Connection connection = (Connection) invocation.getArgs()[0];
			handle(connection, statementHandler, page);
		}

		return invocation.proceed();
	}

	private void handle(Connection connection, StatementHandler statementHandler, Page page) throws SQLException {
		BoundSql boundSql = statementHandler.getBoundSql();
		String sql = boundSql.getSql();
		String countSql = pageSqlBuilder.buildCountSql(sql);
		String pageSql = pageSqlBuilder.buildPageSql(sql, page);

		MetaObject statementHandlerMetaObject = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) statementHandlerMetaObject
				.getValue("delegate.mappedStatement");

		int count = getCount(connection, countSql, boundSql, mappedStatement);
		page.setTotalSize(count);
		statementHandlerMetaObject.setValue("delegate.boundSql.sql", pageSql);
	}

	private int getCount(Connection connection, String countSql, BoundSql boundSql, MappedStatement mappedStatement)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(),
				boundSql);
		try {
			ps = connection.prepareStatement(countSql);
			parameterHandler.setParameters(ps);
			rs = ps.executeQuery();
			int cout = 0;
			if (rs.next()) {
				cout = rs.getInt(1);
			}
			return cout;
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
			} finally {
				if (null != ps) {
					ps.close();
				}
			}
		}
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

	public PageSqlBuilder getPageSqlBuilder() {
		return pageSqlBuilder;
	}

	public void setPageSqlBuilder(PageSqlBuilder pageSqlBuilder) {
		this.pageSqlBuilder = pageSqlBuilder;
	}

}
