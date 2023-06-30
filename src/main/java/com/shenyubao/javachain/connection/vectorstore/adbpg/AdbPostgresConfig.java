package com.shenyubao.javachain.connection.vectorstore.adbpg;

import com.alibaba.druid.pool.DruidDataSource;
import com.shenyubao.javachain.connection.embeddings.Embeddings;
import com.shenyubao.javachain.connection.vectorstore.adbpg.mapper.AdbPostgresKnowledgeMapper;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author shenyubao
 * @date 2023/6/29 23:21
 */
//@Configuration
//@MapperScan(basePackages = "com.shenyubao.javachain.connection.vectorstore.adbpg.mapper", sqlSessionFactoryRef = AdbPostgresConfig.SQL_SESSION_FACTORY_NAME)
public class AdbPostgresConfig {
//
//    private static final String ENDPOINT = VectorstoreConfiguratio.ADBPG_DATASOURCE_ENDPOINT;
//
//    private static final String DBNAME = VectorstoreConfiguration.ADBPG_DATASOURCE_DATABASENAME;
//
//    private static final String DATABASE_U = VectorstoreConfiguration.ADBPG_DATASOURCE_U;
//
//    private static final String DATABASE_P = VectorstoreConfiguration.ADBPG_DATASOURCE_P;
//
//    private static final String DATA_SOURCE_NAME = "adbpgDataSource";
//
//    public static final String SQL_SESSION_FACTORY_NAME = "adbpgSqlSessionFactory";
//
//    private static final String TRANSACTION_MANAGER = "adbpgTransactionManager";
//
//    @Bean(name = DATA_SOURCE_NAME)
//    public DataSource getDataSource() throws SQLException {
//        String url = "jdbc:postgresql://" + ENDPOINT + "/" + DBNAME + "?preferQueryMode=simple&tcpKeepAlive=true";
//
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(url);
//
//        druidDataSource.setUsername(DATABASE_U);
//        druidDataSource.setPassword(DATABASE_P);
//
//        // 初始化连接数
//        druidDataSource.setInitialSize(5);
//        // 最大连接数
//        druidDataSource.setMaxActive(20);
//
//        // 连接等待超时时间
//        druidDataSource.setMaxWait(12000);
//
//        // 配置间隔多久进行一次检测，检测需要关闭的空闲连接
//        druidDataSource.setTimeBetweenEvictionRunsMillis(3000);
//        druidDataSource.setValidationQuery("SELECT 1");
//        druidDataSource.setFilters("stat");
//        druidDataSource.setTestWhileIdle(true);
//        // 配置从连接池获取连接时，是否检查连接有效性，true每次都检查；false不检查
//        druidDataSource.setTestOnBorrow(false);
//        // 配置向连接池归还连接时，是否检查连接有效性，true每次都检查；false不检查
//        druidDataSource.setTestOnReturn(false);
//
//        return druidDataSource;
//    }
//
////    @Bean(name = TRANSACTION_MANAGER)
////    public DataSourceTransactionManager hologresDataSourceTransactionManager(@Qualifier(DATA_SOURCE_NAME) DataSource dataSource) {
////        return new DataSourceTransactionManager(dataSource);
////    }
//
//    @Bean(name = SQL_SESSION_FACTORY_NAME)
//    public SqlSessionFactory hologresSqlSessionFactory(@Qualifier(DATA_SOURCE_NAME) DataSource dataSource)
//            throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        return sqlSessionFactoryBean.getObject();
//    }

//    @Bean
//    public AdbPostgresDB adbPostgresDB(AdbPostgresKnowledgeMapper adbPostgresKnowledgeMapper,
//                                       Embeddings embedding) {
//        AdbPostgresDB adbPostgresDB = new AdbPostgresDB();
//        adbPostgresDB.setEmbedding(embedding);
//        adbPostgresDB.setAdbPostgresKnowledgeMapper(adbPostgresKnowledgeMapper);
//        return adbPostgresDB;
//    }
}
