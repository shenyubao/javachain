package com.shenyubao.javachain.connection.vectorstore.adbpg.mapper;

import com.shenyubao.javachain.connection.vectorstore.adbpg.KnowledgeDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/29 23:16
 */
@Mapper
public interface AdbPostgresKnowledgeMapper {
    //    SELECT id, price FROM products WHERE
//    price > 100 AND price <= 200
//    AND InTime > '2019-03-01' AND InTime <= '2019-03-31'
//    ORDER BY
//    l2_distance(array[10,2.0,…, 512.0], feature)
//    LIMIT 100;

    @Select("select content <-> #{query}::real[] AS distance, content_id, type, idx, row_content " +
            "from knowledge " +
            "where type = #{type} order by distance asc limit #{limit}")
    @Results(id = "knowledgeDO", value = {
            @Result(property="distance",  column="distance"),
            @Result(property="id",   column="id"),
            @Result(property="contentId",  column="content_id"),
            @Result(property="type", column="type"),
            @Result(property="idx", column="idx"),
            @Result(property="rowContent", column="row_content"),
    })
    List<KnowledgeDO> similaritySearch(@Param("query") String query,
                                       @Param("type") Integer type,
                                       @Param("limit") Integer limit);

    /**
     * 参考：https://help.aliyun.com/document_detail/205193.html
     *
     * @param knowledgeDO
     * @return
     */
    @Insert("insert into knowledge(content_id, type, content, idx, row_content)" +
            "values(#{contentId}, #{type}, #{content}, #{idx}, #{rowContent})")
    @Options(useGeneratedKeys = true)
    Long insert(KnowledgeDO knowledgeDO);
}
