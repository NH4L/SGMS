package cn.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DbcpUtils {
	private static DataSource bds;
	static {
		if (bds == null)
			try {
				InputStream is = DbcpUtils.class.getClassLoader().getResourceAsStream("dpcp.properties");
				Properties props = new Properties();
				props.load(is);
				bds = BasicDataSourceFactory.createDataSource(props);
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private DbcpUtils() {
	}

	public static Connection getConnection() throws SQLException {
		return bds.getConnection();
	}

}
