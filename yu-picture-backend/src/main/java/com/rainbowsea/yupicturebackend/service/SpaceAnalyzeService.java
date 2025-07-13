package com.rainbowsea.yupicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbowsea.yupicturebackend.model.dto.space.analyze.SpaceAnalyzeRequest;
import com.rainbowsea.yupicturebackend.model.dto.space.analyze.SpaceCategoryAnalyzeRequest;
import com.rainbowsea.yupicturebackend.model.dto.space.analyze.SpaceRankAnalyzeRequest;
import com.rainbowsea.yupicturebackend.model.dto.space.analyze.SpaceSizeAnalyzeRequest;
import com.rainbowsea.yupicturebackend.model.dto.space.analyze.SpaceTagAnalyzeRequest;
import com.rainbowsea.yupicturebackend.model.dto.space.analyze.SpaceUsageAnalyzeRequest;
import com.rainbowsea.yupicturebackend.model.dto.space.analyze.SpaceUserAnalyzeRequest;
import com.rainbowsea.yupicturebackend.model.entity.Picture;
import com.rainbowsea.yupicturebackend.model.entity.Space;
import com.rainbowsea.yupicturebackend.model.entity.User;
import com.rainbowsea.yupicturebackend.model.vo.space.analyze.SpaceCategoryAnalyzeResponse;
import com.rainbowsea.yupicturebackend.model.vo.space.analyze.SpaceSizeAnalyzeResponse;
import com.rainbowsea.yupicturebackend.model.vo.space.analyze.SpaceTagAnalyzeResponse;
import com.rainbowsea.yupicturebackend.model.vo.space.analyze.SpaceUsageAnalyzeResponse;
import com.rainbowsea.yupicturebackend.model.vo.space.analyze.SpaceUserAnalyzeResponse;

import java.util.List;

public interface SpaceAnalyzeService extends IService<Space> {


    /**
     * 空间使用排行分析(仅管理员)可用
     * @param spaceRankAnalyzeRequest
     * @param loginUser
     * @return
     */
    List<Space> getSpaceRankAnalyze(SpaceRankAnalyzeRequest spaceRankAnalyzeRequest, User loginUser);

    /**
     * 获取空间用户上传行为分析
     * @param spaceUserAnalyzeRequest
     * @param loginUser
     * @return
     */
    List<SpaceUserAnalyzeResponse> getSpaceUserAnalyze(SpaceUserAnalyzeRequest spaceUserAnalyzeRequest, User loginUser);

    /**
     * 获取空间图片大小分析
     * @param spaceSizeAnalyzeRequest
     * @param loginUser
     * @return
     */
    List<SpaceSizeAnalyzeResponse> getSpaceSizeAnalyze(SpaceSizeAnalyzeRequest spaceSizeAnalyzeRequest, User loginUser);

    /**
     * 获取空间图片标签分析
     * @param spaceTagAnalyzeRequest
     * @param loginUser
     * @return
     */
    List<SpaceTagAnalyzeResponse> getSpaceTagAnalyze(SpaceTagAnalyzeRequest spaceTagAnalyzeRequest, User loginUser);

    /**
     * 获取空间图片分类分析
     * @param spaceCategoryAnalyzeRequest
     * @param loginUser
     * @return
     */
    List<SpaceCategoryAnalyzeResponse> getSpaceCategoryAnalyze(SpaceCategoryAnalyzeRequest spaceCategoryAnalyzeRequest, User loginUser);

    /**
     * 获取空间使用分析数据
     *
     * @param spaceUsageAnalyzeRequest SpaceUsageAnalyzeRequest 请求参数
     * @param loginUser                当前登录用户
     * @return SpaceUsageAnalyzeResponse 分析结果
     */
    SpaceUsageAnalyzeResponse getSpaceUsageAnalyze(SpaceUsageAnalyzeRequest spaceUsageAnalyzeRequest, User loginUser);
}
