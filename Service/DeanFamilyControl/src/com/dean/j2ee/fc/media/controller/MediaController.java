package com.dean.j2ee.fc.media.controller;

import com.dean.j2ee.fc.media.service.MediaService;
import com.dean.j2ee.framework.controller.ConvenientController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 媒体控制器
 */
@Controller
@RequestMapping(value = "/media")
public class MediaController extends ConvenientController {

    @Autowired
    private MediaService mediaService;

    /**
     * 开启摄像头
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/startCamera/{token}", method = RequestMethod.POST)
    @ResponseBody
    public Object startCamera(@PathVariable String token) {
        return null;
    }

    /**
     * 开启视频媒体流
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/startPhotograph/{token}", method = RequestMethod.POST)
    @ResponseBody
    public Object startPhotograph(@PathVariable String token) {
        return mediaService.startPhotograph(token);
    }

    /**
     * 停止视频媒体流
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/stopPhotograph/{token}", method = RequestMethod.POST)
    @ResponseBody
    public Object stopPhotograph(@PathVariable String token) {
        return mediaService.stopPhotograph(token);
    }

}
