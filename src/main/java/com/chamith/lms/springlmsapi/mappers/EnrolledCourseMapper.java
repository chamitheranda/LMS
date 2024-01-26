package com.chamith.lms.springlmsapi.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EnrolledCourseMapper {

    @Delete("DELETE FROM enrolled_courses WHERE email = #{email}")
    void deleteUserFromEnrolledCourses(String email);

    @Select("SELECT COUNT(*) > 0 FROM enrolled_courses WHERE subject = #{subject}")
    boolean doesEmailExistEnrolledCourses(String subject);

}
