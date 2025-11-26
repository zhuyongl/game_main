package com.tencent.wxcloudrun.controller;

import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.common.ApiResponse;
import com.tencent.wxcloudrun.exception.UserNotFoundException;
import com.tencent.wxcloudrun.model.dto.PageRequest;
import com.tencent.wxcloudrun.model.dto.UserRequest;
import com.tencent.wxcloudrun.model.dto.WechatLoginRequest;
import com.tencent.wxcloudrun.model.vo.UserVO;
import com.tencent.wxcloudrun.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    final UserService userService;
    final Logger logger;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(UserController.class);
    }

    /**
     * 分页获取所有用户
     * @param pageRequest 分页请求参数
     * @return API response json
     */
    @GetMapping
    ApiResponse getAllUsers(PageRequest pageRequest) {
        logger.info("/api/users get request, page: {}, size: {}", pageRequest.getPage(), pageRequest.getSize());

        PageInfo<UserVO> pageInfo = userService.getUsersVOWithPagination(pageRequest.getPage(), pageRequest.getSize());
        return ApiResponse.ok(pageInfo);
    }

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return API response json
     */
    @GetMapping("/{id}")
    ApiResponse getUserById(@PathVariable Integer id) {
        logger.info("/api/users/{} get request", id);
        UserVO userVO = userService.getUserVOById(id)
                .orElseThrow(() -> new UserNotFoundException("用户ID: " + id + " 不存在"));
        return ApiResponse.ok(userVO);
    }

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return API response json
     */
    @GetMapping("/username/{username}")
    ApiResponse getUserByUsername(@PathVariable String username) {
        logger.info("/api/users/username/{} get request", username);
        UserVO userVO = userService.getUserVOByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("用户名: " + username + " 不存在"));
        return ApiResponse.ok(userVO);
    }

    /**
     * 根据邮箱获取用户
     * @param email 邮箱
     * @return API response json
     */
    @GetMapping("/email/{email}")
    ApiResponse getUserByEmail(@PathVariable String email) {
        logger.info("/api/users/email/{} get request", email);
        UserVO userVO = userService.getUserVOByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("邮箱: " + email + " 不存在"));
        return ApiResponse.ok(userVO);
    }

    /**
     * 根据微信OpenId获取用户
     * @param wechatOpenId 微信OpenId
     * @return API response json
     */
    @GetMapping("/wechat/{wechatOpenId}")
    ApiResponse getUserByWechatOpenId(@PathVariable String wechatOpenId) {
        logger.info("/api/users/wechat/{} get request", wechatOpenId);
        UserVO userVO = userService.getUserVOByWechatOpenId(wechatOpenId)
                .orElseThrow(() -> new UserNotFoundException("微信OpenId: " + wechatOpenId + " 不存在"));
        return ApiResponse.ok(userVO);
    }

    /**
     * 创建用户
     * @param request {@link UserRequest}
     * @return API response json
     */
    @PostMapping
    ApiResponse createUser(@RequestBody UserRequest request) {
        logger.info("/api/users post request");

        UserVO userVO = userService.createUserAndReturnVO(request);
        return ApiResponse.ok(userVO);
    }

    /**
     * 更新用户
     * @param id 用户ID
     * @param request {@link UserRequest}
     * @return API response json
     */
    @PutMapping("/{id}")
    ApiResponse updateUser(@PathVariable Integer id, @RequestBody UserRequest request) {
        logger.info("/api/users/{} put request", id);

        UserVO userVO = userService.updateUserAndReturnVO(id, request)
                .orElseThrow(() -> new UserNotFoundException("用户ID: " + id + " 不存在"));
        return ApiResponse.ok(userVO);
    }

    /**
     * 微信登录
     * @param request {@link WechatLoginRequest}
     * @return API response json
     */
    @PostMapping("/wechat/login")
    ApiResponse wechatLogin(@RequestBody WechatLoginRequest request) {
        logger.info("/api/users/wechat/login post request");

        UserVO userVO = userService.wechatLogin(request);
        return ApiResponse.ok(userVO);
    }

    /**
     * 根据微信OpenId更新用户信息
     * @param wechatOpenId 微信OpenId
     * @param request {@link UserRequest}
     * @return API response json
     */
    @PutMapping("/wechat/{wechatOpenId}")
    ApiResponse updateWechatUserInfo(@PathVariable String wechatOpenId, @RequestBody UserRequest request) {
        logger.info("/api/users/wechat/{} put request", wechatOpenId);

        UserVO userVO = userService.updateWechatUserInfo(wechatOpenId, request)
                .orElseThrow(() -> new UserNotFoundException("微信OpenId: " + wechatOpenId + " 不存在的用户"));
        return ApiResponse.ok(userVO);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return API response json
     */
    @DeleteMapping("/{id}")
    ApiResponse deleteUser(@PathVariable Integer id) {
        logger.info("/api/users/{} delete request", id);

        userService.deleteUser(id);
        return ApiResponse.ok();
    }
}