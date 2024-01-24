package com.chamith.lms.springlmsapi.mappers;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ResultMapper {

    @Insert("insert into results(student_name, email, subject , results) values(#{student_name}, #{email}, #{subject},#{results})")
    void addResults(ResultsRequestDTO resultsRequestDTO);
}
