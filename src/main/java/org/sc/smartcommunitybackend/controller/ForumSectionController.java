package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.response.SectionResponse;
import org.sc.smartcommunitybackend.service.ForumSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论坛板块Controller
 */
@RestController
@RequestMapping("/forum/section")
@Tag(name = "论坛板块管理", description = "论坛板块相关接口")
public class ForumSectionController extends BaseController {
    
    @Autowired
    private ForumSectionService forumSectionService;
    
    @GetMapping("/list")
    @Operation(summary = "获取板块列表", description = "获取所有启用的板块列表")
    public Result<List<SectionResponse>> getSectionList() {
        List<SectionResponse> sections = forumSectionService.getActiveSections();
        return success(sections);
    }
}
