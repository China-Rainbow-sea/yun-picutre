package com.rainbowsea.yupicturebackend.model.dto.space.analyze;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 空间用户上传行为分析请求(后端传递信息交互类)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpaceUserAnalyzeRequest extends SpaceAnalyzeRequest {

    private static final long serialVersionUID = 8235405937026939627L;
    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 时间维度：day / week / month
     */
    private String timeDimension;
}
