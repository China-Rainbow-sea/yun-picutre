package com.rainbowsea.yupicturebackend.model.dto.space.analyze;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 空间图片大小分析请求类(后端传递信息交互类)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpaceSizeAnalyzeRequest extends SpaceAnalyzeRequest {

    private static final long serialVersionUID = 6658559344484040061L;
}
