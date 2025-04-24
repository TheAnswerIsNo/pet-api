package com.wait.app.domain.dto.adopt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 天
 * Time: 2025/4/24 18:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationDTO {

    private Integer code;

    private String msg;

    private String taskNo;

    private Result data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result{
        private List<Info> results;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info{

        private String score;

        private String name;
    }

}
