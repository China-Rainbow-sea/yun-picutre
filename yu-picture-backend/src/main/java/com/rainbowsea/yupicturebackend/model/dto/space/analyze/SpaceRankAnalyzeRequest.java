package com.rainbowsea.yupicturebackend.model.dto.space.analyze;

import lombok.Data;

import java.io.Serializable;


/**
 * 管理员对空间使用的排行分析(默认前10)，对于该功能实现，后端传递信息交互类
 */
@Data
public class SpaceRankAnalyzeRequest implements Serializable {

    /**
     * 排名前 N 的空间
     */
    private Integer topN = 10;

    private static final long serialVersionUID = 1L;
}
