package com.rainbowsea.yupicturebackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import lombok.Getter;

/**
 * 图片审核状态枚举类
 */
@Getter
public enum PictureReviewStatusEnum {
    REVIEWING("待审核", 0),
    PASS("通过", 1),
    REJECT("拒绝", 2);

    private final String text;

    private final int value;

    PictureReviewStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }


    /**
     * 根据 value 获取枚举
     * @param value  枚举值的 value
     * @return 枚举值对象
     */
    public static PictureReviewStatusEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        // 遍历枚举
        for (PictureReviewStatusEnum pictureReviewStatusEnum : PictureReviewStatusEnum.values()) {
            if (pictureReviewStatusEnum.value == value) {
                return pictureReviewStatusEnum;
            }
        }
        // 该枚举值 value 的 text 不存在
        return null;
    }

}
