package me.zhengjie;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.api.domain.generator.GenConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EladminSystemApplicationTests {

    @Test
    public void updateGenConfig() {
        StrUtil.split(bizTables, '\n')
                .forEach(name -> {
                    HttpResponse resp0 = HttpRequest.get("http://localhost:8000/api/genConfig/" + name)
                            .execute();
                    Long id = null;
                    if (resp0.getStatus() == HttpStatus.HTTP_OK) {
                        GenConfig genConfig = JSONUtil.toBean(resp0.body(), GenConfig.class);
                        id = genConfig.getId();
                    }
                    String filteredName = StrUtil.toCamelCase(StrUtil.removePrefix(name, "biz_"));
                    GenConfig config = new GenConfig();
                    config.setId(id);
                    config.setTableName(name);
                    config.setApiAlias("biz: " + StrUtil.upperFirst(filteredName));
                    config.setPack("me.zhengjie.modules.biz");
                    config.setModuleName("biz");
                    config.setPath("C:\\Users\\Administrator.DESKTOP-D3RJA7N\\IdeaProjects\\eladmin\\eladmin-web\\src\\views\\biz\\" + filteredName);
                    config.setApiPath("C:\\Users\\Administrator.DESKTOP-D3RJA7N\\IdeaProjects\\eladmin\\eladmin-web\\src\\api\\biz");
                    config.setAuthor("piaohao");
                    config.setPrefix("biz_");
                    config.setCover(false);

                    HttpResponse resp = HttpRequest.put("http://localhost:8000/api/genConfig")
                            .body(JSONUtil.toJsonStr(config), ContentType.JSON.toString())
                            .execute();
                    if (resp.getStatus() == HttpStatus.HTTP_OK) {
                        log.info("table:{} save success. id:{}", name, JSONUtil.toBean(resp.body(), GenConfig.class).getId());
                    } else {
                        log.error("table:{} save failed.", name);
                    }
                });
    }

    @Test
    public void updateColumns() {
        StrUtil.split(bizTables, '\n')
                .forEach(name -> {
                    HttpResponse resp = HttpRequest.get("http://localhost:8000/api/generator/columns")
                            .form("tableName", name)
                            .execute();
                    if (resp.getStatus() == HttpStatus.HTTP_OK) {
                        log.info("table:{} save success. column count:{}", name, JSONUtil.parseObj(resp.body()).getInt("totalElements"));
                    } else {
                        log.error("table:{} save failed.", name);
                    }
                });
    }

    @Test
    public void genCode() {
        StrUtil.split(bizTables, '\n')
                .forEach(name -> {
                    HttpResponse resp = HttpRequest.post(StrUtil.format("http://localhost:8000/api/generator/{}/{}", name, 0))
                            .execute();
                    if (resp.getStatus() == HttpStatus.HTTP_OK) {
                        log.info("generate code success.");
                    } else {
                        log.error("generate code failed.");
                    }
                });
    }

    String bizTables = "biz_admin_user\n" +
            "biz_apprentice_project\n" +
            "biz_apprentice_project_applicant\n" +
            "biz_banner\n" +
            "biz_city\n" +
            "biz_college\n" +
            "biz_company\n" +
            "biz_company_financing\n" +
            "biz_company_following\n" +
            "biz_company_message\n" +
            "biz_company_scale\n" +
            "biz_company_user\n" +
            "biz_company_welfare\n" +
            "biz_district\n" +
            "biz_education\n" +
            "biz_feed_back\n" +
            "biz_industry\n" +
            "biz_interest\n" +
            "biz_major\n" +
            "biz_mentor\n" +
            "biz_message\n" +
            "biz_resume\n" +
            "biz_resume_academic\n" +
            "biz_resume_education\n" +
            "biz_resume_intention\n" +
            "biz_resume_interest\n" +
            "biz_resume_intern\n" +
            "biz_resume_prize\n" +
            "biz_resume_project\n" +
            "biz_resume_skill\n" +
            "biz_resume_status\n" +
            "biz_resume_summarize\n" +
            "biz_resume_work\n" +
            "biz_salary_type\n" +
            "biz_school\n" +
            "biz_school_user\n" +
            "biz_skill_tag\n" +
            "biz_state\n" +
            "biz_student_user\n" +
            "biz_union_project\n" +
            "biz_vacancy\n" +
            "biz_vacancy_bookmark\n" +
            "biz_vacancy_skill_tag\n" +
            "biz_vacancy_workplace\n" +
            "biz_vcc_applicant\n" +
            "biz_vcc_type\n" +
            "biz_verify_code";

}

