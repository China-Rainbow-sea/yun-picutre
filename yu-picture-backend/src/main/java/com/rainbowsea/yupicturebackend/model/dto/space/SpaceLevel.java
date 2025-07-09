package com.rainbowsea.yupicturebackend.model.dto.space;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 空间级别
 */
@Data
@AllArgsConstructor
public class SpaceLevel implements Serializable {

    private static final long serialVersionUID = -1521979603356429615L;
    /**
     * 值
     */
    private int value;

    /**
     * 中文
     */
    private String text;

    /**
     * 最大数量
     */
    private long maxCount;


    /**
     * 最大容量
     */
    private long maxSize;
}
