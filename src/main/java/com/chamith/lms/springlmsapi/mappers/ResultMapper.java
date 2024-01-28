package com.chamith.lms.springlmsapi.mappers;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ResultMapper {

    @Insert("insert into results(student_name, email, subject , results) values(#{student_name}, #{email}, #{subject},#{results})")
    void addResults(ResultsRequestDTO resultsRequestDTO);

    @Select("SELECT COUNT(*) > 0 FROM results WHERE subject = #{subject}")
    boolean doesSubjectExist(String subject);

    @Select("SELECT COUNT(*) > 0 FROM results WHERE email = #{email}")
    boolean doesResultsExist(String email);

    @Delete("DELETE FROM results WHERE email = #{email}")
    void deleteUserFromResults(String email);

    @Select("SELECT COUNT(*) > 0 FROM results WHERE email = #{email} AND subject = #{subject}")
    boolean doesResultsExistByEmailAndSubject(String email , String subject);
}
