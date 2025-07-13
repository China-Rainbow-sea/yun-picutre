package com.rainbowsea.yupicturebackend.model.dto.space.analyze;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 空间图片分类分析请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpaceCategoryAnalyzeRequest extends SpaceAnalyzeRequest {

    private static final long serialVersionUID = -638253109655112721L;
}
