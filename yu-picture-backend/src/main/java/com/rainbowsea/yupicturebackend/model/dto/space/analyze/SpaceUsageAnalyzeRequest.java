package com.rainbowsea.yupicturebackend.model.dto.space.analyze;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 空间资源使用分析
 */
@EqualsAndHashCode(callSuper = true)  // 表示lombok为父类生成 get set 方法
@Data
public class SpaceUsageAnalyzeRequest extends SpaceAnalyzeRequest {

    private static final long serialVersionUID = -1065317415598425470L;
}
