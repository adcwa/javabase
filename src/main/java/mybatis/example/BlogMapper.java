package mybatis.example;

import org.apache.ibatis.annotations.Select;

public interface BlogMapper {

    @Select("SELECT * FROM blog WHERE id = #{id}")
    Object selectBlog(int id);
}
