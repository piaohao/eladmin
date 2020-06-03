package org.piaohao.recruitment;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RecruitmentAppTest {

    @Autowired
    private JdbcTemplate template;

    @Test
    public void rename() {
        template.update("rename table admin_user to biz_admin_user");
    }

    @Test
    public void renameBatch() {
        String str = "apprentice_project\n" +
                "apprentice_project_applicant\n" +
                "banner\n" +
                "city\n" +
                "college\n" +
                "company\n" +
                "company_financing\n" +
                "company_following\n" +
                "company_message\n" +
                "company_scale\n" +
                "company_user\n" +
                "company_welfare\n" +
                "district\n" +
                "education\n" +
                "feed_back\n" +
                "industry\n" +
                "interest\n" +
                "major\n" +
                "mentor\n" +
                "message\n" +
                "resume\n" +
                "resume_academic\n" +
                "resume_education\n" +
                "resume_intention\n" +
                "resume_interest\n" +
                "resume_intern\n" +
                "resume_prize\n" +
                "resume_project\n" +
                "resume_skill\n" +
                "resume_status\n" +
                "resume_summarize\n" +
                "resume_work\n" +
                "salary_type\n" +
                "school\n" +
                "school_user\n" +
                "skill_tag\n" +
                "state\n" +
                "student_user\n" +
                "union_project\n" +
                "vacancy\n" +
                "vacancy_bookmark\n" +
                "vacancy_skill_tag\n" +
                "vacancy_workplace\n" +
                "vcc_applicant\n" +
                "vcc_type\n" +
                "verify_code";
        StrUtil.split(str, '\n')
                .forEach(name -> template.update(StrUtil.format("rename table {} to biz_{}", name, name)));
    }

}